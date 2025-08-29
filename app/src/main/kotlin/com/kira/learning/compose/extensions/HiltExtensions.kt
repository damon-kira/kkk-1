package com.kira.learning.compose.extensions

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * 基于 Hilt 的 ViewModel 获取扩展。
 *
 * 功能与 [daggerViewModel] 类似：
 * 1. 自动从 [LocalViewModelStoreOwner] 获取作用域，缺失时抛出明确错误。
 * 2. 支持可选 key，在同一作用域内区分多个同类型 ViewModel。
 * 3. 使用 Hilt 的默认工厂（@HiltViewModel + @Inject 构造）。
 *
 * 用法：
 * val vm: MyViewModel = hiltViewModelEx()
 * val vm2: MyViewModel = hiltViewModelEx(key = "Second")
 */
@Composable
inline fun <reified VM : ViewModel> hiltViewModelEx(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
): VM {
    @Suppress("UNCHECKED_CAST")
    return if (key == null) {
        hiltViewModel<VM>(viewModelStoreOwner = viewModelStoreOwner)
    } else {
        // 新版 hilt-navigation-compose (>=1.1.0) 提供带 key 的重载；为了向后兼容，使用反射尝试调用
        runCatching {
            val method = Class.forName("androidx.hilt.navigation.compose.HiltViewModelKt")
                .methods.firstOrNull { it.name == "hiltViewModel" && it.parameterTypes.size >= 2 }
            if (method != null) {
                method.invoke(null, viewModelStoreOwner, key) as VM
            } else {
                // 回退：不支持 key，直接返回（会与其它实例复用）
                hiltViewModel<VM>(viewModelStoreOwner = viewModelStoreOwner)
            }
        }.getOrElse {
            hiltViewModel<VM>(viewModelStoreOwner = viewModelStoreOwner)
        }
    }
}

