package com.colombia.credit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.dp

class ReviewProcessView : View {

    private val mPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.color = ContextCompat.getColor(context, R.color.colorPrimary)
            it.style = Paint.Style.FILL
            it.strokeWidth = 8.dp()
            it.strokeCap = Paint.Cap.ROUND
        }
    }


    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private val mCircleRadius = 10.dp()
    private val mOutCircleColor = Color.parseColor("#F6EBFE")
    private val mInnerColor = ContextCompat.getColor(context, R.color.colorPrimary)

    private val mLastOutColor = Color.parseColor("#EBEBEB")
    private val mLastInnerColor = Color.parseColor("#999999")

    private val halfWidth
        get() = mWidth / 2f
    private val halfHeight
        get() = mHeight / 2f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(width, (mCircleRadius * 2).toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w - (mCircleRadius * 2).toInt()
        mHeight = h
    }


    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawLine(mCircleRadius, halfHeight, halfWidth, halfHeight, mPaint)
        mPaint.color = mOutCircleColor
        canvas.drawCircle(mCircleRadius, halfHeight, mCircleRadius, mPaint)
        mPaint.color = mInnerColor
        canvas.drawCircle(mCircleRadius, halfHeight, mCircleRadius / 2f, mPaint)

        canvas.save()
        mPaint.color = mLastInnerColor
        canvas.translate(halfWidth + mCircleRadius, 0f)
        canvas.drawLine(mCircleRadius, halfHeight, halfWidth, halfHeight, mPaint)
        mPaint.color = mOutCircleColor
        canvas.drawCircle(0f, halfHeight, mCircleRadius, mPaint)
        mPaint.color = mInnerColor
        canvas.drawCircle(0f, halfHeight, mCircleRadius / 2, mPaint)

        canvas.translate(halfWidth, 0f)
        mPaint.color = mLastOutColor
        canvas.drawCircle(0f, halfHeight, mCircleRadius, mPaint)
        mPaint.color = mLastInnerColor
        canvas.drawCircle(0f, halfHeight, mCircleRadius / 2f, mPaint)
        canvas.restore()


    }
}