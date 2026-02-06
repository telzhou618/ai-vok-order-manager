package com.rbac.mapper

import com.baomidou.dynamic.datasource.annotation.DS
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.rbac.entity.Order
import org.apache.ibatis.annotations.Mapper

/**
 * 订单 Mapper
 * 使用 @DS("sharding") 注解指定使用分表数据源
 */
@Mapper
@DS("sharding")
interface OrderMapper : BaseMapper<Order>
