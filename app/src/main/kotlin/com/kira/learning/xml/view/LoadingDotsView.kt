package com.kira.learning.xml.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.kira.learning.R
import kotlin.math.sin

class LoadingDotsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 动画控制参数
    private var animationProgress = 0f
    private val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1200
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener { 
            animationProgress = it.animatedValue as Float
            invalidate() // 触发重绘
        }
    }

    // 绘图参数
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }
    private val dotRadius = 6f.dp // 扩展函数将dp转为px
    private val dotSpacing = 12f.dp

    // 自定义View尺寸计算
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = (dotRadius * 2 * 3 + dotSpacing * 2).toInt()
        val desiredHeight = (dotRadius * 2 * 3).toInt()
        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerY = height / 2f
        val startX = (width - (dotRadius * 2 * 3 + dotSpacing * 2)) / 2
        
        // 绘制三个圆点
        repeat(3) { index ->
            val phase = (animationProgress + index * 0.2f) % 1f
            val alpha = (sin(phase * Math.PI * 2) * 255).toInt().coerceIn(0, 255)
            dotPaint.alpha = alpha
            
            val cx = startX + dotRadius + index * (dotRadius * 2 + dotSpacing)
            canvas.drawCircle(cx, centerY, dotRadius, dotPaint)
        }
    }

    // 生命周期控制
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

    // 扩展函数：dp转px
    private val Float.dp: Float
        get() = this * resources.displayMetrics.density + 0.5f
}