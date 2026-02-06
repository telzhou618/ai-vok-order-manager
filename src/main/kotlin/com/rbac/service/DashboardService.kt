package com.rbac.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.rbac.dto.DashboardDto
import com.rbac.entity.Order
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DashboardService(
    private val orderService: OrderService
) {

    fun getDashboardData(): DashboardDto {
        // 获取今天的开始时间
        val todayStart = LocalDate.now().atStartOfDay()

        // 订单总数
        val totalOrderCount = orderService.count()

        // 今日订单数
        val todayOrderCount = orderService.count(
            KtQueryWrapper(Order::class.java)
                .ge(Order::createdTime, todayStart)
        )

        // 待付款订单数（订单状态=1）
        val pendingPaymentCount = orderService.count(
            KtQueryWrapper(Order::class.java)
                .eq(Order::orderStatus, 1)
        )

        // 已完成订单数（订单状态=4）
        val completedOrderCount = orderService.count(
            KtQueryWrapper(Order::class.java)
                .eq(Order::orderStatus, 4)
        )

        // 总销售额（已付款的订单：支付状态=1）
        val paidOrders = orderService.list(
            KtQueryWrapper(Order::class.java)
                .eq(Order::payStatus, 1)
        )
        val totalSalesAmount = paidOrders.sumOf { it.actualAmount }

        // 今日销售额
        val todayPaidOrders = orderService.list(
            KtQueryWrapper(Order::class.java)
                .eq(Order::payStatus, 1)
                .ge(Order::payTime, todayStart)
        )
        val todaySalesAmount = todayPaidOrders.sumOf { it.actualAmount }

        // 总下单用户数（去重）
        val allOrders = orderService.list()
        val totalOrderUserCount = allOrders.map { it.userId }.distinct().count().toLong()

        // 今日下单用户数（去重）
        val todayOrders = orderService.list(
            KtQueryWrapper(Order::class.java)
                .ge(Order::createdTime, todayStart)
        )
        val todayOrderUserCount = todayOrders.map { it.userId }.distinct().count().toLong()

        return DashboardDto(
            totalOrderCount = totalOrderCount,
            todayOrderCount = todayOrderCount,
            pendingPaymentCount = pendingPaymentCount,
            completedOrderCount = completedOrderCount,
            totalSalesAmount = totalSalesAmount,
            todaySalesAmount = todaySalesAmount,
            totalOrderUserCount = totalOrderUserCount,
            todayOrderUserCount = todayOrderUserCount
        )
    }
}
