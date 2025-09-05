package com.kira.learning.base.mvi

import androidx.compose.runtime.Immutable

/**
 * 通用UI状态基类
 * 适用于大部分需要加载数据的场景
 */
@Immutable
sealed interface BaseUiState<out T> {
    object Idle : BaseUiState<Nothing>
    object Loading : BaseUiState<Nothing>
    data class Error(val message: String) : BaseUiState<Nothing>
    data class Success<T>(
        val data: T,
        val refreshing: Boolean = false
    ) : BaseUiState<T>
}

/**
 * 扩展函数：获取数据
 */
val <T> BaseUiState<T>.data: T?
    get() = when (this) {
        is BaseUiState.Success -> data
        else -> null
    }

/**
 * 扩展函数：是否正在加载
 */
val <T> BaseUiState<T>.isLoading: Boolean
    get() = this is BaseUiState.Loading

/**
 * 扩展函数：是否有错误
 */
val <T> BaseUiState<T>.isError: Boolean
    get() = this is BaseUiState.Error

/**
 * 扩展函数：是否成功
 */
val <T> BaseUiState<T>.isSuccess: Boolean
    get() = this is BaseUiState.Success

/**
 * 扩展函数：获取错误信息
 */
val <T> BaseUiState<T>.errorMessage: String?
    get() = when (this) {
        is BaseUiState.Error -> message
        else -> null
    }
