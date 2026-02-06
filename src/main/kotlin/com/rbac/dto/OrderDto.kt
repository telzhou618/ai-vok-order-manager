package com.rbac.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderDto(
    var id: Long? = null,
    var orderNo: String = "",
    var userId: Long = 0,
    var orderStatus: Int = 1,
    var orderStatusText: String = "",
    var payStatus: Int = 0,
    var payStatusText: String = "",
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    var discountAmount: BigDecimal = BigDecimal.ZERO,
    var actualAmount: BigDecimal = BigDecimal.ZERO,
    var freightAmount: BigDecimal = BigDecimal.ZERO,
    var payType: Int = 0,
    var payTypeText: String = "",
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
    var sourceType: Int = 1,
    var sourceTypeText: String = "",
    var createdTime: LocalDateTime? = null,
    var updatedTime: LocalDateTime? = null
)

data class OrderQueryDto(
    var orderNo: String? = null,
    var userId: Long? = null
)
