package com.rbac.ui.order

import com.github.mvysny.karibudsl.v10.*
import com.rbac.service.OrderService
import com.rbac.ui.MainLayout
import com.rbac.util.NotifyUtil
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import java.time.format.DateTimeFormatter

@Route("order-detail", layout = MainLayout::class)
@PageTitle("订单详情")
class OrderDetailView(
    private val orderService: OrderService
) : VerticalLayout(), BeforeEnterObserver {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        setSizeFull()
        isPadding = true
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        // 从 URL 参数中获取订单ID和用户ID
        val orderIdStr = event.location.queryParameters.parameters["orderId"]?.firstOrNull()
        val userIdStr = event.location.queryParameters.parameters["userId"]?.firstOrNull()
        
        if (orderIdStr == null || userIdStr == null) {
            NotifyUtil.showError("订单ID或用户ID不能为空")
            event.rerouteTo(OrderListView::class.java)
            return
        }

        try {
            val orderId = orderIdStr.toLong()
            val userId = userIdStr.toLong()
            loadOrderDetail(orderId, userId)
        } catch (e: NumberFormatException) {
            NotifyUtil.showError("订单ID或用户ID格式错误")
            event.rerouteTo(OrderListView::class.java)
        }
    }

    private fun loadOrderDetail(orderId: Long, userId: Long) {
        try {
            // 使用用户ID查询订单列表，然后找到对应的订单
            // 这样可以确保查询路由到正确的分表
            val orders = orderService.listByUserId(userId)
            val order = orders.find { it.id == orderId }
            
            if (order == null) {
                NotifyUtil.showError("订单不存在")
                ui.ifPresent { it.navigate(OrderListView::class.java) }
                return
            }

            val orderDto = orderService.toDto(order)
            renderOrderDetail(orderDto)
            
        } catch (e: Exception) {
            NotifyUtil.showError("加载订单详情失败：${e.message}")
            e.printStackTrace()
            ui.ifPresent { it.navigate(OrderListView::class.java) }
        }
    }

    private fun renderOrderDetail(order: com.rbac.dto.OrderDto) {
        removeAll()
        
        // 标题和返回按钮
        horizontalLayout {
            width = "100%"
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
            alignItems = FlexComponent.Alignment.CENTER
            
            h3("订单详情")
            
            button("返回列表") {
                icon = VaadinIcon.ARROW_LEFT.create()
                addThemeVariants(ButtonVariant.LUMO_TERTIARY)
                onLeftClick {
                    ui.ifPresent { it.navigate(OrderListView::class.java) }
                }
            }
        }
        
        hr()
        
        // 基本信息
        h4("基本信息")
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
    }
}
