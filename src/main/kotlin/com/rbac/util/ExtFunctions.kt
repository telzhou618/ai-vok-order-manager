package com.rbac.util

import com.rbac.config.DateFormatConfig.Companion.DATE_FORMATTER
import com.rbac.config.DateFormatConfig.Companion.DATE_TIME_FORMATTER
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import java.time.LocalDate
import java.time.LocalDateTime

// 扩展函数，可以直接在任何地方调用
fun showError(message: String) {
    val notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER)
    notification.addThemeVariants(NotificationVariant.LUMO_ERROR)
}

fun showSuccess(message: String) {
    val notification = Notification.show(message, 2000, Notification.Position.TOP_CENTER)
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS)
}

fun showWarning(message: String) {
    val notification = Notification.show(message, 2500, Notification.Position.TOP_CENTER)
    notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST)
}

fun showInfo(message: String) {
    val notification = Notification.show(message, 2000, Notification.Position.TOP_CENTER)
    notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY)
}

fun formatDateTime(dateTime: LocalDateTime?): String {
    return dateTime?.format(DATE_TIME_FORMATTER) ?: ""
}

fun formatDate(date: LocalDate?): String {
    return date?.format(DATE_FORMATTER) ?: ""
}