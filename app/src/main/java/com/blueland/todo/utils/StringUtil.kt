package com.blueland.todo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timeInMillis: Long, format: String = "yyyy.MM.dd HH:mm:ss"): String {
    val date = Date(timeInMillis)
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(date)
}
