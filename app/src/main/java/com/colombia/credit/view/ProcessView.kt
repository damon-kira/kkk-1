package com.colombia.credit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
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
    private val mStepCount = 4

    private val mCurrColor = Color.WHITE
    private val mNormalColor = Color.parseColor("#4dffffff")

    private val mPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
            it.color = mCurrColor
            it.strokeWidth = mProcessHeight.toFloat()
        }
    }

    private val mRectLine by lazy {
        RectF(0f, 0f, mProcessWidth * 1f, mProcessHeight * 1f)
    }

    private val mTextBgSpace = 6.dp()
    private val mTextPadding = 4.dp()
    private val mTextSize = 13.sp()
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

    private var mLineMoveX = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val bgWidth = mTextBg?.intrinsicWidth ?: 0
        val processWidth = (mProcessWidth + mProcessSpace) * mStepCount - mProcessSpace
        val finalHeight = (mTextBg?.intrinsicHeight ?: 0) + mTextBgSpace + mProcessHeight
        mLineMoveX = if (bgWidth > mProcessWidth) (bgWidth - mProcessWidth) * 0.5f else 0f
        setMeasuredDimension((processWidth + mLineMoveX * 2).toInt(), finalHeight.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val processWidth = mProcessWidth * 1f
        drawText(canvas, mLineMoveX)
        val moveY = mHeight - mProcessHeight * 1f
        canvas.save()
        canvas.translate(mLineMoveX, moveY)
        val rectRadius = mProcessHeight / 0.75f
        mPaint.color = mCurrColor
        for (index in 1..mStepCount) {
            if (index > mCurrStep) {
                mPaint.color = mNormalColor
            }
            canvas.drawRoundRect(mRectLine, rectRadius, rectRadius, mPaint)
            if (index < mStepCount) {
                canvas.translate(processWidth + mProcessSpace, 0f)
            }
        }
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, normalMoveX: Float) {
        canvas.save()
        val bgWidth = mTextBg?.intrinsicWidth ?: 0
        val bgHeight = mTextBg?.intrinsicHeight ?: 0
        val moveX = ((mProcessWidth + mProcessSpace) * mCurrStep - mProcessSpace * 1f - mProcessWidth).coerceAtLeast(0f)
        canvas.translate(moveX, 0f)
        mTextBg?.setBounds(0, 0, bgWidth, bgHeight)
        mTextBg?.draw(canvas)
        val text = "${(mCurrStep * 25)}%"
        val bound = Rect()
        mTextPaint.getTextBounds(text, 0, text.length, bound)
        canvas.drawText(text, bgWidth / 2f, bgHeight - bound.height() + 4.dp(), mTextPaint)
        canvas.restore()
    }

    fun setCurrStep(step: Int) {
        this.mCurrStep = step
    }
}