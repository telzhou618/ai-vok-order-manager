package com.rbac.common

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "sharding")
data class ShardingProperties(
    var showSql: Boolean = false,
    var shardingSize: Int = 2,
    var tableRuleConfigs: List<TableRuleConfig> = emptyList()
)

data class TableRuleConfig(
    var logicTableName: String = "",
    var shardingColumn: String = ""
)
