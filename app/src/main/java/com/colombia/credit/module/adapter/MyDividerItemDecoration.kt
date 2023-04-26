package com.colombia.credit.module.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.colombia.credit.R
import com.util.lib.dp


class MyDividerItemDecoration(private val context: Context, orientation: Int) :
    ItemDecoration() {

    private var mOrientation: Int = VERTICAL_LIST

    private val mPaint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.color = ContextCompat.getColor(context, R.color.color_eeeeee)
            it.strokeWidth = 1.dp()
            it.style = Paint.Style.FILL

        }
    }

    init {
        setOrientation(orientation)
    }

    //设置屏幕的方向
    private fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)) { "invalid orientation" }
        mOrientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == HORIZONTAL_LIST) {
            drawHorizontalLine(c, parent)
        } else {
            drawVerticalLine(c, parent)
        }
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    private fun drawHorizontalLine(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)

            //获得child的布局信息
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            c.drawLine(0f, top * 1f, right * 1f, top * 1f, mPaint)
        }
    }

    //画竖线
    private fun drawVerticalLine(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)

            //获得child的布局信息
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left: Int = child.right + params.rightMargin
            c.drawLine(left * 1f, top * 1f, bottom * 1f, left * 1f, mPaint)
        }
    }


    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mOrientation == HORIZONTAL_LIST) {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, 1f.dp())
        } else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, 1f.dp(), 0)
        }
    }

    companion object {
        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}
