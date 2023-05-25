package com.colombia.credit.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.colombia.credit.R


class ScannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val scanRectf = RectF()//圆角矩形
    private val scanRegion = Region()//圆角矩形区域

    //    val scanPath = Path()//圆角矩形的path
    private val allRegion = Region()//总的区域

    private val pathPaint = Paint().also {
        it.color = Color.WHITE
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.strokeWidth = 4f
    }

    private val paint_fill = Paint().also {
        it.color = ContextCompat.getColor(context, R.color.color_80FFFFFF)
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
    }

    private var mWidth = 0
    private var mHeight = 0

    private val scanPath: Path = Path()

//    private val linePath = Path()
//    private val lineLength = 40.dp()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        val pLeft = paddingLeft * 1f
        val pTop = paddingTop * 1f
        val right = mWidth - paddingRight.toFloat()
        val bottom = mHeight - paddingBottom * 1f
//        linePath.reset()
//        linePath.rMoveTo(pLeft, pTop + lineLength)
//        linePath.rLineTo(0f, -lineLength)
//        linePath.rLineTo(lineLength, 0f)
//
//        linePath.rMoveTo(right - pLeft - lineLength * 2, 0f)
//        linePath.rLineTo(lineLength, 0f)
//        linePath.rLineTo(0f, lineLength)
//
//        linePath.rMoveTo(0f, bottom - pTop - lineLength * 2)
//        linePath.rLineTo(0f, lineLength)
//        linePath.rLineTo(-lineLength, 0f)
//
//        linePath.rMoveTo(-(right - pLeft - lineLength * 2), 0f)
//        linePath.rLineTo(-lineLength, 0f)
//        linePath.rLineTo(0f, -lineLength)
//
//        linePath.rMoveTo(-(bottom - pTop), 0f)
//        linePath.close()

        scanRectf.left = pLeft
        scanRectf.top = pTop
        scanRectf.bottom = mHeight - paddingBottom * 1f
        scanRectf.right = mWidth - paddingRight.toFloat()

    }

    override fun onDraw(canvas: Canvas?) {

        allRegion.set(0, 0, mWidth, mHeight)

        scanPath.addRoundRect(scanRectf, 32f, 32f, Path.Direction.CW)

        canvas?.drawPath(scanPath, pathPaint)
//        canvas?.drawPath(linePath, pathPaint)

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