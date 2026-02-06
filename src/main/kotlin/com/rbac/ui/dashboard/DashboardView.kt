package com.rbac.ui.dashboard

import com.rbac.entity.Order
import com.rbac.service.DashboardService
import com.rbac.service.OrderService
import com.rbac.ui.MainLayout
import com.rbac.util.LocalDateUtil
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteAlias
import com.vaadin.flow.theme.lumo.LumoUtility
import java.text.DecimalFormat

@Route("", layout = MainLayout::class)
@RouteAlias("dashboard", layout = MainLayout::class)
@PageTitle("首页")
class DashboardView(
    private val dashboardService: DashboardService,
    private val orderService: OrderService,
) : VerticalLayout() {

    private val moneyFormat = DecimalFormat("#,##0.00")

    init {
        setSizeFull()
        isPadding = true
        isSpacing = true

        val data = dashboardService.getDashboardData()

        // 订单统计卡片容器
        add(H3("订单统计"))
        val orderStatsLayout = HorizontalLayout().apply {
            width = "100%"
            isSpacing = true
        }

        // 订单总数卡片
        orderStatsLayout.add(
            createStatCard(
                title = "订单总数",
                value = data.totalOrderCount.toString(),
                icon = VaadinIcon.INVOICE,
                colorTheme = "primary"
            )
        )

        // 今日订单卡片
        orderStatsLayout.add(
            createStatCard(
                title = "今日订单",
                value = data.todayOrderCount.toString(),
                icon = VaadinIcon.CALENDAR_CLOCK,
                colorTheme = "success"
            )
        )

        // 待付款订单卡片
        orderStatsLayout.add(
            createStatCard(
                title = "待付款",
                value = data.pendingPaymentCount.toString(),
                icon = VaadinIcon.CLOCK,
                colorTheme = "error"
            )
        )

        // 已完成订单卡片
        orderStatsLayout.add(
            createStatCard(
                title = "已完成",
                value = data.completedOrderCount.toString(),
                icon = VaadinIcon.CHECK_CIRCLE,
                colorTheme = "contrast"
            )
        )

        add(orderStatsLayout)

        // 销售额统计卡片容器
        add(H3("销售统计").apply {
            addClassNames(LumoUtility.Margin.Top.MEDIUM)
        })
        val salesStatsLayout = HorizontalLayout().apply {
            width = "100%"
            isSpacing = true
        }

        // 总销售额卡片
        salesStatsLayout.add(
            createStatCard(
                title = "总销售额",
                value = "¥${moneyFormat.format(data.totalSalesAmount)}",
                icon = VaadinIcon.DOLLAR,
                colorTheme = "primary"
            )
        )

        // 今日销售额卡片
        salesStatsLayout.add(
            createStatCard(
                title = "今日销售额",
                value = "¥${moneyFormat.format(data.todaySalesAmount)}",
                icon = VaadinIcon.MONEY,
                colorTheme = "success"
            )
        )

        // 总下单用户数卡片
        salesStatsLayout.add(
            createStatCard(
                title = "总下单用户数",
                value = data.totalOrderUserCount.toString(),
                icon = VaadinIcon.USERS,
                colorTheme = "contrast"
            )
        )

        // 今日下单用户数卡片
        salesStatsLayout.add(
            createStatCard(
                title = "今日下单用户数",
                value = data.todayOrderUserCount.toString(),
                icon = VaadinIcon.USER_CHECK,
                colorTheme = "error"
            )
        )

        add(salesStatsLayout)

        // 最近订单标题
        add(H3("最近订单").apply {
            addClassNames(LumoUtility.Margin.Top.MEDIUM)
        })

        // 订单表格
        val orderGrid = Grid(Order::class.java, false).apply {
            addColumn { it.orderNo }.setHeader("订单号").setAutoWidth(true)
            addColumn { it.userId }.setHeader("用户ID").setAutoWidth(true)
            addColumn { orderService.toDto(it).orderStatusText }.setHeader("订单状态").setAutoWidth(true)
            addColumn { orderService.toDto(it).payStatusText }.setHeader("支付状态").setAutoWidth(true)
            addColumn { "¥${moneyFormat.format(it.actualAmount)}" }.setHeader("实付金额").setAutoWidth(true)
            addColumn { orderService.toDto(it).payTypeText }.setHeader("支付方式").setAutoWidth(true)
            addColumn { orderService.toDto(it).sourceTypeText }.setHeader("订单来源").setAutoWidth(true)
            addColumn { order ->
                LocalDateUtil.formatDateTime(order.createdTime)
            }.setHeader("创建时间").setAutoWidth(true)

            // 获取最近10条订单
            val recentOrders = orderService.list().sortedByDescending { it.createdTime }.take(10)
            setItems(recentOrders)
        }
        add(orderGrid)
    }

    /**
     * 创建统计卡片
     */
    private fun createStatCard(
        title: String,
        value: String,
        icon: VaadinIcon,
        colorTheme: String
    ): Div {
        return Div().apply {
            // 使用 Lumo 工具类设置样式
            addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.LARGE
            )
            width = "25%"

            // 卡片内容容器
            val content = VerticalLayout().apply {
                isPadding = false
                isSpacing = true
            }

            // 图标和标题行
            val headerLayout = HorizontalLayout().apply {
                width = "100%"
                justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
                alignItems = FlexComponent.Alignment.CENTER

                // 标题
                add(Span(title).apply {
                    addClassNames(
                        LumoUtility.FontSize.SMALL,
                        LumoUtility.TextColor.SECONDARY
                    )
                })

                // 图标
                add(icon.create().apply {
                    addClassNames(LumoUtility.IconSize.MEDIUM)
                    when (colorTheme) {
                        "primary" -> element.style.set("color", "var(--lumo-primary-color)")
                        "success" -> element.style.set("color", "var(--lumo-success-color)")
                        "error" -> element.style.set("color", "var(--lumo-error-color)")
                        "contrast" -> element.style.set("color", "var(--lumo-contrast-60pct)")
                    }
                })
            }

            content.add(headerLayout)

            // 数值
            content.add(H2(value).apply {
                addClassNames(
                    LumoUtility.Margin.Top.SMALL,
                    LumoUtility.Margin.Bottom.NONE
                )
            })

            add(content)
        }
    }
}
