package com.colombia.credit.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.DisplayUtils
import com.util.lib.dp
import com.util.lib.log.logger_d


class WheelView : ScrollView {

    companion object {
        const val checkInterval = 80L
        const val TAG = "WheelView"
    }

    private lateinit var containerLayout: LinearLayout

    private var itemList = ArrayList<String>()

    private var oldY: Int = 0

    private var itemHeight = 56f.dp()

    private var itemWidth = 0

    private var displayItemCount = 0 //每页显示的数量
    private var itemTextSize = 14f
    private var highlightItemTextSize = 16f

    private var itemTextColor = context.resources.getColor(R.color.color_black)
    private var highlightItemTextColor = context.resources.getColor(R.color.color_FF4C4C4C)
    private var dividerColor = ContextCompat.getColor(context, R.color.color_D9E5F4)
    private var bgColor = ContextCompat.getColor(context, R.color.white)


    private val offset = 2
    private var selectedIndex = offset

    private lateinit var bgPaint: Paint
    lateinit var textPaint: Paint
    lateinit var dividerPaint: Paint

    var highlightText: String = ""
        set(value) {
            field = value
            setBgDrawable()
        }

    var highlightTextColorId: Int = context.resources.getColor(R.color.color_D9E5F4)
    var distance: Float = 0f

    private lateinit var itemLayoutParams: LinearLayout.LayoutParams

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        attrs ?: return
        context.obtainStyledAttributes(attrs, R.styleable.WheelView).apply {
            for (index in 0 until indexCount) {
                val attr = getIndex(index)
                when (attr) {
                    R.styleable.WheelView_wl_view_bg_color -> {
                        bgColor =
                            getColor(attr, ContextCompat.getColor(context, R.color.color_FFF6FAFC))
                    }
                    R.styleable.WheelView_wl_item_select_bg_color -> {
                        dividerColor =
                            getColor(attr, ContextCompat.getColor(context, R.color.color_D9E5F4))
                    }
                    R.styleable.WheelView_wl_text_select_color -> {
                        highlightItemTextColor =
                            getColor(attr, ContextCompat.getColor(context, R.color.color_FF4C4C4C))
                    }
                    R.styleable.WheelView_wl_text_unselect_color -> {
                        itemTextColor =
                            getColor(attr, ContextCompat.getColor(context, R.color.color_black))
                    }
                }
            }
            recycle()
        }
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributeSet(context, attrs)
        isVerticalScrollBarEnabled = false

        containerLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            this@WheelView.addView(this@apply)
        }

        bgPaint = Paint().apply {
            strokeWidth = 1f.dp().toFloat()
            color = bgColor
        }

        textPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            textAlign = Paint.Align.CENTER
            color = highlightTextColorId
            textSize = 12f.dp().toFloat()

            val fontMetrics = fontMetrics
            distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        }

        dividerPaint = Paint().apply {
            isAntiAlias = true
            color = dividerColor
        }

        itemLayoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight)
    }

    fun updateItemList(itemList: List<String>) {
        this@WheelView.itemList.apply {
            clear()
            addAll(itemList)
            for (i in 0 until offset) { //补齐
                add(0, "")
                add("")
            }
        }

        initData()
    }

    fun setSelection(pos: Int) {
        selectedIndex = pos + offset
        post {
            this@WheelView.scrollTo(0, pos * itemHeight)
        }
    }

    private fun initData() {
        displayItemCount = 2 * offset + 1
        containerLayout.apply {
            removeAllViews()
            itemList.forEach {
                addView(createTextView(it))
            }

            layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, itemHeight * displayItemCount)
            this@WheelView.layoutParams =
                LinearLayout.LayoutParams(layoutParams.width, itemHeight * displayItemCount)
        }
        highlightSelectedView(0)
    }

    private fun createTextView(item: String) = TextView(context).apply {
        layoutParams = itemLayoutParams
        isSingleLine = true
        textSize = itemTextSize
        gravity = Gravity.CENTER
        setTextColor(itemTextColor)
        text = item
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_UP -> {
                startScrollCheckTask()
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        logger_d(TAG, "onSizeChanged>>w:$w,h:$h,oldw:$oldw,oldh:$oldh")
        itemWidth = w
        setBgDrawable()
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        highlightSelectedView(t)
    }

    /**
     * 高亮选中的view
     */
    private fun highlightSelectedView(top: Int) {
        val modY = top % itemHeight
        val divided = top / itemHeight
        var position = divided + offset

        if (modY != 0) { //需要校正
            if (modY > itemHeight / 2) {
                position++
            }
        }

        containerLayout.apply {
            for (i in 0 until childCount) {
                getChildAt(i)?.let { childView: View ->
                    if (childView is TextView) {
                        if (i == position) {
                            childView.setTextColor(highlightItemTextColor)
                            childView.textSize = highlightItemTextSize
                        } else {
                            childView.setTextColor(itemTextColor)
                            childView.textSize = itemTextSize
                        }
                    }
                }
            }
        }
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY / 3)
    }

    /**
     * 滑动检测任务
     */
    private val scrollCheckTask = Runnable {
        val newY = scrollY
        if (oldY == newY) { //滑动结束
            val modY = newY % itemHeight
            val divided = newY / itemHeight
            selectedIndex = divided + offset
            if (modY == 0) { //静止的位置ok
                notifySelectEvent()
            } else { //校正停止位置
                if (modY > itemHeight / 2) { //向上滑动
                    post {
                        smoothScrollTo(0, newY - modY + itemHeight)
                        selectedIndex++
                        notifySelectEvent()
                    }
                } else {//向下滑动
                    post {
                        smoothScrollTo(0, newY - modY)
                        selectedIndex
                        notifySelectEvent()
                    }
                }
            }
        } else { //继续检测监听滑动
            startScrollCheckTask()
        }
    }

    fun getCurrItem(): String? {
        return if (selectedIndex >= 0 && selectedIndex < itemList.size) {
            itemList[selectedIndex]
        } else null
    }

    private fun notifySelectEvent() {
        if (selectedIndex >= itemList.size) return
        onWheelViewListener?.onSelected(selectedIndex - offset, itemList[selectedIndex])
    }

    private fun startScrollCheckTask() {
        oldY = scrollY
        postDelayed(scrollCheckTask, checkInterval)
    }


    private fun setBgDrawable() {
        ensureItemWidth()
        val drawable = object : Drawable() {
            override fun setAlpha(alpha: Int) {
            }

            override fun getOpacity(): Int {
                return PixelFormat.UNKNOWN
            }

            override fun setColorFilter(colorFilter: ColorFilter?) {
            }

            override fun draw(canvas: Canvas) {
                val border = obtainSelectedAreaBorder()
                border?.apply {
                    val top = get(0).toFloat()
                    val bottom = get(1).toFloat()
                    val strokeWidth = bottom - top
                    val startY = top + strokeWidth / 2
                    val endX = itemWidth.toFloat()
                    dividerPaint.strokeWidth = strokeWidth
                    canvas?.drawRect(0f, 0f, endX, startY * displayItemCount, bgPaint)
                    canvas?.drawLine(0f, startY, endX, startY, dividerPaint)
                    if (!TextUtils.isEmpty(highlightText)) {
                        canvas?.drawText(
                            highlightText,
                            itemWidth * 5 / 8f,
                            (top + bottom) / 2 + distance,
                            textPaint
                        )
                    }
                }
            }
        }
        setBackgroundDrawable(drawable)
    }

    private fun ensureItemWidth() {
        if (itemWidth == 0) {
            itemWidth = DisplayUtils.getRealScreenWidth(context)
        }
    }

    /**
     * 获取选中区域的边界
     */
    var selectedAreaBorder: IntArray? = null

    private fun obtainSelectedAreaBorder(): IntArray? {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = IntArray(2)
            selectedAreaBorder?.let {
                it[0] = itemHeight * offset
                it[1] = itemHeight * (offset + 1)
            }

        }
        return selectedAreaBorder
    }

    ///////////////////////////////////////////////////////////////////////////
    // listener
    ///////////////////////////////////////////////////////////////////////////
    var onWheelViewListener: OnWheelViewListener? = null

    interface OnWheelViewListener {
        fun onSelected(pos: Int, item: String)
    }

}