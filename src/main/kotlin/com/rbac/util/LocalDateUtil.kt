package com.rbac.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateUtil {

    private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun formatDateTime(dateTime: LocalDateTime?): String {
        return dateTime?.format(DATE_TIME_FORMATTER) ?: ""
    }

    fun formatDate(date: LocalDate?): String {
        return date?.format(DATE_FORMATTER) ?: ""
    }
}
