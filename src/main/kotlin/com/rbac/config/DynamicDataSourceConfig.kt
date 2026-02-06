package com.rbac.config

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider
import com.rbac.common.ShardingProperties
import com.rbac.common.TableRuleConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory
import org.apache.shardingsphere.infra.config.RuleConfiguration
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.*
import javax.sql.DataSource

@Configuration
class DynamicDataSourceConfig {

    @Autowired
    private lateinit var shardingProperties: ShardingProperties

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    fun masterDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean
    fun shardingDataSource(): DataSource {
        // 1. 配置数据源
        val dataSourceMap = mutableMapOf<String, DataSource>()
        dataSourceMap["ds"] = masterDataSource()

        // 2. 配置分片规则
        val shardingRuleConfig = getShardingRuleConfiguration(shardingProperties)

        // 3. 配置属性
        val props = Properties().apply {
            setProperty("sql-show", shardingProperties.showSql.toString())
        }

        return ShardingSphereDataSourceFactory.createDataSource(
            dataSourceMap,
            listOf<RuleConfiguration>(shardingRuleConfig),
            props
        )
    }

    private fun getShardingRuleConfiguration(shardingProperties: ShardingProperties): ShardingRuleConfiguration {
        val shardingRuleConfig = ShardingRuleConfiguration()

        // 配置分表规则
        shardingProperties.tableRuleConfigs.forEach { tableConfig ->
            val tableRule = getShardingTableRuleConfiguration(shardingProperties, tableConfig)
            shardingRuleConfig.tables.add(tableRule)
        }

        // 默认分片算法
        val algorithmProps = Properties().apply {
            setProperty("sharding-count", shardingProperties.shardingSize.toString())
        }
        shardingRuleConfig.shardingAlgorithms["mod_sharding"] =
            ShardingSphereAlgorithmConfiguration("MOD", algorithmProps)

        // 默认主键生成算法
        shardingRuleConfig.keyGenerators["snowflake"] =
            ShardingSphereAlgorithmConfiguration("SNOWFLAKE", Properties())

        return shardingRuleConfig
    }

    private fun getShardingTableRuleConfiguration(
        shardingProperties: ShardingProperties,
        tableRuleConfig: TableRuleConfig
    ): ShardingTableRuleConfiguration {
        // 表分布
        val tableRule = ShardingTableRuleConfiguration(
            tableRuleConfig.logicTableName,
            "ds.${tableRuleConfig.logicTableName}_\${0..${shardingProperties.shardingSize - 1}}"
        )

        // 分片算法
        tableRule.tableShardingStrategy = StandardShardingStrategyConfiguration(
            tableRuleConfig.shardingColumn,
            "mod_sharding"
        )

        // 主键生成算法
        tableRule.keyGenerateStrategy = KeyGenerateStrategyConfiguration("id", "snowflake")

        return tableRule
    }

    @Primary
    @Bean
    fun dataSource(): DataSource {
        val providers = listOf(
            DynamicDataSourceProvider {
                mapOf(
                    "master" to masterDataSource(),
                    "sharding" to shardingDataSource()
                )
            }
        )
        val dynamicDataSource = DynamicRoutingDataSource(providers)
        dynamicDataSource.setPrimary("master")
        return dynamicDataSource
    }
}