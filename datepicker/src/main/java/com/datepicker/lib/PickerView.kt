package com.datepicker.lib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.manu.mdatepicker.R
import java.lang.Math.pow
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs
import kotlin.math.pow

open class PickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private val SPEED = 5f
    }

    //行距与mTextSizeNormal之比，保证View内显示的内容在适当的位置
    private val RATE = 3f

    private val mPaintNormal: Paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.textAlign = Paint.Align.CENTER
            it.textSize = mTextSizeNormal
            it.color = 0xa2a2a2
        }
    }
    private val mPaintSelect: Paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.textAlign = Paint.Align.CENTER
            it.textSize = mTextSizeSelect
            it.color = 0xffa000
        }
    }
    private val mPaintText: Paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.textSize = Util.dpToPx(context, 15f).toFloat()
            it.color = ContextCompat.getColor(context, R.color.colorPickViewDefaultSelect)
        }
    }
    private val mPaintLine: Paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.color = ContextCompat.getColor(context, R.color.colorDivider)
            it.strokeWidth = Util.dpToPx(context, 0.5f).toFloat()
        }
    }

    private var mTextSizeNormal = 0f
    private var mTextSizeSelect = 0f

    @ColorInt
    private var mNormalColor = 0

    @ColorInt
    private var mSelectColor = 0
    private var mCurrentSelectColor = 0
    private var mCurrentNormalColor = 0

    private var mTextAlphaSelect = 0f
    private var mTextAlphaNormal = 0f

    private var mPaddingStart = 0f
    private var mPaddingEnd = 0f

    // 选中的位置
    private var mSelectPosition = 0

    // 开始触摸的位置
    private var mStartTouchY = 0f

    // 手指滑动的距离
    private var mMoveDistance = 0f
    private var mWidth = 0
    private var mHeight = 0
    private var mText: String? = null

    private val mTimer: Timer by lazy {
        Timer()
    }
    private val mHandler by lazy(LazyThreadSafetyMode.NONE) {
        MHandler(this)
    }
    private var mTask: MTimerTask? = null

    private val mData: ArrayList<String> by lazy {
        ArrayList()
    }
    private var mOnSelectListener: OnSelectListener? = null

    private val TAG = "debug_PickerView"

    init {
        mTask = MTimerTask(mHandler)
        mTextAlphaSelect = 255f
        mTextAlphaNormal = 120f
    }

    fun setNormalColor(@ColorInt normalColor: Int) {
        mNormalColor = normalColor
        if (mNormalColor != mCurrentNormalColor) {
            mCurrentNormalColor = mNormalColor
            mPaintNormal.color = if (mNormalColor != 0) mNormalColor else 0xa2a2a2
            invalidate()
        }
    }

    fun setSelectColor(@ColorInt selectColor: Int) {
        mSelectColor = selectColor
        if (mSelectColor != mCurrentSelectColor) {
            mCurrentSelectColor = mSelectColor
            mPaintSelect.color = if (mSelectColor != 0) mSelectColor else 0xffa000
            mPaintText.color = if (mSelectColor != 0) mSelectColor else 0xffa000
            invalidate()
        }
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener?) {
        mOnSelectListener = onSelectListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mTextSizeSelect = context.resources.displayMetrics.density * 70 / 3
        mTextSizeNormal = mTextSizeSelect / 2f

        // 默认宽高
        mPaintSelect.textSize = mTextSizeSelect
        mPaintNormal.textSize = mTextSizeNormal
        val mDefaultWidth =
            (mPaintSelect.measureText("0000") + mPaintText.measureText("时") * 2).toInt()
        val mAnIntSelect = mPaintSelect.fontMetricsInt
        val mAnIntNormal = mPaintNormal.fontMetricsInt
        val mDefaultHeight =
            mAnIntSelect.bottom - mAnIntSelect.top + (mAnIntNormal.bottom - mAnIntNormal.top) * 4
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT &&
            layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT
        ) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight)
        } else if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mDefaultWidth, heightSize)
        } else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mDefaultHeight)
        }
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaddingStart = paddingStart.toFloat()
        mPaddingEnd = paddingEnd.toFloat()
        // 绘制中间位置
        draw(canvas, 1, 0, mPaintSelect)
        // 绘制上方数据
        for (i in 1 until mSelectPosition - 1) {
            draw(canvas, -1, i, mPaintNormal)
        }
        // 绘制下方数据
        var i = 1
        while (mSelectPosition + i < mData.size) {
            draw(canvas, 1, i, mPaintNormal)
            i++
        }
        invalidate()
    }

    private fun draw(canvas: Canvas, type: Int, position: Int, paint: Paint) {
        val space = RATE * mTextSizeNormal * position + type * mMoveDistance
        val scale = parabola(mHeight / 4.0f, space)
        val size = (mTextSizeSelect - mTextSizeNormal) * scale + mTextSizeNormal
        val alpha = ((mTextAlphaSelect - mTextAlphaNormal) * scale + mTextAlphaNormal).toInt()
        paint.textSize = size
        paint.alpha = alpha
        val x = mWidth / 2.0f
        val y = mHeight / 2.0f + type * space
        val fmi = paint.fontMetricsInt
        val baseline = y + (fmi.bottom - fmi.top) / 2.0f - fmi.descent - 2
        val width = mPaintText.measureText("年")
        val index = mSelectPosition + type * position
        if (mData.isEmpty() || index < 0 || index >= mData.size) return
        canvas.drawText(
            mData[index],
            x - width / 2 + mPaddingStart - mPaddingEnd,
            baseline,
            paint
        )
        if (position == 0) {
            mText?.let { text ->
                mPaintSelect.textSize = mTextSizeSelect
                val typeText = mData[mSelectPosition]
                mPaintSelect.measureText(typeText)
                val startX = mPaintSelect.measureText(typeText) / 2 + x - width / 2 + Util.dpToPx(
                    context,
                    2.0f
                ) + mPaddingStart - mPaddingEnd
                val anInt = mPaintText.fontMetricsInt
                canvas.drawText(
                    text,
                    startX,
                    mHeight / 2.0f + (anInt.bottom - anInt.top) / 2.0f - anInt.descent,
                    mPaintText
                )
            }

            val metricsInt = paint.fontMetricsInt
            val line =
                mHeight / 2.0f + (metricsInt.bottom - metricsInt.top) / 2.0f - metricsInt.descent
            canvas.drawLine(
                0f,
                line + metricsInt.ascent - Util.dpToPx(context, 2.0f),
                mWidth.toFloat(),
                line + metricsInt.ascent - Util.dpToPx(context, 2.0f),
                mPaintLine
            )
            canvas.drawLine(
                0f,
                line + metricsInt.descent + Util.dpToPx(context, 2.0f),
                mWidth.toFloat(),
                line + metricsInt.descent + Util.dpToPx(context, 2.0f),
                mPaintLine
            )
            canvas.drawLine(
                0f,
                Util.dpToPx(context, 0.5f).toFloat(),
                mWidth.toFloat(),
                Util.dpToPx(context, 0.5f).toFloat(),
                mPaintLine
            )
            canvas.drawLine(
                0f,
                (mHeight - Util.dpToPx(context, 0.5f)).toFloat(),
                mWidth.toFloat(),
                (mHeight - Util.dpToPx(context, 0.5f)).toFloat(),
                mPaintLine
            )
        }
    }

    private fun parabola(zero: Float, x: Float): Float {
        val y = (1 - (x / zero).toDouble().pow(2.0)).toFloat()
        return if (y < 0) 0f else y
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mStartTouchY = event.y
            MotionEvent.ACTION_MOVE -> {
                mMoveDistance += event.y * 1f - mStartTouchY
                if (mMoveDistance > RATE * mTextSizeNormal / 2) {
                    // 向下滑动
                    moveTailToHead()
                    mMoveDistance -= RATE * mTextSizeNormal
                } else if (mMoveDistance < -RATE * mTextSizeNormal / 2) {
                    // 向上滑动
                    moveHeadToTail()
                    mMoveDistance += RATE * mTextSizeNormal
                }
                mStartTouchY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (abs(mMoveDistance) < 0.0001) {
                    mMoveDistance = 0f
                    return true
                }
                if (mTask != null) {
                    mTask?.cancel()
                    mTask = null
                }
                mTask = MTimerTask(mHandler)
                mTimer.schedule(mTask, 0, 10)
            }
        }
        return true
    }

    fun setData(data: List<String>) {
        mData.clear()
        mData.addAll(data)
        mSelectPosition = data.size / 2
    }

    fun setText(mText: String?) {
        this.mText = mText
    }

    private fun moveHeadToTail() {
        if (mData.size > 0) {
            val head = mData.first()
            mData.removeFirst()
            mData.add(head)
        }
    }

    private fun moveTailToHead() {
        if (mData.size > 0) {
            val tail = mData.last()
            mData.removeLast()
            mData.add(0, tail)
        }
    }

    private fun setSelectPosition(position: Int) {
        mSelectPosition = position
        val value = mData.size / 2 - mSelectPosition
        if (value < 0) {
            for (i in 0 until -value) {
                moveHeadToTail()
                mSelectPosition--
            }
        } else if (value > 0) {
            for (i in 0 until value) {
                moveTailToHead()
                mSelectPosition++
            }
        }
        invalidate()
    }

    fun setDefaultValue(value: String) {
        for (i in mData.indices) {
            if (value == mData[i]) {
                setSelectPosition(i)
                break
            }
        }
    }

    fun setDefaultValue(value: String, @DateType.Type type: String, replace: String) {
        if (mData.size > 0) {
            for (i in mData.indices) {
                val data = getStringToNumber(type, mData[i], replace)
                if (value == data) {
                    setSelectPosition(i)
                    break
                }
            }
        }
    }

    private fun getStringToNumber(
        @DateType.Type type: String,
        value: String,
        replace: String
    ): String {
        return when (type) {
            DateType.MONTH, DateType.DAY -> {
                if (value.startsWith("0") && value.length == 2) {
                    return value.substring(1)
                }
                if (value == "00") {
                    return replace
                } else if (value.startsWith("0") && value.length == 2) {
                    return value.substring(1)
                }
                value
            }
            DateType.HOUR_12, DateType.HOUR_24, DateType.MINUTE -> {
                if (value == "00") {
                    return replace
                } else if (value.startsWith("0") && value.length == 2) {
                    return value.substring(1)
                }
                value
            }
            else -> value
        }
    }

    fun getSelectValue(): String {
        return if (mData.size > 0 && mData.size > mSelectPosition) mData[mSelectPosition] else ""
    }

    internal class MHandler(view: View) : Handler() {
        private val mWeakReference: WeakReference<View>

        init {
            mWeakReference = WeakReference(view)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val mPickerView = mWeakReference.get() as? PickerView
            if (mPickerView != null) {
                if (abs(mPickerView.mMoveDistance) < SPEED) {
                    mPickerView.mMoveDistance = 0f
                    mPickerView.mTask?.cancel()
                    mPickerView.mTask = null
                    if (mPickerView.mData.isEmpty()) return
                    mPickerView.mOnSelectListener?.onSelect(
                        mPickerView,
                        mPickerView.mData[mPickerView.mSelectPosition]
                    )
                } else {
                    mPickerView.mMoveDistance =
                        mPickerView.mMoveDistance - mPickerView.mMoveDistance / abs(mPickerView.mMoveDistance) * SPEED
                }
                mPickerView.invalidate()
            }
        }
    }

    internal class MTimerTask(private val handler: Handler?) : TimerTask() {
        override fun run() {
            handler?.sendMessage(handler.obtainMessage())
        }
    }

    open interface OnSelectListener {
        fun onSelect(view: View?, data: String?)
    }

}