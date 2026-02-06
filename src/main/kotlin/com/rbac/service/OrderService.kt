package com.rbac.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.rbac.dto.OrderDto
import com.rbac.dto.OrderQueryDto
import com.rbac.entity.Order
import com.rbac.mapper.OrderMapper
import org.springframework.stereotype.Service

/**
 * 订单服务
 * Mapper 已经使用 @DS("sharding") 注解，所有操作会自动路由到分表数据源
 */
@Service
class OrderService : ServiceImpl<OrderMapper, Order>() {
    
    /**
     * 根据查询条件查询订单列表
     */
    fun queryOrders(query: OrderQueryDto): List<Order> {
        val queryWrapper = lambdaQuery()
        
        // 如果有订单号，按订单号查询
        query.orderNo?.takeIf { it.isNotBlank() }?.let {
            queryWrapper.eq(Order::orderNo, it)
        }
        
        // 如果有用户ID，按用户ID查询（使用分片键，性能更好）
        query.userId?.let {
            queryWrapper.eq(Order::userId, it)
        }
        
        return queryWrapper
            .orderByDesc(Order::createdTime)
            .list()
    }
    
    /**
     * 根据用户ID查询订单列表
     * ShardingSphere 会根据 user_id 自动路由到对应的分表
     */
    fun listByUserId(userId: Long): List<Order> {
        return lambdaQuery()
            .eq(Order::userId, userId)
            .orderByDesc(Order::createdTime)
            .list()
    }
    
    /**
     * 根据订单号查询订单
     * 注意：如果不带分片键查询，会扫描所有分表
     */
    fun getByOrderNo(orderNo: String): Order? {
        return lambdaQuery()
            .eq(Order::orderNo, orderNo)
            .one()
    }
    
    /**
     * 转换为 DTO
     */
    fun toDto(order: Order): OrderDto {
        return OrderDto(
            id = order.id,
            orderNo = order.orderNo,
            userId = order.userId,
            orderStatus = order.orderStatus,
            orderStatusText = getOrderStatusText(order.orderStatus),
            payStatus = order.payStatus,
            payStatusText = getPayStatusText(order.payStatus),
            totalAmount = order.totalAmount,
            discountAmount = order.discountAmount,
            actualAmount = order.actualAmount,
            freightAmount = order.freightAmount,
            payType = order.payType,
            payTypeText = getPayTypeText(order.payType),
            payTime = order.payTime,
            consignee = order.consignee,
            phone = order.phone,
            address = order.address,
            memo = order.memo,
            deliveryTime = order.deliveryTime,
            receiveTime = order.receiveTime,
            cancelTime = order.cancelTime,
            cancelReason = order.cancelReason,
            deliveryCompany = order.deliveryCompany,
            deliveryNo = order.deliveryNo,
            sourceType = order.sourceType,
            sourceTypeText = getSourceTypeText(order.sourceType),
            createdTime = order.createdTime,
            updatedTime = order.updatedTime
        )
    }
    
    private fun getOrderStatusText(status: Int): String {
        return when (status) {
            1 -> "待付款"
            2 -> "已付款"
            3 -> "已发货"
            4 -> "已完成"
            5 -> "已取消"
            6 -> "已关闭"
            else -> "未知"
        }
    }
    
    private fun getPayStatusText(status: Int): String {
        return when (status) {
            0 -> "未支付"
            1 -> "支付成功"
            2 -> "支付失败"
            3 -> "已退款"
            4 -> "退款中"
            else -> "未知"
        }
    }
    
    private fun getPayTypeText(type: Int): String {
        return when (type) {
            0 -> "未选择"
            1 -> "微信"
            2 -> "支付宝"
            3 -> "银行卡"
            4 -> "余额"
            else -> "未知"
        }
    }
    
    private fun getSourceTypeText(type: Int): String {
        return when (type) {
            1 -> "APP"
            2 -> "PC"
            3 -> "小程序"
            4 -> "H5"
            else -> "未知"
        }
    }
}
