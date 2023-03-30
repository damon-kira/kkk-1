package com.colombia.credit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.dp
import com.util.lib.sp
import kotlin.math.max
import kotlin.math.min

class ProcessView : View {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }


    private val mProcessWidth = 24f.dp()
    private val mProcessHeight = 6f.dp()
    private val mProcessSpace = 10f.dp()
    private val mStepCount = 5

    private val mCurrColor = Color.WHITE
    private val mNormalColor = Color.parseColor("#4dffffff")

    private val mPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
            it.strokeCap = Paint.Cap.ROUND
            it.color = mCurrColor
            it.strokeWidth = mProcessHeight.toFloat()
        }
    }

    private val mTextBgSpace = 6.dp()
    private val mTextPadding = 4.dp()
    private val mTextSize = 14.sp()
    private val mTextPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
            it.color = ContextCompat.getColor(context, R.color.colorPrimary)
            it.textSize = mTextSize
            it.textAlign = Paint.Align.CENTER
        }
    }

    private val mTextBg = ContextCompat.getDrawable(context, R.drawable.ic_process_bg)

    private var mCurrStep = 1

    private var mWidth = 0
    private var mHeight = 0

    private val TAG = "debug_ProcessView"

    private fun init(
        context: Context?,
        attrs: AttributeSet?
    ) {
        context?.obtainStyledAttributes(attrs, R.styleable.ProcessView)?.let {
            val indexCount = it.indexCount
            for (index in 0 until indexCount) {
                val attr = it.getIndex(index)
                if (attr == R.styleable.ProcessView_pv_curr_step) {
                    mCurrStep = min(it.getInteger(attr, 1), mStepCount)
                }
            }
            it.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val processWidth = (mProcessWidth + mProcessSpace) * mStepCount - mProcessSpace
        val drawableWidth = (mTextBg?.intrinsicWidth ?: 0) * mStepCount
        val finalHeight = (mTextBg?.intrinsicHeight ?: 0) + mTextBgSpace + mProcessHeight
        setMeasuredDimension(max(processWidth, drawableWidth), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        Log.d(TAG, "onDraw: ===========mCurrStep = $mCurrStep")
        val halfHeight = mHeight / 2f

        canvas.save()
        val bgWidth = mTextBg?.intrinsicWidth ?: 0
        val moveX = if (bgWidth > mProcessWidth) (bgWidth - mProcessWidth) / 2f else 0f
        drawText(canvas, moveX)
        val moveY = mHeight - mProcessHeight * 1f
        Log.d(TAG, "onDraw: moveY = $moveY")
        canvas.translate(moveX, moveY / 2)
        for (index in 1..mStepCount) {
            if (index <= mCurrStep) {
                mPaint.color = mCurrColor
            } else {
                mPaint.color = mNormalColor
            }
            canvas.drawLine(0f, halfHeight, mProcessWidth * 1f, halfHeight, mPaint)
            if (index < mStepCount) {
                canvas.translate((mProcessWidth + mProcessSpace) * 1f, 0f)
            }
        }
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, normalMoveX: Float) {
        canvas.save()
        val bgWidth = mTextBg?.intrinsicWidth ?: 0
        val bgHeight = mTextBg?.intrinsicHeight ?: 0
        val moveX = (mProcessWidth + mProcessSpace) * mCurrStep - mProcessSpace * 1f - mProcessWidth
        canvas.translate(moveX, 0f)
        mTextBg?.setBounds(0, 0, bgWidth, bgHeight)
        mTextBg?.draw(canvas)
        val text = "${(mCurrStep * 20)}%"
        canvas.drawText(text, bgWidth / 2f, bgHeight - 8.dp(), mTextPaint)
        canvas.restore()
    }

    fun setCurrStep(step: Int) {
        this.mCurrStep = step
    }
}