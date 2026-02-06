package com.rbac.config

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider
import com.zaxxer.hikari.HikariDataSource
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory
import org.apache.shardingsphere.infra.config.algorithm.AlgorithmConfiguration
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration
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
        // 创建数据源映射
        val dataSourceMap = mutableMapOf<String, DataSource>()
        dataSourceMap["ds0"] = masterDataSource()

        // 创建分片规则配置
        val shardingRuleConfig = ShardingRuleConfiguration()

        // 配置分表规则
        shardingProperties.tableRuleConfigs.forEach { tableConfig ->
            val tableRuleConfig = ShardingTableRuleConfiguration(
                tableConfig.logicTableName,
                "ds0.${tableConfig.logicTableName}_\${0..${shardingProperties.shardingSize - 1}}"
            )

            // 配置分表策略（按指定列取模）
            val shardingAlgorithmName = "${tableConfig.logicTableName}_mod"
            tableRuleConfig.tableShardingStrategy = StandardShardingStrategyConfiguration(
                tableConfig.shardingColumn,
                shardingAlgorithmName
            )

            shardingRuleConfig.tables.add(tableRuleConfig)

            // 配置取模算法
            shardingRuleConfig.shardingAlgorithms[shardingAlgorithmName] = AlgorithmConfiguration(
                "MOD",
                Properties().apply {
                    setProperty("sharding-count", shardingProperties.shardingSize.toString())
                }
            )
        }

        // 创建 ShardingSphere 数据源
        val props = Properties().apply {
            setProperty("sql-show", shardingProperties.showSql.toString())
        }

        return ShardingSphereDataSourceFactory.createDataSource(
            dataSourceMap,
            listOf<RuleConfiguration>(shardingRuleConfig),
            props
        )
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