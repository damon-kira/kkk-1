package com.colombia.credit.module.ocr

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.colombia.credit.module.ocr.utils.Utils

/**
 * 网格参考线
 */
class ReferenceLine : View {
    private var mLinePaint: Paint? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        mLinePaint = Paint()
        mLinePaint!!.setAntiAlias(true)
        mLinePaint!!.setColor(Color.parseColor("#ffffffff"))
        mLinePaint!!.setStrokeWidth(1f)
    }


    override fun onDraw(canvas: Canvas) {
        val screenWidth = Utils.getScreenWH(getContext())?.widthPixels ?: 0
        val screenHeight = Utils.getScreenWH(getContext())?.heightPixels ?: 0

        val width = screenWidth / 3
        val height = screenHeight / 3

        run {
            var i = width
            var j = 0
            while (i < screenWidth && j < 2) {
                canvas.drawLine(i.toFloat(), 0f, i.toFloat(), screenHeight.toFloat(), mLinePaint!!)
                i += width
                j++
            }
        }
        var j1 = height
        var i1 = 0
        while (j1 < screenHeight && i1< 2) {
            canvas.drawLine(0f, j1.toFloat(), screenWidth.toFloat(), j1.toFloat(), mLinePaint!!)
            j1 += height
            i1++
        }
    }
}
