package com.colombia.credit.module.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.view.View
import android.view.textclassifier.TextLanguage
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.SearchInfo
import com.util.lib.dip2px
import com.util.lib.dp
//import me.jessyan.autosize.utils.AutoSizeUtils.dp2px
//import me.jessyan.autosize.utils.AutoSizeUtils.sp2px


class BankItemDecoration(
    private val context: Context,
    private val mDatalist: List<SearchInfo>,
    private val hasHotTitle: Boolean = false
) : RecyclerView.ItemDecoration() {
    private val mBgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)//画背景
    private val mTextPaint: Paint//画文字
    private val mTitleHeight: Int//Title的高度
    private val mHotTitleHeigh: Int //热门高度
    private val mLinePaint = Paint()
    private var isHot = false
    private val mHotString = "Bancos populares"

    init {
        mBgPaint.color =
            ContextCompat.getColor(context, R.color.color_F8F8F8)

        mLinePaint.color = ContextCompat.getColor(context, R.color.color_F8F8F8)
        mLinePaint.strokeWidth = 1.dp()

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color =
            ContextCompat.getColor(context, R.color.color_FF989898)
        mTextPaint.textSize =  12f

        mTitleHeight = 28
        mHotTitleHeigh = 40

    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.layoutManager?.getPosition(view) ?: 0
        if (position >= mDatalist.size || position < 0) {
            return
        }
        if (position == 0) {//第一个条目肯定需要Title
            if (hasHotTitle) {
                outRect.set(0, mHotTitleHeigh, 0, 0)
            } else {
                outRect.set(0, mTitleHeight, 0, 0)
            }
            return
        }
        if (checkItem(position)) {
            //当前条目和上一个条目的第一个拼音不同时需要Title
            outRect.set(0, mTitleHeight, 0, 0)
            return
        }
        outRect.set(0, 1f.dp(), 0, 0)
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val textLeft = 16.dp()
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.layoutManager?.getPosition(child) ?: 0
            if (position >= mDatalist.size || position < 0) {
                return
            }
            isHot = mDatalist[position].isHot()
            if (position == 0) {
                var bgHeight: Int
                if (hasHotTitle) {
                    mBgPaint.color =
                        ContextCompat.getColor(context, R.color.white)
                    bgHeight = mHotTitleHeigh
                } else {
                    mBgPaint.color = ContextCompat.getColor(
                        context,
                        R.color.color_F5F5F5
                    )
                    bgHeight = mTitleHeight
                }
                drawBg(
                    c,
                    0f,
                    (child.top - bgHeight).toFloat(),
                    parent.right.toFloat(),
                    child.top.toFloat(),
                    mBgPaint
                )
                val text = mDatalist[position].getTag()
                if (text.isEmpty()) {
                    return
                }
                val rect = Rect()
                mTextPaint.getTextBounds(text, 0, 1, rect)
                mTextPaint.color = ContextCompat.getColor(context, R.color.color_FF989898)
                //画文字
                drawText(
                    c,
                    text,
                    textLeft,
                    (child.top - (bgHeight / 2 - rect.height() / 2)).toFloat(),
                    mTextPaint
                )
            }

            if (checkItem(position) || position == 0) {
                mBgPaint.color = ContextCompat.getColor(
                    context,
                    R.color.color_F8F8F8
                )
                //画背景
                drawBg(
                    canvas = c,
                    left = 0f,
                    top = (child.top - mTitleHeight).toFloat(),
                    right = parent.right.toFloat(),
                    bottom = child.top.toFloat(),
                    paint = mBgPaint
                )
                val text = mDatalist[position].getTag()
                if (text.isEmpty()) {
                    return
                }
                val rect = Rect()
                mTextPaint.getTextBounds(text, 0, 1, rect)
                mTextPaint.color =
                    ContextCompat.getColor(context, R.color.color_FF989898)
                //画文字
                drawText(
                    canvas = c,
                    text = text,
                    x =16f,
                    y = (child.top - (mTitleHeight / 2 - rect.height() / 2)).toFloat(),
                    paint = mTextPaint
                )
            } else {
                //画背景
                drawBg(
                    canvas = c,
                    left = 0f,
                    top = (child.top - 1.dp()),
                    right = parent.right.toFloat(),
                    bottom = child.top.toFloat(),
                    paint = mBgPaint
                )
            }
        }
    }

    private fun checkItem(position: Int) =
        position > 0 && mDatalist[position].getTag() != mDatalist[position - 1].getTag() && (mDatalist[position].isHot() != mDatalist[position - 1].isHot() || !mDatalist[position].isHot())

//    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        super.onDrawOver(c, parent, state)
//        //第一个可见条目的位置
//        val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//        if (position >= mDatalist.size || position < 0) {
//            return
//        }
//        var bgBottom = 0f
//        var textLeft = 16.dp()
//        isHot = mDatalist[position].isHot()
//        if (isHot) {
//            bgBottom = mHotTitleHeigh.toFloat()
//            mTextPaint.color =
//                ContextCompat.getColor(context, R.color.color_999999)
//        } else {
//            bgBottom = mTitleHeight.toFloat()
//            mTextPaint.color =
//                ContextCompat.getColor(context, R.color.colorPrimary)
//            textLeft = 16.dp()
//        }
//        //画背景
//        drawBg(c, 0f, 0f, parent.right.toFloat(), bgBottom, mBgPaint, isHot)
//        //绘制常用银行icon
//        drawIcon(c, parent.context, 24.dp())
//
//        val text = mDatalist[position].getTag()
//        if (text.isEmpty()) {
//            return
//        }
//        val rect = Rect()
//        mTextPaint.getTextBounds(text, 0, 1, rect)
//
//        //画文字
//        drawText(
//            canvas = c,
//            text = text,
//            x = textLeft,
//            y = (bgBottom / 2 + rect.height() / 2),
//            paint = mTextPaint
//        )
//    }

    /**
     * 绘制热门icon
     */
    fun drawIcon(
        c: Canvas,
        context: Context,
        y: Float
    ) {
        if (isHot) {
            val drawable = AppCompatResources.getDrawable(
                context,
                R.drawable.ic_hot_tag
            )?.toBitmap()
            val left = mTextPaint.measureText(mHotString)
            drawable?.let {
                c.drawBitmap(
                    it,
                    left + 22.dp(),
                    y,
                    null
                )
            }
//            if (drawable != null && !drawable.isRecycled) {
//                drawable.recycle()
//            }
        }
    }

    /**
     * 绘制背景
     */
    fun drawBg(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint,
        drawHOt: Boolean = false
    ) {
        if (drawHOt) {
            canvas.drawLine(0f, bottom, right, bottom, mLinePaint)
        }
        canvas.drawRect(
            left,
            top,
            right,
            bottom,
            paint
        )
    }

    fun drawText(canvas: Canvas, text: String, x: Float, y: Float, paint: Paint) {
        var currentStr = text
        if (isHot) {
            currentStr = mHotString
        }
        canvas.drawText(currentStr, x, y, paint)
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        drawIcon(canvas, context, y - bounds.height())

    }

}
