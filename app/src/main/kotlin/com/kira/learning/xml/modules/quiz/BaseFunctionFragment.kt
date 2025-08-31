package com.kira.learning.xml.modules.quiz

import android.os.Bundle
import com.common.lib.base.BaseFragment

abstract class BaseFunctionFragment : BaseFragment() {
    
    // 结果回调接口
    interface ResultListener {
        fun onPartialResult(moduleId: String, result: Bundle)
        fun onError(moduleId: String, error: Throwable)
    }
    
    // 模块唯一标识
    abstract val moduleId: String
    abstract val moduleName: String

    // 结果监听器
    var resultListener: ResultListener? = null
    
    // 收集计算结果
    abstract fun collectResult(): Bundle
    
    // 模块间通信
    open fun onMessageReceived(senderId: String, message: Bundle) {}
    
    // 生命周期扩展
    open fun onVisible() {}
    open fun onHidden() {}
    
    override fun onResume() {
        super.onResume()
        if (isVisible) onVisible()
    }
    
//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if (hidden) onHidden() else onVisible()
//    }
}