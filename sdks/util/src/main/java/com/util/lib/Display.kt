package com.util.lib

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by weisl on 2019/10/15.
 */

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Float.dp(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    ).toInt()
}
fun Int.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

fun Int.sp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

fun Float.sp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )
}

fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}


fun getWidth(context: Context): Int {
    val dm = context.applicationContext.resources.displayMetrics
    return dm.widthPixels
}

fun getHeight(context: Context): Int {
    val dm = context.applicationContext.resources.displayMetrics
    return dm.heightPixels
}
