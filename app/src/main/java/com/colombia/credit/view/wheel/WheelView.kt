package com.colombia.credit.view.wheel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.colombia.credit.R
import com.util.lib.log.logger_e
import com.util.lib.sp
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * 3d滚轮控件
 */
class WheelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    enum class ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DAGGLE
    }

    enum class DividerType {
        // 分隔线类型
        FILL, WRAP, NONE
    }

    //分隔线类型
    private var dividerType: DividerType? = null
    var messageHandler: Handler? = null
        private set
    private var gestureDetector: GestureDetector? = null
    var onItemSelectedListener: OnItemSelectedListener? = null
    private var isOptions = false
    private var isCenterLabel = true

    // Timer mTimer;
    var mExecutor = Executors.newSingleThreadScheduledExecutor()
    private var mFuture: ScheduledFuture<*>? = null
    var paintOuterText: Paint? = null
    var paintCenterText: Paint? = null
    var paintIndicator: Paint? = null
    private var adapter: WheelAdapter<Any>? = null

    //附加单位
    private var label: String? = null
    private var textSize = 18.sp() //选项的文字大小
    var maxTextWidth = 0
    var maxTextHeight = 0
    private var textXOffset = 0
    var itemHeight //每行高度
            = 0f
        private set
    private var typeface = Typeface.MONOSPACE //字体样式，默认是等宽字体
    private var textColorOut = -0x575758
    private var textColorCenter = -0xd5d5d6
    private var dividerColor = -0x2a2a2b

    // 条目间距倍数
    private var lineSpacingMultiplier = 1.6f
    var isLoop = false
        private set

    // 第一条线Y坐标值
    var firstLineY = 0f

    //第二条线Y坐标
    var secondLineY = 0f

    //中间label绘制的Y坐标
    var centerY = 0f

    //滚动总高度y值
    var totalScrollY = 0f

    //初始化默认选中项
    var initPosition = 0
        private set

    //选中的Item是第几个
    private var selectedItem = 0
    var preCurrentIndex = 0

    //滚动偏移值,用于记录滚动了多少个item
    var change = 0

    // 绘制几个条目，实际上第一项和最后一项Y轴压缩成0%了，所以可见的数目实际为9
    var itemsVisible = 11

    // WheelView 控件高度
    private var mMeasuredHeight = 0

    // WheelView 控件宽度
    private var mMeasuredWidth = 0

    // 半圆周长
    var halfCircumference = 0

    // 半径
    var radius = 0
    private var mOffset = 0
    private var previousY = 0f
    var startTime: Long = 0
    var widthMeasureSpec = 0
    private var mGravity = Gravity.CENTER
    private var drawCenterContentStart = 0 //中间选中文字开始绘制位置
    private var drawOutContentStart = 0 //非中间文字开始绘制位置
    private var CENTERCONTENTOFFSET //偏移量
            = 0f
    private val DEFAULT_TEXT_TARGET_SKEWX = 0.5f

    init {
        val dm = resources.displayMetrics
        val density = dm.density // 屏幕密度比（0.75/1.0/1.5/2.0/3.0）
        if (density < 1) { //根据密度不同进行适配
            CENTERCONTENTOFFSET = 2.4f
        } else if (1 <= density && density < 2) {
            CENTERCONTENTOFFSET = 3.6f
        } /* else if (1 <= density && density < 2) {
            CENTERCONTENTOFFSET = 4.5F;
        } */ else if (2 <= density && density < 3) {
            CENTERCONTENTOFFSET = 6.0f
        } else if (density >= 3) {
            CENTERCONTENTOFFSET = density * 2.5f
        }
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.pickerview, 0, 0).let { ta ->
                val indexCount = ta.indexCount
                for (index in 0 until indexCount) {
                    when (val attr = ta.getIndex(index)) {
                        R.styleable.pickerview_pv_gravity -> {
                            mGravity = ta.getInt(attr, Gravity.CENTER)
                        }
                        R.styleable.pickerview_pv_textColorOut -> {
                            textColorOut = ta.getColor(attr, textColorOut)
                        }
                        R.styleable.pickerview_pv_textColorCenter -> {
                            textColorCenter = ta.getColor(attr, textColorCenter)
                        }
                        R.styleable.pickerview_pv_dividerColor -> {
                            dividerColor = ta.getColor(attr, dividerColor)
                        }
                        R.styleable.pickerview_pv_textSize -> {
                            textSize = ta.getDimensionPixelOffset(attr, textSize.toInt()).toFloat()
                        }
                        R.styleable.pickerview_pv_lineSpacingMultiplier -> {
                            lineSpacingMultiplier = ta.getFloat(attr, lineSpacingMultiplier)
                        }
                    }
                }

                ta.recycle()
            }
        }
        judgeLineSpace()
        initLoopView(context)
    }

    /**
     * 判断间距是否在1.0-2.0之间
     */
    private fun judgeLineSpace() {
        if (lineSpacingMultiplier < 1.2f) {
            lineSpacingMultiplier = 1.2f
        } else if (lineSpacingMultiplier > 2.0f) {
            lineSpacingMultiplier = 2.0f
        }
    }

    private fun initLoopView(context: Context) {
        messageHandler = MessageHandler(this)
        gestureDetector = GestureDetector(context, LoopViewGestureListener(this))
        gestureDetector!!.setIsLongpressEnabled(false)
        isLoop = true
        totalScrollY = 0f
        initPosition = -1
        initPaints()
    }

    private fun initPaints() {
        paintOuterText = Paint()
        paintOuterText!!.color = textColorOut
        paintOuterText!!.isAntiAlias = true
        paintOuterText!!.typeface = typeface
        paintOuterText!!.textSize = textSize.toFloat()
        paintCenterText = Paint()
        paintCenterText!!.color = textColorCenter
        paintCenterText!!.isAntiAlias = true
        paintCenterText!!.textScaleX = 1.1f
        paintCenterText!!.typeface = typeface
        paintCenterText!!.textSize = textSize
        paintIndicator = Paint()
        paintIndicator!!.color = dividerColor
        paintIndicator!!.isAntiAlias = true
        paintIndicator!!.style = Paint.Style.FILL
        paintIndicator!!.strokeWidth = 4f
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    private fun remeasure() { //重新测量
        if (adapter == null) {
            return
        }
        measureTextWidthHeight()

        //半圆的周长 = item高度乘以item数目-1
        halfCircumference = (itemHeight * (itemsVisible - 1)).toInt()
        //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
        mMeasuredHeight = (halfCircumference * 2 / Math.PI).toInt()
        //求出半径
        radius = (halfCircumference / Math.PI).toInt()
        //控件宽度，这里支持weight
        mMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        //计算两条横线 和 选中项画笔的基线Y位置
        firstLineY = (mMeasuredHeight - itemHeight) / 2.0f
        secondLineY = (mMeasuredHeight + itemHeight) / 2.0f
        centerY = secondLineY - (itemHeight - maxTextHeight) / 2.0f - CENTERCONTENTOFFSET

        //初始化显示的item的position
        if (initPosition == -1) {
            initPosition = if (isLoop) {
                (adapter!!.itemsCount + 1) / 2
            } else {
                0
            }
        }
        preCurrentIndex = initPosition
    }

    /**
     * 计算最大length的Text的宽高度
     */
    private fun measureTextWidthHeight() {
        val rect = Rect()
        for (i in 0 until adapter!!.itemsCount) {
            val s1 = getContentText(adapter!!.getItem(i))
            paintCenterText!!.getTextBounds(s1, 0, s1.length, rect)
            val textWidth = rect.width()
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth
            }
            paintCenterText!!.getTextBounds("\u661F\u671F", 0, 2, rect) // 星期的字符编码（以它为标准高度）
            maxTextHeight = rect.height() + 2
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight + 30
    }

    fun smoothScroll(action: ACTION) { //平滑滚动的实现
        cancelFuture()
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            mOffset = ((totalScrollY % itemHeight + itemHeight) % itemHeight).toInt()
            mOffset = if (mOffset.toFloat() > itemHeight / 2.0f) { //如果超过Item高度的一半，滚动到下一个Item去
                (itemHeight - mOffset.toFloat()).toInt()
            } else {
                -mOffset
            }
        }
        //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
        mFuture = mExecutor.scheduleWithFixedDelay(
            SmoothScrollTimerTask(this, mOffset),
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    fun scrollBy(velocityY: Float) { //滚动惯性的实现
        cancelFuture()
        mFuture = mExecutor.scheduleWithFixedDelay(
            InertiaTimerTask(this, velocityY),
            0,
            VELOCITYFLING.toLong(),
            TimeUnit.MILLISECONDS
        )
    }

    fun cancelFuture() {
        val future = mFuture
        if (future != null && !future.isCancelled) {
            future.cancel(true)
            mFuture = null
        }
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    fun setCyclic(cyclic: Boolean) {
        isLoop = cyclic
    }

    fun setTypeface(font: Typeface) {
        typeface = font
        paintOuterText!!.typeface = typeface
        paintCenterText!!.typeface = typeface
    }

    fun setTextSize(size: Float) {
        if (size > 0.0f) {
            textSize = (context!!.resources.displayMetrics.density * size).toInt().toFloat()
            paintOuterText!!.textSize = textSize
            paintCenterText!!.textSize = textSize
        }
    }

    fun setAdapter(adapter: WheelAdapter<Any>?) {
        this.adapter = adapter
        remeasure()
        invalidate()
    }

    fun getAdapter(): WheelAdapter<*>? {
        return adapter
    }

    //不添加这句,当这个wheelview不可见时,默认都是0,会导致获取到的时间错误
    //回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
    var currentItem: Int
        get() = selectedItem
        set(currentItem) {
            //不添加这句,当这个wheelview不可见时,默认都是0,会导致获取到的时间错误
            selectedItem = currentItem
            initPosition = currentItem
            totalScrollY = 0f //回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
            invalidate()
        }

    fun onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(OnItemSelectedRunnable(this), 200L)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (adapter == null) {
            return
        }
        //initPosition越界会造成preCurrentIndex的值不正确
        if (initPosition < 0) {
            initPosition = 0
        }
        if (initPosition >= adapter!!.itemsCount) {
            initPosition = adapter!!.itemsCount - 1
        }
        //可见的item数组
        val visibles = arrayOfNulls<Any>(itemsVisible)
        //滚动的Y值高度除去每行Item的高度，得到滚动了多少个item，即change数
        change = (totalScrollY / itemHeight).toInt()
        try {
            //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
            preCurrentIndex = initPosition + change % adapter!!.itemsCount
        } catch (e: ArithmeticException) {
            logger_e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配")
        }
        if (!isLoop) { //不循环的情况
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0
            }
            if (preCurrentIndex > adapter!!.itemsCount - 1) {
                preCurrentIndex = adapter!!.itemsCount - 1
            }
        } else { //循环
            if (preCurrentIndex < 0) { //举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
                preCurrentIndex = adapter!!.itemsCount + preCurrentIndex
            }
            if (preCurrentIndex > adapter!!.itemsCount - 1) { //同理上面,自己脑补一下
                preCurrentIndex = preCurrentIndex - adapter!!.itemsCount
            }
        }
        //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        val itemHeightOffset = totalScrollY % itemHeight

        // 设置数组中每个元素的值
        var counter = 0
        while (counter < itemsVisible) {
            var index =
                preCurrentIndex - (itemsVisible / 2 - counter) //索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值
            //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
            if (isLoop) {
                index = getLoopMappingIndex(index)
                visibles[counter] = adapter?.getItem(index)
            } else if (index < 0) {
                visibles[counter] = ""
            } else if (index > adapter!!.itemsCount - 1) {
                visibles[counter] = ""
            } else {
                visibles[counter] = adapter!!.getItem(index)
            }
            counter++
        }

        //绘制中间两条横线
        if (dividerType == DividerType.WRAP) { //横线长度仅包裹内容
            var startX: Float
            val endX: Float
            startX = if (TextUtils.isEmpty(label)) { //隐藏Label的情况
                (measuredWidth - maxTextWidth) / 2f - 12
            } else {
                (measuredWidth - maxTextWidth) / 4f - 12
            }
            if (startX <= 0) { //如果超过了WheelView的边缘
                startX = 10f
            }
            endX = measuredWidth - startX
            canvas.drawLine(startX, firstLineY, endX, firstLineY, paintIndicator!!)
            canvas.drawLine(startX, secondLineY, endX, secondLineY, paintIndicator!!)
        } else if (dividerType == DividerType.FILL) {
            canvas.drawLine(
                0.0f, firstLineY, measuredWidth.toFloat(), firstLineY,
                paintIndicator!!
            )
            canvas.drawLine(
                0.0f, secondLineY, measuredWidth.toFloat(), secondLineY,
                paintIndicator!!
            )
        } //NONE 不做操作，不画分割线


        //只显示选中项Label文字的模式，并且Label文字不为空，则进行绘制
        if (!TextUtils.isEmpty(label) && isCenterLabel) {
            //绘制文字，靠右并留出空隙
            val drawRightContentStart = measuredWidth - getTextWidth(paintCenterText, label)
            canvas.drawText(
                label!!, drawRightContentStart - CENTERCONTENTOFFSET, centerY,
                paintCenterText!!
            )
        }
        counter = 0
        while (counter < itemsVisible) {
            canvas.save()
            // 弧长 L = itemHeight * counter - itemHeightOffset
            // 求弧度 α = L / r  (弧长/半径) [0,π]
            val radian = ((itemHeight * counter - itemHeightOffset) / radius).toDouble()
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            // angle [-90°,90°]
            val angle = (90.0 - radian / Math.PI * 180.0).toFloat() //item第一项,从90度开始，逐渐递减到 -90度

            // 计算取值可能有细微偏差，保证负90°到90°以外的不绘制
            if (angle >= 90f || angle <= -90f) {
                canvas.restore()
            } else {
                // 根据当前角度计算出偏差系数，用以在绘制时控制文字的 水平移动 透明度 倾斜程度
                val offsetCoefficient = Math.pow((Math.abs(angle) / 90f).toDouble(), 2.2).toFloat()
                //获取内容文字
                var contentText: String

                //如果是label每项都显示的模式，并且item内容不为空、label 也不为空
                contentText = if (!isCenterLabel && !TextUtils.isEmpty(label) && !TextUtils.isEmpty(
                        getContentText(
                            visibles[counter]
                        )
                    )
                ) {
                    getContentText(visibles[counter]) + label
                } else {
                    getContentText(visibles[counter])
                }
                reMeasureTextSize(contentText)
                //计算开始绘制的位置
                measuredCenterContentStart(contentText)
                measuredOutContentStart(contentText)
                val translateY =
                    (radius - Math.cos(radian) * radius - Math.sin(radian) * maxTextHeight / 2.0).toFloat()
                //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0f, translateY)
                //                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), firstLineY - translateY)
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * SCALECONTENT)
                    canvas.drawText(
                        contentText, drawOutContentStart.toFloat(), maxTextHeight.toFloat(),
                        paintOuterText!!
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        firstLineY - translateY,
                        measuredWidth.toFloat(),
                        itemHeight.toInt().toFloat()
                    )
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * 1.0f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - CENTERCONTENTOFFSET,
                        paintCenterText!!
                    )
                    canvas.restore()
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), secondLineY - translateY)
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * 1.0f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - CENTERCONTENTOFFSET,
                        paintCenterText!!
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        secondLineY - translateY,
                        measuredWidth.toFloat(),
                        itemHeight.toInt().toFloat()
                    )
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * SCALECONTENT)
                    canvas.drawText(
                        contentText, drawOutContentStart.toFloat(), maxTextHeight.toFloat(),
                        paintOuterText!!
                    )
                    canvas.restore()
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    //canvas.clipRect(0, 0, measuredWidth,   maxTextHeight);
                    //让文字居中
                    val Y =
                        maxTextHeight - CENTERCONTENTOFFSET //因为圆弧角换算的向下取值，导致角度稍微有点偏差，加上画笔的基线会偏上，因此需要偏移量修正一下
                    canvas.drawText(
                        contentText, drawCenterContentStart.toFloat(), Y,
                        paintCenterText!!
                    )
                    val preSelectedItem = adapter?.indexOf(visibles[counter]) ?: return
                    selectedItem = preSelectedItem
                } else {
                    // 其他条目
                    canvas.save()
                    canvas.clipRect(0, 0, measuredWidth, itemHeight.toInt())
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * SCALECONTENT)
                    // 控制文字倾斜角度
                    paintOuterText!!.textSkewX =
                        (if (textXOffset == 0) 0 else if (textXOffset > 0) 1 else -1) * (if (angle > 0) -1 else 1) * DEFAULT_TEXT_TARGET_SKEWX * offsetCoefficient
                    // 控制透明度
                    paintOuterText!!.alpha = ((1 - offsetCoefficient) * 255).toInt()
                    // 控制文字水平便宜距离
                    canvas.drawText(
                        contentText,
                        drawOutContentStart + textXOffset * offsetCoefficient,
                        maxTextHeight.toFloat(),
                        paintOuterText!!
                    )
                    canvas.restore()
                }
                canvas.restore()
                paintCenterText!!.textSize = textSize
            }
            counter++
        }
    }

    /**
     * 根据文字的长度 重新设置文字的大小 让其能完全显示
     *
     * @param contentText
     */
    private fun reMeasureTextSize(contentText: String) {
        val rect = Rect()
        paintCenterText!!.getTextBounds(contentText, 0, contentText.length, rect)
        var width = rect.width()
        var size = textSize.toInt()
        while (width > measuredWidth) {
            size--
            //设置2条横线中间的文字大小
            paintCenterText!!.textSize = size.toFloat()
            paintCenterText!!.getTextBounds(contentText, 0, contentText.length, rect)
            width = rect.width()
        }
        //设置2条横线外面的文字大小
        paintOuterText!!.textSize = size.toFloat()
    }

    //递归计算出对应的index
    private fun getLoopMappingIndex(index: Int): Int {
        var index = index
        if (index < 0) {
            index += (adapter?.itemsCount ?: 0)
            index = getLoopMappingIndex(index)
        } else if (index > adapter!!.itemsCount - 1) {
            index = index - adapter!!.itemsCount
            index = getLoopMappingIndex(index)
        }
        return index
    }

    /**
     * 根据传进来的对象获取getPickerViewText()方法，来获取需要显示的值
     *
     * @param item 数据源的item
     * @return 对应显示的字符串
     */
    private fun getContentText(item: Any?): String {
        if (item == null) {
            return ""
        } else if (item is IPickerViewData) {
            return (item as IPickerViewData).getPickerViewText()
        } else if (item is Int) {
            //如果为整形则最少保留两位数.
            return String.format(Locale.getDefault(), "%02d", item)
        }
        return item.toString()
    }

    private fun measuredCenterContentStart(content: String) {
        val rect = Rect()
        paintCenterText!!.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawCenterContentStart =
                if (isOptions || label == null || label == "" || !isCenterLabel) {
                    ((measuredWidth - rect.width()) * 0.5).toInt()
                } else { //只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
                    ((measuredWidth - rect.width()) * 0.25).toInt()
                }
            Gravity.LEFT -> drawCenterContentStart = 0
            Gravity.RIGHT -> drawCenterContentStart =
                measuredWidth - rect.width() - CENTERCONTENTOFFSET.toInt()
        }
    }

    private fun measuredOutContentStart(content: String) {
        val rect = Rect()
        paintOuterText!!.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawOutContentStart =
                if (isOptions || label == null || label == "" || !isCenterLabel) {
                    ((measuredWidth - rect.width()) * 0.5).toInt()
                } else { //只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
                    ((measuredWidth - rect.width()) * 0.25).toInt()
                }
            Gravity.LEFT -> drawOutContentStart = 0
            Gravity.RIGHT -> drawOutContentStart =
                measuredWidth - rect.width() - CENTERCONTENTOFFSET.toInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.widthMeasureSpec = widthMeasureSpec
        remeasure()
        setMeasuredDimension(measuredWidth, mMeasuredHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventConsumed = gestureDetector!!.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                cancelFuture()
                previousY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = previousY - event.rawY
                previousY = event.rawY
                totalScrollY = totalScrollY + dy

                // 边界处理。
                if (!isLoop) {
                    var top = -initPosition * itemHeight
                    var bottom = (adapter!!.itemsCount - 1 - initPosition) * itemHeight
                    if (totalScrollY - itemHeight * 0.25 < top) {
                        top = totalScrollY - dy
                    } else if (totalScrollY + itemHeight * 0.25 > bottom) {
                        bottom = totalScrollY - dy
                    }
                    if (totalScrollY < top) {
                        totalScrollY = top.toInt().toFloat()
                    } else if (totalScrollY > bottom) {
                        totalScrollY = bottom.toInt().toFloat()
                    }
                }
            }
            MotionEvent.ACTION_UP -> if (!eventConsumed) { //未消费掉事件
                /**
                 * TODO<关于弧长的计算>
                 *
                 * 弧长公式： L = α*R
                 * 反余弦公式：arccos(cosα) = α
                 * 由于之前是有顺时针偏移90度，
                 * 所以实际弧度范围α2的值 ：α2 = π/2-α    （α=[0,π] α2 = [-π/2,π/2]）
                 * 根据正弦余弦转换公式 cosα = sin(π/2-α)
                 * 代入，得： cosα = sin(π/2-α) = sinα2 = (R - y) / R
                 * 所以弧长 L = arccos(cosα)*R = arccos((R - y) / R)*R
                </关于弧长的计算> */
                val y = event.y
                val L = Math.acos(((radius - y) / radius).toDouble()) * radius
                //item0 有一半是在不可见区域，所以需要加上 itemHeight / 2
                val circlePosition = ((L + itemHeight / 2) / itemHeight).toInt()
                val extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight
                //已滑动的弧长值
                mOffset = ((circlePosition - itemsVisible / 2f) * itemHeight - extraOffset).toInt()
                if (System.currentTimeMillis() - startTime > 120) {
                    // 处理拖拽事件
                    smoothScroll(ACTION.DAGGLE)
                } else {
                    // 处理条目点击事件
//                        smoothScroll(ACTION.CLICK);
                }
            }
            else -> if (!eventConsumed) {
                val y = event.y
                val L = Math.acos(((radius - y) / radius).toDouble()) * radius
                val circlePosition = ((L + itemHeight / 2) / itemHeight).toInt()
                val extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight
                mOffset = ((circlePosition - itemsVisible / 2f) * itemHeight - extraOffset).toInt()
                if (System.currentTimeMillis() - startTime > 120) {
                    smoothScroll(ACTION.DAGGLE)
                } else {
                }
            }
        }
        invalidate()
        return true
    }

    /**
     * 获取Item个数
     *
     * @return item个数
     */
    val itemsCount: Int
        get() = if (adapter != null) adapter!!.itemsCount else 0

    /**
     * 附加在右边的单位字符串
     *
     * @param label 单位
     */
    fun setLabel(label: String?) {
        this.label = label
    }

    fun isCenterLabel(isCenterLabel: Boolean) {
        this.isCenterLabel = isCenterLabel
    }

    fun setGravity(gravity: Int) {
        mGravity = gravity
    }

    fun getTextWidth(paint: Paint?, str: String?): Int { //计算文字宽度
        var iRet = 0
        if (str != null && str.length > 0) {
            val len = str.length
            val widths = FloatArray(len)
            paint!!.getTextWidths(str, widths)
            for (j in 0 until len) {
                iRet += Math.ceil(widths[j].toDouble()).toInt()
            }
        }
        return iRet
    }

    fun setIsOptions(options: Boolean) {
        isOptions = options
    }

    fun setTextColorOut(textColorOut: Int) {
        if (textColorOut != 0) {
            this.textColorOut = textColorOut
            paintOuterText!!.color = this.textColorOut
        }
    }

    fun setTextColorCenter(textColorCenter: Int) {
        if (textColorCenter != 0) {
            this.textColorCenter = textColorCenter
            paintCenterText!!.color = this.textColorCenter
        }
    }

    fun setTextXOffset(textXOffset: Int) {
        this.textXOffset = textXOffset
        if (textXOffset != 0) {
            paintCenterText!!.textScaleX = 1.0f
        }
    }

    fun setDividerColor(dividerColor: Int) {
        if (dividerColor != 0) {
            this.dividerColor = dividerColor
            paintIndicator!!.color = this.dividerColor
        }
    }

    fun setDividerType(dividerType: DividerType?) {
        this.dividerType = dividerType
    }

    fun setLineSpacingMultiplier(lineSpacingMultiplier: Float) {
        if (lineSpacingMultiplier != 0f) {
            this.lineSpacingMultiplier = lineSpacingMultiplier
            judgeLineSpace()
        }
    }

    companion object {
        // 修改这个值可以改变滑行速度
        private const val VELOCITYFLING = 5
        private const val SCALECONTENT = 0.8f //非中间文字则用此控制高度，压扁形成3d错觉
    }
}