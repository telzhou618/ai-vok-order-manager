package com.rbac.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.annotation.Version
import java.math.BigDecimal
import java.time.LocalDateTime

@TableName("`order`")
data class Order(
    @TableId(type = IdType.AUTO)
    var id: Long? = null,
    
    var orderNo: String = "",
    
    var userId: Long = 0,
    
    var orderStatus: Int = 1,
    
    var payStatus: Int = 0,
    
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    
    var discountAmount: BigDecimal = BigDecimal.ZERO,
    
    var actualAmount: BigDecimal = BigDecimal.ZERO,
    
    var freightAmount: BigDecimal = BigDecimal.ZERO,
    
    var payType: Int = 0,
    
    var payTime: LocalDateTime? = null,
    
    var consignee: String = "",
    
    var phone: String = "",
    
    var address: String = "",
    
    var memo: String = "",
    
    var deliveryTime: LocalDateTime? = null,
    
    var receiveTime: LocalDateTime? = null,
    
    var cancelTime: LocalDateTime? = null,
    
    var cancelReason: String = "",
    
    var deliveryCompany: String = "",
    
    var deliveryNo: String = "",
    
    var invoiceType: Int = 0,
    
    var invoiceTitle: String = "",
    
    var invoiceTaxNo: String = "",
    
    var sourceType: Int = 1,
    
    var isDeleted: Int = 0,
    
    @Version
    var version: Int = 0,
    
    var createdTime: LocalDateTime? = null,
    
    var updatedTime: LocalDateTime? = null
)
