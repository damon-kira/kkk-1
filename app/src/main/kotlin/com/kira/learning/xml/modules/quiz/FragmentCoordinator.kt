package com.kira.learning.xml.modules.quiz

import android.os.Bundle
import androidx.fragment.app.FragmentManager

class FragmentCoordinator(
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) {
    private val fragments = mutableMapOf<String, BaseFunctionFragment>()
    private val results = mutableMapOf<String, Bundle>()

    // 注册功能模块
    fun registerModule(fragment: BaseFunctionFragment) {
        fragments[fragment.moduleId] = fragment
        fragment.resultListener = object : BaseFunctionFragment.ResultListener {
            override fun onPartialResult(moduleId: String, result: Bundle) {
                results[moduleId] = result
                checkAllResultsReady()
            }

            override fun onError(moduleId: String, error: Throwable) {
                // 错误处理逻辑
            }
        }

        fragmentManager.beginTransaction()
            .add(containerId, fragment, fragment.moduleId)
//            .hide(fragment)
            .commit()
    }

    // 显示指定模块
    fun showModule(moduleId: String) {
        fragments.values.forEach { fragment ->
            if (fragment.moduleId == moduleId) {
                fragmentManager.beginTransaction()
                    .show(fragment)
                    .commit()
            } else {
                fragmentManager.beginTransaction()
                    .hide(fragment)
                    .commit()
            }
        }
    }

    // 模块间通信
    fun sendMessage(senderId: String, receiverId: String, message: Bundle) {
        fragments[receiverId]?.onMessageReceived(senderId, message)
    }

    // 收集所有结果
    fun collectAllResults(): Map<String, Bundle> {
        fragments.values.forEach { fragment ->
            results[fragment.moduleId] = fragment.collectResult()
        }
        return results.toMap()
    }

    fun getAllFragments(): Map<String, BaseFunctionFragment> {
        return fragments.toMap() // toMap() 创建一个新的只读 Map 副本
    }

    private fun checkAllResultsReady() {
        if (results.size == fragments.size) {
            // 所有结果就绪，触发最终处理
            onAllResultsReady(results.toMap())
        }
    }

    var onAllResultsReady: (Map<String, Bundle>) -> Unit = {}

    fun clear() {
        fragments.clear()
//        results.clear()
        fragments.forEach {
            fragmentManager.beginTransaction()
                .remove(it.value)
                .commit()
        }
    }
}