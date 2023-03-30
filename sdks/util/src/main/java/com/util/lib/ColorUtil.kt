package com.util.lib

import android.graphics.Color

/*
 *
 * Author: jinguang
 * Create: 2020/11/20 14:28
 * Description:
 */

object ColorUtil {
    /** 不透明度 0% --100%*/
    val alphaValue = arrayListOf(
        "00",
        "03",
        "05",
        "08",
        "0A",
        "0D",
        "0F",
        "12",
        "14",
        "17",
        "1A",
        "1C",
        "1F",
        "21",
        "24",
        "26",
        "29",
        "2B",
        "2E",
        "30",
        "33",
        "36",
        "38",
        "3B",
        "3D",
        "40",
        "42",
        "45",
        "47",
        "4A",
        "4D",
        "4F",
        "52",
        "54",
        "57",
        "59",
        "5C",
        "5E",
        "61",
        "63",
        "66",
        "69",
        "6B",
        "6E",
        "70",
        "73",
        "75",
        "78",
        "7A",
        "7D",
        "80",
        "82",
        "85",
        "87",
        "8A",
        "8C",
        "8F",
        "91",
        "94",
        "96",
        "99",
        "9C",
        "9E",
        "A1",
        "A3",
        "A6",
        "A8",
        "AB",
        "AD",
        "B0",
        "B3",
        "B5",
        "B8",
        "BA",
        "BD",
        "BF",
        "C2",
        "C4",
        "C7",
        "C9",
        "CC",
        "CF",
        "D1",
        "D4",
        "D6",
        "D9",
        "DB",
        "DE",
        "E0",
        "E3",
        "E6",
        "E8",
        "EB",
        "ED",
        "F0",
        "F2",
        "F5",
        "F7",
        "FA",
        "FC",
        "FF"
    )

    /**
     * alpha 不透明度 两位小数
     * color 六位16进制表示的颜色值 不含透明度
     */
    fun getColorByAlpha(alpha: Float, color: String): Int {
        if (alpha > 1f || color.length > 6 || alpha < 0) return Color.parseColor("#$color")
        val color = alphaValue[(alpha * 100).toInt()] + color
        return Color.parseColor("#$color")
    }
}