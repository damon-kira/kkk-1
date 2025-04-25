package com.common.lib.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.common.lib.R
import com.util.lib.dp
import com.util.lib.log.logger_i

/**
 * created by wangjiazhen 2018/12/29
 */
class LoadingView : View {
    companion object {
        const val TAG = "LoadingView"
        const val DEFAULT_LINE_WIDTH = 10F//单位：px 默认线宽度
        const val CIRCLE_MIN_SWEEP_ANGLE = 90F//单位：度，圈最小扫描角度，一圈为360F
        const val CIRCLE_MAX_SWEEP_ANGLE = 270F//单位：度，圈最大扫描角度，一圈为360F
        const val SWING_ANGLE = 15//单位：度
    }

    private val mPaint = Paint()
    /**
     *  边界大小，正方形（长宽最小值）
     */
    private var mRectSize: Int = 0
    /**
     * 扫描角度，单位：度
     */
    private var mSweepAngle = CIRCLE_MIN_SWEEP_ANGLE
    /**
     * 起始角度，单位：度
     */
    private var mStartRotateAngle: Float = 0F
        set(value) {
            //用于动画设置value值
            field = 90F + value * 360//90度起始，转一圈value为1，即（90+360）度
        }
    private val mAnimatorSet = AnimatorSet()
    private var mLineWidth = DEFAULT_LINE_WIDTH
    /**
     * 起始点变化的集合,arrayOf为角度变化集合，转换为相应的Float（一圈360度为1F）；顺时针为正，逆时针为负；
     */
    private val mAnimatorStartAngleFloats: List<Float> =
        arrayOf(0, SWING_ANGLE, -(90 + SWING_ANGLE), 270, 720).map { it / 360F }
    /**
     * 底色渐变：左上到右下:#E6EDFE -#F9FBFF
     */
    private val mBackgroundGradientStartColor = Color.parseColor("#E6EDFE")
    private val mBackgroundGradientEndColor = Color.parseColor("#F9FBFF")
    private val mBackgroundGradient: LinearGradient by lazy {
        LinearGradient(
            0F,
            0F,
            mRectSize.toFloat(),
            mRectSize.toFloat(),
            mBackgroundGradientStartColor,
            mBackgroundGradientEndColor,
            Shader.TileMode.CLAMP
        )
    }//代理属性，避免mRectSize值为0

    private val mRect: RectF by lazy {
        RectF(mLineWidth, mLineWidth, mRectSize.toFloat() - mLineWidth, mRectSize.toFloat() - mLineWidth)
    }//代理属性，避免mRectSize值为0

    /**
     * 线的颜色:#02C792
     */
    private val mCircleColor = ContextCompat.getColor(context, R.color.colorPrimary)

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mLineWidth = 3.dp()
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = mLineWidth
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.color = mCircleColor
        initAnimatorSet()
    }

    private fun initAnimatorSet() {
        val animator1 = ValueAnimator.ofFloat(
            mAnimatorStartAngleFloats[0],
            mAnimatorStartAngleFloats[1],
            mAnimatorStartAngleFloats[2]
        )
        animator1.duration = (difference(
            mAnimatorStartAngleFloats[0],
            mAnimatorStartAngleFloats[1],
            mAnimatorStartAngleFloats[2]
        ) * 2800).toLong()
        animator1.interpolator = DecelerateInterpolator()
        animator1.addUpdateListener {
            animatorUpdate(it) {
                CIRCLE_MIN_SWEEP_ANGLE
            }
        }

        val animator2 = ValueAnimator.ofFloat(mAnimatorStartAngleFloats[2], mAnimatorStartAngleFloats[3])
        animator2.duration = (difference(mAnimatorStartAngleFloats[2], mAnimatorStartAngleFloats[3]) * 800).toLong()
        animator2.interpolator = AccelerateInterpolator()
        animator2.addUpdateListener {
            animatorUpdate(it) { value ->
                CIRCLE_MIN_SWEEP_ANGLE + (CIRCLE_MAX_SWEEP_ANGLE - CIRCLE_MIN_SWEEP_ANGLE) * value.coefficient(
                    mAnimatorStartAngleFloats[2],
                    mAnimatorStartAngleFloats[3]
                )
            }
        }

        val animator3 = ValueAnimator.ofFloat(mAnimatorStartAngleFloats[3], mAnimatorStartAngleFloats[4])
        animator3.interpolator = DecelerateInterpolator()
        animator3.duration = (difference(mAnimatorStartAngleFloats[3], mAnimatorStartAngleFloats[4]) * 800).toLong()
        animator3.addUpdateListener {
            animatorUpdate(it) { value ->
                CIRCLE_MAX_SWEEP_ANGLE - (CIRCLE_MAX_SWEEP_ANGLE - CIRCLE_MIN_SWEEP_ANGLE) * value.coefficient(
                    mAnimatorStartAngleFloats[3],
                    mAnimatorStartAngleFloats[4]
                )
            }
        }
        mAnimatorSet.cancel()
        mAnimatorSet.playSequentially(animator1, animator2, animator3)
        mAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                startAnimator()
            }
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                if (!isReverse) {
                    startAnimator()
                }
            }
        })
    }

    private fun animatorUpdate(valueAnimator: ValueAnimator, sweepChange: (Float) -> Float) {
        val value = valueAnimator.animatedValue as Float
        mStartRotateAngle = value
        mSweepAngle = sweepChange(value)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        mRectSize = Math.min(widthSpecSize, heightSpecSize)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            mPaint.shader = mBackgroundGradient
            drawCircle(mRectSize / 2F, mRectSize / 2F, (mRectSize / 2.0F) - mLineWidth, mPaint)
            mPaint.shader = null
            drawArc(mRect, mStartRotateAngle, mSweepAngle, false, mPaint)
        }
        super.onDraw(canvas)
    }

    /**
     * 系数转变；用于计算扫描角度
     * F（x) = x-a/|b-a|
     * 将value改变范围[a,b]转换为[0,1]
     */
    private fun Float.coefficient(a: Float, b: Float): Float {
        return (this - a) / Math.abs(b - a)
    }

    /**
     * 计算转变的角度之和
     * 根据不同的角度计算动画时间
     */
    private fun difference(vararg points: Float): Float {
        var acc = 0F
        if (points.size < 2) return -1F
        for (index in 1 until points.size) {
            acc += Math.abs(points[index] - points[index - 1])
        }
        return acc
    }

    /* 以下用于启动动画*/
    private var canStart = true

    private fun startAnimator() {
        if (canStart) {
            mAnimatorSet.start()
            logger_i(TAG, "start")
        } else {
            logger_i(TAG, "not can start")
        }
    }

    fun cancelAnimator() {
        mAnimatorSet.end()
        logger_i(TAG, "end, isRunning = ${mAnimatorSet.isRunning}")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        canStart = true
        startAnimator()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        canStart = false
        cancelAnimator()
    }
//
//    override fun onWindowVisibilityChanged(visibility: Int) {
//        super.onWindowVisibilityChanged(visibility)
//        if (visibility != VISIBLE) {
//            cancelAnimator()
//        }
//    }


    fun pauseAnim(){
        if(mAnimatorSet.isRunning){
            mAnimatorSet.pause()
        }
    }

    fun resumeAnim(){
        if(mAnimatorSet.isPaused){
            mAnimatorSet.resume()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE && this.visibility == VISIBLE) {
            resumeAnim()
        } else {
            pauseAnim()
        }
    }

}