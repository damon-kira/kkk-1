package com.kira.learning.xml.expand

import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun RandomCID(tempStartTimeStamp: Long): Long {
    val randomNumber = (Math.random() * 1000000L).toLong()
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    val date = Date(tempStartTimeStamp) // 将long时间戳转换为Date对象
    val formattedDateString = formatter.format(date) // 将Date对象格式化为字符串
    return formattedDateString.toLong() * 1000000L + randomNumber
}