package com.colombia.credit.popwindow

import android.app.Activity
import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.util.lib.DisplayUtils
import com.util.lib.dip2px
import com.util.lib.dp
import com.util.lib.log.isDebug

class MsgListPopWindow(private val context: Context) : PopAdapter.onItemClickListener,
    LifecycleEventObserver {

    protected var mPopWindow: PopupWindow = PopupWindow(context)
    protected var callback: OnItemClickCallback? = null
    protected var callbackWithData: OnItemClickCallbackWithData? = null
    private var popRecycler: RecyclerView? = null
    private var view: View? = null
    private var mMaxWidth: Int = 0

    private var mMargin = 0

    init {
        view = LayoutInflater.from(context).inflate(R.layout.pop_view, null, false)
        mPopWindow.setBackgroundDrawable(
            ContextCompat.getDrawable(context, R.color.transparent)
        )
        mPopWindow.isOutsideTouchable = true
        mPopWindow.isTouchable = true
        popRecycler = view?.findViewById(R.id.pop_recycleview) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        popRecycler?.layoutManager = linearLayoutManager
        mPopWindow.contentView = view

        val ctx = context
        if (ctx is LifecycleOwner) {
            ctx.lifecycle.addObserver(this)
        }
        mMargin = dip2px(context, 8f)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_STOP) {
            dismissPopWindow()
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            dismissPopWindow()
        }
    }

    fun setOutsideTouchable(outTouchable: Boolean): MsgListPopWindow {
        mPopWindow.isOutsideTouchable = outTouchable
        return this
    }

    fun setTouchable(touchable: Boolean): MsgListPopWindow {
        mPopWindow.isTouchable = touchable
        return this
    }

    fun setFocusable(isFocusable: Boolean): MsgListPopWindow {
        mPopWindow.isFocusable = isFocusable
            return this
    }

    /**
     * 最大宽度
     */
    fun setMaxWidth(maxWidth: Int) {
        // 加上内边距margin的宽度
        val margin = dip2px(context, 8f)
        this.mMaxWidth = maxWidth + margin * 2
        mPopWindow.width = mMaxWidth
    }

    fun setData(popArray: List<String>, type: Int, targetview: View, content: CharSequence) {
        val dataList = ArrayList<PopData>()
        popArray.forEach {
            val popdata = PopData()
            popdata.selectValues = it
            popdata.isSelected = false
            dataList.add(popdata)
        }
        setPopData(dataList, type, targetview, content)
    }

    fun setPopData(popArray: List<PopData>, type: Int, targetview: View, content: CharSequence) {
        initData(popArray, type, targetview)
        showPopWindow(targetview, content)
    }

    fun setPopData(dataMap: Map<Int, String>, type: Int, targetview: View, content: CharSequence) {
        initData(mapToPopData(dataMap), type, targetview)
        showPopWindow(targetview, content)
    }

    fun setPopDataStr(
        dataMap: Map<String, String>,
        type: Int,
        targetview: View,
        content: CharSequence
    ) {
        initData(mapStrToPopData(dataMap), type, targetview)
        showPopWindow(targetview, content)
    }

    private fun mapStrToPopData(dataMap: Map<String, String>): List<PopData> {
        val list = mutableListOf<PopData>()
        var popData: PopData
        dataMap.forEach {
            popData = PopData()
            popData.isSelected = false
            popData.keyStr = it.key
            popData.selectValues = it.value
            list.add(popData)
        }
        return list
    }

    fun initData(dataList: List<PopData>, type: Int, targetView: View) {
        var adapter = popRecycler?.adapter
        if (adapter != null && adapter is PopAdapter) {
            adapter.setDataList(dataList)
            adapter.setCurrType(type)
            measureView()
        } else {
            val viewLocation = IntArray(2)
            targetView.getLocationInWindow(viewLocation)
            adapter = PopAdapter(dataList, type, getListItemPaddingLeft(viewLocation, targetView))
            adapter.setItemListener(this)
            popRecycler?.adapter = adapter
            measureView()
        }
    }

    private fun measureView() {
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view?.measure(width, height)
        val measuredHeight = (view?.measuredHeight ?: 0) + 10f.dp()
        val screenHeight = DisplayUtils.getScreenWH(context)[1]
        mPopWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopWindow.height =
            if (measuredHeight > screenHeight) ViewGroup.LayoutParams.WRAP_CONTENT else measuredHeight
    }

    fun showPopWindow(targetview: View, content: CharSequence) {
        if (!mPopWindow.isShowing) {
            mPopWindow.isFocusable = true
            val shadowOffset = (-8f).dp()//阴影偏移量
            val viewLocation = IntArray(2)
            targetview.getLocationInWindow(viewLocation)//控件位于window的位置[0]为x,[1]为y
//            mPopContent?.run {
//                setPadding(
//                    getListItemPaddingLeft(viewLocation, targetview),
//                    paddingTop,
//                    paddingRight,
//                    paddingBottom
//                )
//                text = content
//            }
            val remainingHeight =
                DisplayUtils.getScreenWH(context)[1] - (viewLocation[1] + targetview.height)//控件以下的高度（不含控件)
            val usableMaxHeight =
                Math.max(
                    remainingHeight,
                    viewLocation[1] + targetview.height
                )//可供弹窗的最大高度，控件以下（含控件），控件以上（含控件）的最大值
            popRecycler?.overScrollMode =
                if (mPopWindow.height > usableMaxHeight) View.OVER_SCROLL_ALWAYS else View.OVER_SCROLL_NEVER//是否显示滚动条

            mPopWindow.width = if (mMaxWidth <= 0) {
                getPopWindowWidth(viewLocation[0], targetview, shadowOffset)
            } else {
                mMaxWidth
            }
            mPopWindow.height = getPopHeight(usableMaxHeight)//修正弹窗大小
//            if (remainingHeight >= mPopWindow.height) {//控件以下（含控件）满足弹窗大小，即向下弹窗
//                popLayout.removeView(mPopTopView)
//                popLayout.removeView(mDivider)
//                popLayout.addView(mDivider, 0)
//                popLayout.addView(mPopTopView, 0)
//            } else {//控件以上（含控件）满足弹窗大小，即向上弹窗
//                popLayout.removeView(mPopTopView)
//                popLayout.removeView(mDivider)
//                popLayout.addView(mDivider, popLayout.childCount)
//                popLayout.addView(mPopTopView, popLayout.childCount)
//            }
            //从状态栏往下弹
            showAtLocation(
                targetview,
                getOffsetX(viewLocation, targetview, shadowOffset),
                getOffsetY(remainingHeight, targetview, shadowOffset)
            )
        } else {
            mPopWindow.dismiss()
        }
    }

    private fun getPopHeight(usableMaxHeigth: Int): Int {
        val height = mPopWindow.height
        return if (height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            Math.min(usableMaxHeigth, height)
        } else {
            usableMaxHeigth
        }
    }

    protected open fun showAtLocation(targetView: View, offsetX: Int, offsetY: Int) {
        val isFinishing = (targetView.context as? Activity)?.isFinishing == true
        val isDestroyed = (targetView.context as? Activity)?.isDestroyed == true
        try {
            if (!isFinishing && !isDestroyed && targetView.windowToken != null) {
                mPopWindow.showAtLocation(
                    targetView,
                    Gravity.TOP + Gravity.START,
                    offsetX,
                    offsetY
                )
            }
        } catch (e: WindowManager.BadTokenException) {
            if (isDebug()) {
                throw e
            }
        }
    }

    protected open fun getOffsetX(
        viewLocation: IntArray,
        targetView: View,
        shadowOffset: Int
    ): Int = viewLocation[0] - mMargin

    /**
     * @param remainingHeight 控件下面剩余高度
     * @param targetView
     */
    protected open fun getOffsetY(remainingHeight: Int, targetView: View, shadowOffset: Int): Int {
        val viewLocation = IntArray(2)
        targetView.getLocationInWindow(viewLocation)//控件位于window的位置[0]为x,[1]为y
        return if (remainingHeight >= mPopWindow.height) {
            viewLocation[1] + targetView.height - shadowOffset
        } else {
            viewLocation[1] - mPopWindow.height + shadowOffset
        }
    }

    protected open fun getPopWindowWidth(
        targetViewX: Int,
        targetView: View,
        shadowOffset: Int
    ): Int {
        return targetView.width + mMargin * 2
    }

    protected open fun getListItemPaddingLeft(targetViewLocation: IntArray, targetView: View): Int {
        return targetView.paddingLeft
    }

    override fun onItemClick(popData: PopData?, type: Int, position: Int) {
        callback?.onItemCallback(popData?.selectValues, type, position)
        callbackWithData?.onItemCallback(popData, type, position)
        dismissPopWindow()
    }

    private fun dismissPopWindow() {
        try {
            if (mPopWindow.isShowing) {
                mPopWindow.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    fun setItemCallback(callback: OnItemClickCallback) {
        this.callback = callback
    }

    fun setItemCallbackWithData(callback: OnItemClickCallbackWithData) {
        this.callbackWithData = callback
    }

    interface OnItemClickCallback {
        fun onItemCallback(string: String?, type: Int, position: Int)
    }

    interface OnItemClickCallbackWithData {
        fun onItemCallback(popData: PopData?, type: Int, position: Int)
    }

    fun mapToPopData(dataMap: Map<Int, String>): List<PopData> {
        val list = mutableListOf<PopData>()
        var popData: PopData
        dataMap.forEach {
            popData = PopData()
            popData.isSelected = false
            popData.selectKey = it.key
            popData.selectValues = it.value
            list.add(popData)
        }
        return list
    }

    fun onDestroy() {
        dismissPopWindow()
        if (callback != null) {
            callback = null
        }
        if (callbackWithData != null) {
            callbackWithData = null
        }
    }
}