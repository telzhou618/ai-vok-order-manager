package com.rbac.dto

import java.math.BigDecimal

data class DashboardDto(
    // 订单统计
    var totalOrderCount: Long = 0,
    var todayOrderCount: Long = 0,
    var pendingPaymentCount: Long = 0,
    var completedOrderCount: Long = 0,
    var totalSalesAmount: BigDecimal = BigDecimal.ZERO,
    var todaySalesAmount: BigDecimal = BigDecimal.ZERO,
    var totalOrderUserCount: Long = 0,
    var todayOrderUserCount: Long = 0
)
