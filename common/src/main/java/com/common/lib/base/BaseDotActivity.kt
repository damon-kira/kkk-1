package com.common.lib.base

//import com.behavior.lib.EventAgent
import com.common.lib.base.BaseActivity

/**
 * Created by weisl on 2019/10/15.
 * tuyaqifei*fjdkjfdk
 */
abstract class BaseDotActivity : BaseActivity() {

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
    private fun dotStayEvent() {
//        EventAgent.onStop(getPageNumber(), getStayEvent())
    }

    /**
     * 进入页面打点
     */
    private fun dotEntryEvent() {
//        EventAgent.onStart(getPageNumber(), getEntryEvent())
    }

    /**
     * 页面返回打点
     */
    protected fun dotBackEvent() {
        dotEvent(getBackEvent())
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        dotEntryEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        dotStayEvent()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dotBackEvent()
    }
}