package com.colombia.credit.module.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.SearchInfo
import com.util.lib.dip2px
import me.jessyan.autosize.utils.AutoSizeUtils.dp2px
import me.jessyan.autosize.utils.AutoSizeUtils.sp2px


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
    private val mHotTag = "#"
    private val mHotString = "Bancos frecuentes"
    init {
        mBgPaint.color =
            ContextCompat.getColor(context, R.color.color_F5F5F5)

        mLinePaint.color = ContextCompat.getColor(context, R.color.color_12000000)
        mLinePaint.strokeWidth = dp2px(context, 1f).toFloat()

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color =
            ContextCompat.getColor(context, R.color.color_FF989898)
        mTextPaint.textSize = sp2px(context, 12f).toFloat()

        mTitleHeight = dp2px(context, 28f)

        mHotTitleHeigh = dp2px(context, 40f)

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
        if (mDatalist[position].tag != mDatalist[position - 1].tag) {
            //当前条目和上一个条目的第一个拼音不同时需要Title
            outRect.set(0, mTitleHeight, 0, 0)
            return
        }
        outRect.set(0, 0, 0, 0)
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.layoutManager?.getPosition(child) ?: 0
            if (position >= mDatalist.size || position < 0) {
                return
            }

            if (position == 0) {
                var bgHeight: Int
                var textLeft : Float

                if (hasHotTitle) {
                    mBgPaint.color =
                        ContextCompat.getColor(context, R.color.white)
                    bgHeight = mHotTitleHeigh
                    textLeft = dp2px(context, 52f).toFloat()
                } else {
                    mBgPaint.color = ContextCompat.getColor(
                        context,
                        R.color.color_F5F5F5
                    )
                    bgHeight = mTitleHeight
                    textLeft = dp2px(context, 16f).toFloat()
                }
                drawBg(
                    c,
                    0f,
                    (child.top - bgHeight).toFloat(),
                    parent.right.toFloat(),
                    child.top.toFloat(),
                    mBgPaint
                )

                val text = mDatalist[position].tag.orEmpty()

                if (text.isEmpty()) {
                    return
                }
                val rect = Rect()
                mTextPaint.getTextBounds(text, 0, 1, rect)
                mTextPaint.color =
                    ContextCompat.getColor(context, R.color.color_FF989898)
                //画文字
                c.drawText(
                    text,
                    textLeft,
                    (child.top - (bgHeight / 2 - rect.height() / 2)).toFloat(),
                    mTextPaint
                )
            }

            if (position > 0 && mDatalist[position].tag != mDatalist[position - 1].tag) {
                mBgPaint.color = ContextCompat.getColor(
                    context,
                    R.color.color_F5F5F5
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
                val text = mDatalist[position].tag.orEmpty()
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
                    x = dp2px(context, 16f).toFloat(),
                    y = (child.top - (mTitleHeight / 2 - rect.height() / 2)).toFloat(),
                    paint = mTextPaint
                )
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //第一个可见条目的位置
        val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (position >= mDatalist.size || position < 0) {
            return
        }

        var bgBottom = 0f
        var textLeft = 0f
        val isHot = mDatalist[position].tag == mHotTag
        if (isHot){
            mBgPaint.color = ContextCompat.getColor(
                context,
                R.color.white
            )
            bgBottom = mHotTitleHeigh.toFloat()
            mTextPaint.color =
                ContextCompat.getColor(context, R.color.color_FF989898)
            textLeft = dp2px(context, 52f).toFloat()
        }else{
            mBgPaint.color = ContextCompat.getColor(
                context,
                R.color.color_F5F5F5
            )
            bgBottom = mTitleHeight.toFloat()
            mTextPaint.color =
                ContextCompat.getColor(context, R.color.colorPrimary)
            textLeft = dp2px(context, 16f).toFloat()
        }

        //画背景
        drawBg(c, 0f, 0f, parent.right.toFloat(), bgBottom, mBgPaint,isHot)

        //绘制常用银行icon
        drawIcon(c,parent,isHot)


        val text = mDatalist[position].tag.orEmpty()
        if (text.isEmpty()) {
            return
        }
        val rect = Rect()
        mTextPaint.getTextBounds(text, 0, 1, rect)

        //画文字
        drawText(
            canvas = c,
            text = text,
            x = textLeft,
            y = (bgBottom / 2 + rect.height() / 2),
            paint = mTextPaint
        )
    }



    /**
     * 绘制热门icon
     */
    fun drawIcon(
        c: Canvas,
        parent: RecyclerView,
        hot: Boolean
    ) {
        if (hot){
            val drawable = AppCompatResources.getDrawable(
                parent.context,
                R.drawable.ic_hot_tag
            )?.toBitmap()

            drawable?.let {
                c.drawBitmap(
                    it,
                    dip2px(parent.context, 16f).toFloat(),
                    dip2px(parent.context, 11f).toFloat(),
                    null
                )
            }
//            if (drawable != null && !drawable.isRecycled){
//                drawable.recycle()
//            }
        }
    }

    /**
     * 绘制背景
     */
    fun  drawBg(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint,
        drawHOt: Boolean = false
    ) {
        if (drawHOt){
            canvas.drawLine(0f,bottom,right,bottom,mLinePaint)
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
        if (currentStr == mHotTag){
            currentStr = mHotString
        }
        canvas.drawText(currentStr, x, y, paint)
    }

}
