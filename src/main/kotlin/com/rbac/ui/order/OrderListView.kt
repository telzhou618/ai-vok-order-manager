package com.rbac.ui.order

import com.github.mvysny.karibudsl.v10.*
import com.rbac.dto.OrderDto
import com.rbac.dto.OrderQueryDto
import com.rbac.service.OrderService
import com.rbac.ui.MainLayout
import com.rbac.util.NotifyUtil
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.format.DateTimeFormatter

@Route("orders", layout = MainLayout::class)
@PageTitle("订单管理")
class OrderListView(
    private val orderService: OrderService
) : VerticalLayout() {

    private lateinit var orderNoField: TextField
    private lateinit var userIdField: TextField
    private lateinit var grid: Grid<OrderDto>
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        setSizeFull()
        isPadding = true
        h4("订单管理")
        createToolbar()
        createGrid()
        
        // 默认显示空列表
        grid.setItems(emptyList())
    }

    private fun createToolbar() {
        horizontalLayout {
            width = "100%"
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
            alignItems = FlexComponent.Alignment.END

            horizontalLayout {
                alignItems = FlexComponent.Alignment.END
                
                orderNoField = textField("订单号") {
                    placeholder = "输入订单号查询"
                    width = "200px"
                }
                
                userIdField = textField("用户ID") {
                    placeholder = "输入用户ID查询"
                    width = "150px"
                }
                
                button("查询") {
                    icon = VaadinIcon.SEARCH.create()
                    addThemeVariants(ButtonVariant.LUMO_PRIMARY)
                    onLeftClick { loadData() }
                }
                
                button("重置") {
                    icon = VaadinIcon.REFRESH.create()
                    onLeftClick { 
                        orderNoField.clear()
                        userIdField.clear()
                        grid.setItems(emptyList())
                    }
                }
            }
        }
    }

    private fun createGrid() {
        grid = Grid(OrderDto::class.java, false).apply {
            addColumn { it.id }.setHeader("ID").width = "80px"
            addColumn { it.orderNo }.setHeader("订单号").width = "180px"
            addColumn { it.userId }.setHeader("用户ID").width = "100px"
            
            // 订单状态列 - 带颜色高亮
            addComponentColumn { order ->
                val badge = span(order.orderStatusText) {
                    element.style.set("padding", "4px 8px")
                    element.style.set("border-radius", "4px")
                    element.style.set("font-size", "12px")
                    element.style.set("font-weight", "500")
                    
                    when (order.orderStatus) {
                        1 -> { // 待付款
                            element.style.set("background-color", "#fff3e0")
                            element.style.set("color", "#e65100")
                        }
                        2 -> { // 已付款
                            element.style.set("background-color", "#e3f2fd")
                            element.style.set("color", "#1565c0")
                        }
                        3 -> { // 已发货
                            element.style.set("background-color", "#f3e5f5")
                            element.style.set("color", "#6a1b9a")
                        }
                        4 -> { // 已完成
                            element.style.set("background-color", "#e7f5e9")
                            element.style.set("color", "#2e7d32")
                        }
                        5, 6 -> { // 已取消/已关闭
                            element.style.set("background-color", "#fdecea")
                            element.style.set("color", "#d32f2f")
                        }
                    }
                }
                badge
            }.setHeader("订单状态").width = "120px"
            
            // 支付状态列
            addComponentColumn { order ->
                val badge = span(order.payStatusText) {
                    element.style.set("padding", "4px 8px")
                    element.style.set("border-radius", "4px")
                    element.style.set("font-size", "12px")
                    element.style.set("font-weight", "500")
                    
                    when (order.payStatus) {
                        0 -> { // 未支付
                            element.style.set("background-color", "#f5f5f5")
                            element.style.set("color", "#616161")
                        }
                        1 -> { // 支付成功
                            element.style.set("background-color", "#e7f5e9")
                            element.style.set("color", "#2e7d32")
                        }
                        2 -> { // 支付失败
                            element.style.set("background-color", "#fdecea")
                            element.style.set("color", "#d32f2f")
                        }
                        3, 4 -> { // 已退款/退款中
                            element.style.set("background-color", "#fff3e0")
                            element.style.set("color", "#e65100")
                        }
                    }
                }
                badge
            }.setHeader("支付状态").width = "120px"
            
            addColumn { "¥${it.actualAmount}" }.setHeader("实付金额").width = "120px"
            addColumn { it.payTypeText }.setHeader("支付方式").width = "100px"
            addColumn { it.consignee }.setHeader("收货人").width = "120px"
            addColumn { it.phone }.setHeader("联系电话").width = "130px"
            addColumn { it.sourceTypeText }.setHeader("订单来源").width = "100px"
            addColumn { it.createdTime?.format(dateFormatter) ?: "" }.setHeader("创建时间").width = "180px"
            
            addComponentColumn { order ->
                horizontalLayout {
                    button("详情") {
                        addThemeVariants(ButtonVariant.LUMO_SMALL)
                        icon = VaadinIcon.EYE.create()
                        onLeftClick { showOrderDetail(order) }
                    }
                }
            }.setHeader("操作").width = "100px"

            setSizeFull()
        }
        add(grid)
    }

    private fun loadData() {
        val orderNo = orderNoField.value?.trim()
        val userIdStr = userIdField.value?.trim()
        
        // 验证：必须输入订单号或用户ID
        if (orderNo.isNullOrBlank() && userIdStr.isNullOrBlank()) {
            NotifyUtil.showWarning("请输入订单号或用户ID进行查询")
            return
        }
        
        // 验证用户ID格式
        val userId = if (!userIdStr.isNullOrBlank()) {
            try {
                userIdStr.toLong()
            } catch (e: NumberFormatException) {
                NotifyUtil.showError("用户ID必须是数字")
                return
            }
        } else null
        
        try {
            val query = OrderQueryDto(
                orderNo = orderNo?.takeIf { it.isNotBlank() },
                userId = userId
            )
            
            val orders = orderService.queryOrders(query)
            val orderDtos = orders.map { orderService.toDto(it) }
            
            grid.setItems(orderDtos)
            
            if (orderDtos.isEmpty()) {
                NotifyUtil.showInfo("未查询到订单数据")
            } else {
                NotifyUtil.showSuccess("查询成功，共 ${orderDtos.size} 条记录")
            }
        } catch (e: Exception) {
            NotifyUtil.showError("查询失败：${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun showOrderDetail(order: OrderDto) {
        OrderDetailDialog(order).open()
    }
}
