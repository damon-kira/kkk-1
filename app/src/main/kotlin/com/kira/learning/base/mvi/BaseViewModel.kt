package com.kira.learning.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.network.ApiResult
import com.kira.learning.network.ErrorType
import com.kira.learning.network.GlobalErrorHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 统一的ViewModel基类
 * 提供完整的MVI架构支持和标准化的API调用处理
 *
 * 功能包括：
 * - 状态管理和事件处理
 * - 标准化API调用
 * - 统一错误处理
 * - 加载状态管理
 * - 重试机制
 */
abstract class BaseViewModel<State : ViewState, Event : ViewEvent>(
    initialState: State
) : ViewModel() {

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState.asStateFlow()

    private val _viewEvent = Channel<Event>(Channel.BUFFERED)
    val viewEvent: Flow<Event> = _viewEvent.receiveAsFlow()

    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    protected val currentState: State
        get() = _viewState.value

    protected fun updateState(update: State.() -> State) {
        _viewState.value = currentState.update()
    }

    protected fun setState(newState: State) {
        _viewState.value = newState
    }

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _viewEvent.send(event)
        }
    }

    protected fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    /**
     * 处理用户操作 - 子类必须实现
     */
    abstract fun handleAction(action: Event)

    // ===== API调用支持（可选使用） =====

    /**
     * 执行API调用并自动处理Loading和Error状态
     * 这是可选功能，不需要API调用的ViewModel可以不使用
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

                    if (onError != null) {
                        onError.invoke(result)
                    } else {
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
                        if (onError != null) {
                            onError.invoke(result)
                        } else {
                            handleDefaultError(result, showErrorMessage)
                        }
                    }

                    is ApiResult.Loading -> {
                        onLoading?.invoke(result.message) ?: updateLoadingState(true, result.message)
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

    // ===== 错误处理 =====

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

    // ===== 加载状态管理（可选实现） =====

    /**
     * 更新Loading状态
     * 只有需要API调用的ViewModel才需要实现此方法
     * 提供默认空实现，避免强制实现
     */
    protected open fun updateLoadingState(isLoading: Boolean, message: String = "") {
        // 默认空实现 - 子类可以选择性重写
        // 这样不需要API调用的ViewModel就不需要实现这个方法
    }
}

/**
 * 简化的数据加载ViewModel
 * 适用于需要基础数据加载但不需要复杂API处理的场景
 */
abstract class SimpleDataViewModel<T, State : ViewState, Event : ViewEvent>(
    initialState: State
) : BaseViewModel<State, Event>(initialState) {

    /**
     * 加载数据
     */
    abstract suspend fun loadData(refresh: Boolean = false): T

    /**
     * 处理加载状态更新
     */
    protected abstract fun updateLoadingState(loading: Boolean)

    /**
     * 处理成功状态更新
     */
    protected abstract fun updateSuccessState(data: T, refreshing: Boolean = false)

    /**
     * 处理错误状态更新
     */
    protected abstract fun updateErrorState(message: String)

    /**
     * 执行数据加载
     */
    protected fun executeLoad(refresh: Boolean = false) {
        viewModelScope.launch {
            try {
                if (!refresh) {
                    updateLoadingState(true)
                }
                val data = loadData(refresh)
                updateSuccessState(data, refresh)
            } catch (e: Exception) {
                updateErrorState(e.message ?: "加载失败")
            } finally {
                updateLoadingState(false)
            }
        }
    }
}
