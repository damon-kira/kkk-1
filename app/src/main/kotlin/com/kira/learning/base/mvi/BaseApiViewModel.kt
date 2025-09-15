package com.kira.learning.base.mvi

import androidx.lifecycle.viewModelScope
import com.kira.learning.network.ApiResult
import com.kira.learning.network.ErrorType
import com.kira.learning.network.GlobalErrorHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 统一的ViewModel基类，支持标准化的API调用和错误处理
 */
abstract class BaseApiViewModel<State : ViewState, Event : ViewEvent>(
    initialState: State
) : BaseViewModel<State, Event>(initialState) {

    /**
     * 执行API调用并自动处理Loading和Error状态
     */
    protected fun <T> executeApiCall(
        apiCall: suspend () -> ApiResult<T>,
        onSuccess: (T) -> Unit,
        onError: ((ApiResult.Error) -> Unit)? = null,
        showLoading: Boolean = true,
        showErrorMessage: Boolean = true
    ) {
        viewModelScope.launch {
            if (showLoading) {
                updateLoadingState(true)
            }

            when (val result = apiCall()) {
                is ApiResult.Success -> {
                    if (showLoading) {
                        updateLoadingState(false)
                    }
                    onSuccess(result.data)
                }

                is ApiResult.Error -> {
                    if (showLoading) {
                        updateLoadingState(false)
                    }

                    // 自定义错误处理
                    if (onError?.invoke(result) != null) {
                        onError.invoke(result)
                    } else {
                        // 默认错误处理
                        handleDefaultError(result, showErrorMessage)
                    }
                }

                is ApiResult.NetworkUnavailable -> {
                    if (showLoading) {
                        updateLoadingState(false)
                    }
                    handleNetworkUnavailable(showErrorMessage)
                }

                is ApiResult.Loading -> {
                    // Loading状态已在开始时设置
                }
            }
        }
    }

    /**
     * 执行Flow API调用并自动处理状态变化
     */
    protected fun <T> executeApiFlow(
        apiFlow: Flow<ApiResult<T>>,
        onSuccess: (T) -> Unit,
        onError: ((ApiResult.Error) -> Unit)? = null,
        onLoading: ((String) -> Unit)? = null,
        showErrorMessage: Boolean = true
    ) {
        viewModelScope.launch {
            apiFlow.collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        onSuccess(result.data)
                    }

                    is ApiResult.Error -> {
                        if (onError?.invoke(result) != null) {
                            onError.invoke(result)
                        } else {
                            handleDefaultError(result, showErrorMessage)
                        }
                    }

                    is ApiResult.Loading -> {
                        onLoading?.invoke(result.message) ?: updateLoadingState(
                            true,
                            result.message
                        )
                    }

                    is ApiResult.NetworkUnavailable -> {
                        handleNetworkUnavailable(showErrorMessage)
                    }
                }
            }
        }
    }

    /**
     * 批量执行多个API调用
     */
    protected fun executeBatchApiCalls(
        vararg apiCalls: suspend () -> ApiResult<*>,
        onAllSuccess: () -> Unit,
        onAnyError: ((List<ApiResult.Error>) -> Unit)? = null,
        showLoading: Boolean = true
    ) {
        viewModelScope.launch {
            if (showLoading) {
                updateLoadingState(true, "处理中...")
            }

            val results = apiCalls.map { it() }
            val errors = results.filterIsInstance<ApiResult.Error>()

            if (showLoading) {
                updateLoadingState(false)
            }

            if (errors.isEmpty()) {
                onAllSuccess()
            } else {
                onAnyError?.invoke(errors) ?: errors.forEach { error ->
                    handleDefaultError(error, true)
                }
            }
        }
    }

    /**
     * 默认错误处理
     */
    private fun handleDefaultError(error: ApiResult.Error, showMessage: Boolean) {
        when (error.errorType) {
            ErrorType.AUTHENTICATION -> {
                if (showMessage) {
                    GlobalErrorHandler.handleError(error, showUserMessage = true)
                }
            }

            ErrorType.AUTHORIZATION -> {
                if (showMessage) {
                    GlobalErrorHandler.handleError(error, showUserMessage = true)
                }
            }

            ErrorType.VALIDATION -> {
                if (showMessage) {
                    GlobalErrorHandler.handleError(error, showUserMessage = true)
                }
            }

            else -> {
                if (showMessage) {
                    GlobalErrorHandler.handleError(error, showUserMessage = true)
                }
            }
        }
    }

    /**
     * 处理网络不可用
     */
    private fun handleNetworkUnavailable(showMessage: Boolean) {
        if (showMessage) {
            GlobalErrorHandler.handleNetworkUnavailable(showUserMessage = true)
        }
    }

    /**
     * 更新Loading状态 - 子类需要实现
     */
    protected abstract fun updateLoadingState(isLoading: Boolean, message: String = "")

    /**
     * 重试机制
     */
    protected fun retryApiCall(
        originalCall: suspend () -> ApiResult<*>,
        maxRetries: Int = 3,
        delayMillis: Long = 1000
    ) {
        viewModelScope.launch {
            repeat(maxRetries) { attempt ->
                val result = originalCall()
                if (result is ApiResult.Success) {
                    return@launch
                }

                if (attempt < maxRetries - 1) {
                    kotlinx.coroutines.delay(delayMillis * (attempt + 1))
                }
            }
        }
    }
}
