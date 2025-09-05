package com.kira.learning.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * 通用ViewModel基类
 * 提供状态管理和事件处理的基础功能
 */
abstract class BaseViewModel<State : ViewState, Event : ViewEvent>(
    initialState: State
) : ViewModel() {

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<State> = _viewState.asStateFlow()

    private val _viewEvent = Channel<Event>(Channel.BUFFERED)
    val viewEvent: Flow<Event> = _viewEvent.receiveAsFlow()

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

    /**
     * 处理用户操作
     */
    abstract fun handleAction(action: Any)
}

/**
 * 通用数据加载ViewModel
 * 适用于需要从Repository加载数据的场景
 */
abstract class BaseDataViewModel<T, State : ViewState, Event : ViewEvent>(
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
            }
        }
    }
}
