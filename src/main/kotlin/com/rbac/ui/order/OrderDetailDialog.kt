package com.rbac.ui.order

import com.github.mvysny.karibudsl.v10.*
import com.rbac.dto.OrderDto
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.FlexComponent
import java.time.format.DateTimeFormatter

class OrderDetailDialog(
    private val order: OrderDto
) : Dialog() {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        width = "800px"
        isCloseOnEsc = true
        isCloseOnOutsideClick = false

        verticalLayout {
            isPadding = false
            
            h3("订单详情")
            
            // 基本信息
            formLayout {
                responsiveSteps = listOf(
                    FormLayout.ResponsiveStep("0", 2)
                )
                
                textField("订单号") {
                    value = order.orderNo
                    isReadOnly = true
                    colspan = 2
                }
                
                textField("订单ID") {
                    value = order.id?.toString() ?: ""
                    isReadOnly = true
                }
                
                textField("用户ID") {
                    value = order.userId.toString()
                    isReadOnly = true
                }
                
                textField("订单状态") {
                    value = order.orderStatusText
                    isReadOnly = true
                }
                
                textField("支付状态") {
                    value = order.payStatusText
                    isReadOnly = true
                }
            }
            
            hr()
            
            // 金额信息
            h4("金额信息")
            formLayout {
                responsiveSteps = listOf(
                    FormLayout.ResponsiveStep("0", 2)
                )
                
                textField("订单总金额") {
                    value = "¥${order.totalAmount}"
                    isReadOnly = true
                }
                
                textField("优惠金额") {
                    value = "¥${order.discountAmount}"
                    isReadOnly = true
                }
                
                textField("运费") {
                    value = "¥${order.freightAmount}"
                    isReadOnly = true
                }
                
                textField("实付金额") {
                    value = "¥${order.actualAmount}"
                    isReadOnly = true
                    element.style.set("font-weight", "bold")
                    element.style.set("color", "#d32f2f")
                }
                
                textField("支付方式") {
                    value = order.payTypeText
                    isReadOnly = true
                }
                
                textField("支付时间") {
                    value = order.payTime?.format(dateFormatter) ?: "未支付"
                    isReadOnly = true
                }
            }
            
            hr()
            
            // 收货信息
            h4("收货信息")
            formLayout {
                responsiveSteps = listOf(
                    FormLayout.ResponsiveStep("0", 2)
                )
                
                textField("收货人") {
                    value = order.consignee
                    isReadOnly = true
                }
                
                textField("联系电话") {
                    value = order.phone
                    isReadOnly = true
                }
                
                textField("收货地址") {
                    value = order.address
                    isReadOnly = true
                    colspan = 2
                }
            }
            
            // 物流信息（如果有）
            if (order.deliveryCompany.isNotBlank() || order.deliveryNo.isNotBlank()) {
                hr()
                h4("物流信息")
                formLayout {
                    responsiveSteps = listOf(
                        FormLayout.ResponsiveStep("0", 2)
                    )
                    
                    textField("物流公司") {
                        value = order.deliveryCompany
                        isReadOnly = true
                    }
                    
                    textField("物流单号") {
                        value = order.deliveryNo
                        isReadOnly = true
                    }
                    
                    textField("发货时间") {
                        value = order.deliveryTime?.format(dateFormatter) ?: ""
                        isReadOnly = true
                    }
                    
                    textField("收货时间") {
                        value = order.receiveTime?.format(dateFormatter) ?: ""
                        isReadOnly = true
                    }
                }
            }
            
            // 其他信息
            hr()
            h4("其他信息")
            formLayout {
                responsiveSteps = listOf(
                    FormLayout.ResponsiveStep("0", 2)
                )
                
                textField("订单来源") {
                    value = order.sourceTypeText
                    isReadOnly = true
                }
                
                textField("创建时间") {
                    value = order.createdTime?.format(dateFormatter) ?: ""
                    isReadOnly = true
                }
                
                if (order.memo.isNotBlank()) {
                    textArea("订单备注") {
                        value = order.memo
                        isReadOnly = true
                        colspan = 2
                    }
                }
                
                // 取消信息（如果有）
                if (order.orderStatus == 5 && order.cancelReason.isNotBlank()) {
                    textField("取消时间") {
                        value = order.cancelTime?.format(dateFormatter) ?: ""
                        isReadOnly = true
                    }
                    
                    textArea("取消原因") {
                        value = order.cancelReason
                        isReadOnly = true
                        colspan = 2
                    }
                }
            }
            
            hr()
            
            // 底部按钮
            horizontalLayout {
                width = "100%"
                justifyContentMode = FlexComponent.JustifyContentMode.END
                
                button("关闭") {
                    addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                    onLeftClick { close() }
                }
            }
        }
    }
}
