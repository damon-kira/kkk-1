package com.colombia.credit.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.colombia.credit.R


class TextViewDrawable : AppCompatTextView {

    private var drawableLeftWidth: Int = 0
    private var drawableLeftHeight: Int = 0
    private var drawableTopWidth: Int = 0
    private var drawableTopHeight: Int = 0
    private var drawableRightWidth: Int = 0
    private var drawableRightHeight: Int = 0
    private var drawableBottomWidth: Int = 0
    private var drawableBottomHeight: Int = 0

    private var isAlignCenter: Boolean = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context?, attrs: AttributeSet?) {
        if (context == null || attrs == null) return
        context.obtainStyledAttributes(attrs, R.styleable.TextViewDrawable).also { ta ->
            val indexCount = ta.indexCount
            var attr: Int
            for (index in 0 until indexCount) {
                attr = ta.getIndex(index)
                when (attr) {
                    R.styleable.TextViewDrawable_drawableLeftWidth -> {
                        drawableLeftWidth = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableLeftHeight -> {
                        drawableLeftHeight = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableTopWidth -> {
                        drawableTopWidth = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableTopHeight -> {
                        drawableTopHeight = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableRightWidth -> {
                        drawableRightWidth = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableRightHeight -> {
                        drawableRightHeight = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableBottomWidth -> {
                        drawableBottomWidth = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_drawableBottomHeight -> {
                        drawableBottomHeight = getAttr(ta, attr)
                    }
                    R.styleable.TextViewDrawable_isAlignCenter -> {
                        isAlignCenter = ta.getBoolean(attr, false)
                    }
                }
            }
            ta.recycle()
        }
    }

    private fun getAttr(ta: TypedArray, attr: Int): Int {
        var size = ta.getDimensionPixelOffset(attr, 0)
        if (size == 0) {
            val resource = ta.getResourceId(attr, 0)
            size = checkResource(resource)
        }
        return size
    }

    private fun checkResource(resource: Int): Int {
        return if (resource == 0) 0
        else context.resources.getDimensionPixelOffset(resource)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        try {
            val drawables = compoundDrawables
            if (drawables.size != 4) return
            val drawableLeft = drawables[0]
            if (drawableLeft != null) {
                setDrawable(drawableLeft, 0, drawableLeftWidth, drawableLeftHeight)
            }
            val drawableTop = drawables[1]
            if (drawableTop != null) {
                setDrawable(drawableTop, 1, drawableTopWidth, drawableTopHeight)
            }
            val drawableRight = drawables[2]
            if (drawableRight != null) {
                setDrawable(drawableRight, 2, drawableRightWidth, drawableRightHeight)
            }
            val drawableBottom = drawables[3]
            if (drawableBottom != null) {
                setDrawable(drawableBottom, 3, drawableBottomWidth, drawableBottomHeight)
            }
            setCompoundDrawables(drawableLeft,drawableTop,drawableRight,drawableBottom);
        } catch (e: Exception) {
        }
    }

    private fun setDrawable(drawable: Drawable, tag: Int, drawableWidth: Int, drawableHeight: Int) {
        val width = if (drawableWidth == 0) drawable.intrinsicWidth else drawableWidth
        val height = if (drawableWidth == 0) drawable.intrinsicHeight else drawableHeight
        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        when (tag) {
            0, 2 -> {
                left = 0
                top = if (isAlignCenter) 0 else -lineCount * lineHeight / 2 + lineHeight / 2
                right = width
                bottom = top + height
            }
            1 -> {
                left = if (isAlignCenter) 0 else -getWidth() / 2 + width / 2
                top = 0
                right = left + width
                bottom = top + height
            }
        }
        drawable.setBounds(left, top, right, bottom)
    }


}