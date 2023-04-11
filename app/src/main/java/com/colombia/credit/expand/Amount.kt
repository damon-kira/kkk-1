package com.colombia.credit.expand

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.app.getAppContext
import com.util.lib.PackageUtil.getLaunchIntentByPackageName

/**
 * 现金数值添加分隔符
 * 每三个位数加一个"，"
 *
 * @param symbol 间隔符
 * @param splitDigits 几位分隔
 */
fun formatCommon(cash: String, splitDigits: Int = 3, symbol: String = "."): String {
    val reverdCash = cash.reversed()
    val charArray = reverdCash.toCharArray()
    val builder = StringBuilder()
    val length = charArray.size
    val point_index = reverdCash.indexOf(".")

    for (index in 0 until length) {
        builder.append(charArray[index])
        if (index > point_index) {
            if ((index - point_index) % splitDigits == 0 && (index + 1) != length) {
                builder.append(symbol)
            }
        }
    }

    return builder.toString().reversed()
}

/**
 * 脱敏指定范围内的字符串
 */
fun maskString(str: String?, preCount: Int, postCount: Int): String {
    if (str == null || str.isEmpty()) {
        return ""
    }
    if (str.length <= preCount + postCount || preCount < 0) {
        return str
    }
    val array = str.toCharArray()
    array.fill('*', preCount, array.size - postCount)
    return array.joinToString("")
}

/**
 * @param amount 不需要添加分隔符
 */
fun Context.getUnitString(amount: String) = getString(R.string.amount_unit, formatCommon(amount))

fun getUnitString(amount: String) = getAppContext().getString(R.string.amount_unit, formatCommon(amount))