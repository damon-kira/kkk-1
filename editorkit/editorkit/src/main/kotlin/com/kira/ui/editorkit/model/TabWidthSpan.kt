

package com.kira.ui.editorkit.model

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

class TabWidthSpan(private val width: Int) : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?,
    ): Int = paint.measureText(" ".repeat(width)).toInt()

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint,
    ) = Unit
}