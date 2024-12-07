package com.blueland.todo.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timeInMillis: Long, format: String = "yyyy.MM.dd HH:mm:ss"): String {
    val date = Date(timeInMillis)
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(date)
}

fun getLocalizedGroupName(context: Context, groupNameKey: String): String {
    val resourceId = context.resources.getIdentifier(groupNameKey, "string", context.packageName)
    return if (resourceId != 0) {
        context.getString(resourceId)
    } else {
        groupNameKey // 리소스가 없을 경우 기본 키 반환
    }
}