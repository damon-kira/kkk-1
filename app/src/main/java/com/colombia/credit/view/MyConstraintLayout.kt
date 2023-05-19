package com.colombia.credit.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class MyConstraintLayout : ConstraintLayout {

    private var mITouchEvent: ITouchEvent? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(mITouchEvent?.onInterceptTouchEvent(ev) == true) {
            return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun setTouchEvent(touchEvent: ITouchEvent){
        this.mITouchEvent = touchEvent
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mITouchEvent = null
    }

    interface ITouchEvent {
        fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
    }
}