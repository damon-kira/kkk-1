package com.colombia.credit.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.dp


class ScannerView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    val scanRectf = RectF()//圆角矩形
    val scanRegion = Region()//圆角矩形区域

    //    val scanPath = Path()//圆角矩形的path
    val allRegion = Region()//总的区域

    val pathPaint = Paint().also {
        it.color = Color.WHITE
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = 4f
    }

    val paint_fill = Paint().also {
        it.color = ContextCompat.getColor(context, R.color.color_80FFFFFF)
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
    }

    var mWidth = 0
    var mHeight = 0

    private val scanPath: Path = Path()


    private val TAG = "ScannerView"

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        scanRectf.left = paddingLeft.toFloat()
        scanRectf.top = paddingTop.toFloat()
        scanRectf.bottom = mHeight - paddingBottom * 1f
        scanRectf.right = mWidth - paddingRight.toFloat()

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {

        allRegion.set(0, 0, mWidth, mHeight)

        scanPath.addRoundRect(scanRectf, 32f, 32f, Path.Direction.CW)

        canvas?.drawPath(scanPath, pathPaint)


        scanRegion.setPath(scanPath, allRegion)

        //取扫描区域的补集
        allRegion.op(scanRegion, Region.Op.XOR)


        //再构造一个画笔,填充Region操作结果
        drawRegion(canvas, allRegion, paint_fill)
    }

    private fun drawRegion(canvas: Canvas?, region: Region, paint: Paint) {
        val iter = RegionIterator(region)
        val r = Rect()
        while (iter.next(r)) {
            canvas?.drawRect(r, paint)
        }
    }

    fun getScannerRect(): RectF = scanRectf
}