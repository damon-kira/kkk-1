package com.colombia.credit.expand

/**
 * 现金数值添加分隔符
 * 每三个位数加一个"，"
 *
 * @param symbol 间隔符
 * @param splitDigits 几位分隔
 */
fun formatCommon(cash: String, splitDigits: Int = 3, symbol: String = ","): String {
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