package com.colombia.credit.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.dp
import com.util.lib.sp

class UploadView : View {

    private val STROKE_WIDTH = 4.dp()

    private val mPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.style = Paint.Style.STROKE
            it.strokeWidth = STROKE_WIDTH
            it.color = NORMAL_COLOR
            it.strokeCap = Paint.Cap.ROUND
        }
    }

    private val mTextPaint by lazy {
        TextPaint().also {
            it.isAntiAlias = true
            it.color = ContextCompat.getColor(context, R.color.color_333333)
            it.strokeCap = Paint.Cap.ROUND
            it.textSize = 24.sp()
            it.typeface = Typeface.DEFAULT_BOLD
            it.textAlign = Paint.Align.CENTER
        }
    }

    private val mRectSize = RectF()

    private val RADIUS = 55.dp()

    private var mText = "25%"

    private val NORMAL_COLOR by lazy { ContextCompat.getColor(context, R.color.color_ECD1FA) }
    private val PROCESS_COLOR by lazy { ContextCompat.getColor(context, R.color.colorPrimary) }

    private val mSweepAngle = 90f

    private var mCurrProcess = 1
    private val MAX_PROCESS = 100

    private val rect = Rect()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = (RADIUS * 2).toInt()
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val halfStroke = STROKE_WIDTH / 2
        mRectSize.left = paddingLeft * 1f + halfStroke
        mRectSize.top = paddingTop * 1f + halfStroke
        mRectSize.right = w - paddingRight * 1f - halfStroke
        mRectSize.bottom = h - paddingBottom * 1f - halfStroke
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val centerX = width / 2f
        val centerY = height / 2f

        mPaint.color = NORMAL_COLOR
        canvas.drawCircle(centerX, centerY, RADIUS - STROKE_WIDTH / 2, mPaint)

        mPaint.color = PROCESS_COLOR
        canvas.drawArc(mRectSize, -90f, mCurrProcess * 360 / MAX_PROCESS * 1f, false, mPaint)
        // 绘制文本
        mText = "$mCurrProcess%"
        mTextPaint.getTextBounds(mText, 0, mText.length, rect)
        canvas.drawText(mText, centerX, centerY + rect.height() / 2, mTextPaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mAnim.isStarted || mAnim.isRunning) {
            mAnim.cancel()
        }
    }

    private val mAnim by lazy {
        ValueAnimator.ofInt(0, MAX_PROCESS).apply {
            duration = 10000
            addUpdateListener {
                val value = it.animatedValue as Int
                mCurrProcess = value
                postInvalidate()
                if (value == 95) {
                    pause()
                }
            }
            interpolator = DecelerateInterpolator()
        }

    }

    fun start() {
        mAnim.start()
    }

    fun cancel() {
        mAnim.cancel()
    }

    fun end(){
        cancel()
        mCurrProcess = MAX_PROCESS
    }

    fun animPause() {
        mAnim.pause()
    }

    fun animResume() {
        mAnim.resume()
    }
}