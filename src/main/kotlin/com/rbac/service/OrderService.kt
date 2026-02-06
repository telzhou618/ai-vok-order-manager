package com.rbac.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
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
}
