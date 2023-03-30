package com.util.lib

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Created by weishl on 2020/11/6
 *
 */
object ShapeHelper {

    /**
     * @param radius 数组长度为4
     */
    @JvmStatic
    fun generatorShape(
        context: Context,
        @ColorRes solidColor: Int,
        @ColorRes strokeColor: Int,
        strokeWidth: Int,
        radius: FloatArray
    ): Drawable {
        val radiusBg = GradientDrawable()
        radiusBg.shape = GradientDrawable.RECTANGLE
        radiusBg.setColor(ContextCompat.getColor(context, solidColor))
        if (strokeWidth > 0 && strokeColor != 0) {
            radiusBg.setStroke(strokeWidth, ContextCompat.getColor(context, strokeColor))
        }
        //每连续的两个数值表示是一个角度,四组:左上,右上,右下,左下
        if (radius.size == 4) {
            radiusBg.cornerRadii = floatArrayOf(
                radius[0],
                radius[0],
                radius[1],
                radius[1],
                radius[2],
                radius[2],
                radius[3],
                radius[3]
            )
        }
        return radiusBg
    }
    @JvmStatic
    fun generatorShape(
        context: Context,
        @ColorRes solidColor: Int,
        @ColorRes strokeColor: Int,
        strokeWidth: Int,
        radius: Float
    ): Drawable {
        val radiusBg = GradientDrawable()
        radiusBg.shape = GradientDrawable.RECTANGLE
        radiusBg.setColor(ContextCompat.getColor(context, solidColor))
        if (strokeWidth > 0 && strokeColor != 0) {
            radiusBg.setStroke(strokeWidth, ContextCompat.getColor(context, strokeColor))
        }
        //圆角
        if (radius > 0) {
            radiusBg.cornerRadius = radius
        }
        return radiusBg
    }
}