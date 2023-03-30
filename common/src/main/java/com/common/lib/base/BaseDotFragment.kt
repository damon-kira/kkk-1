package com.common.lib.base

import androidx.annotation.CallSuper
//import com.behavior.lib.EventAgent

/**
 * Created by weisl on 2019/10/15.
 * tuyaqifei*fjdkjfdk
 */
abstract class BaseDotFragment : BaseFragment() {


    abstract fun getPageNumber(): String //页面编号

    abstract fun getEntryEvent(): String //进入页面事件

    abstract fun getStayEvent(): String //页面停留时长

    abstract fun getBackEvent(): String //返回

    /** 点击事件 */
    protected fun dotEvent(eventId: String) {
//        EventAgent.onEvent(getPageNumber(), eventId)
    }

    /**
     * 页面停留时长打点
     */
    protected fun dotStayEvent() {
//        EventAgent.onStop(getPageNumber(), getStayEvent())
    }

    /**
     * 进入页面打点
     */
    protected fun dotEntryEvent() {
//        EventAgent.onStart(getPageNumber(), getEntryEvent())
    }

    /**
     * 页面返回打点
     */
    protected fun dotBackEvent() {
        dotEvent(getBackEvent())
    }


    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (!isNeedPageDot()) return
        if (visible) {
            dotEntryEvent()
        } else {
            dotStayEvent()
        }
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        if (isNeedPageDot()) {
            dotStayEvent()
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        dotBackEvent()
        return super.onFragmentBackPressed()
    }

    /**
     * 不带打点的返回
     */
    open fun onBackPressedWithoutDot(): Boolean {
        return super.onFragmentBackPressed()
    }

    /** 是否需要页面打点 */
    open fun isNeedPageDot(): Boolean = true
}