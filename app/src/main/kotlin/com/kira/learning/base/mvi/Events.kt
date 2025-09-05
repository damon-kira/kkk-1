package com.kira.learning.base.mvi

/**
 * 通用事件基类
 */
interface ViewEvent

/**
 * 通用状态基类
 */
abstract class ViewState

/**
 * 通用用户操作事件
 */
sealed interface UserAction {
    object Refresh : UserAction
    object Retry : UserAction
    object Load : UserAction
    data class Navigate(val route: String) : UserAction
    data class ShowMessage(val message: String) : UserAction
    data class ShowError(val error: String) : UserAction
}

/**
 * 通用UI事件
 */
sealed interface UiEvent : ViewEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class ShowDialog(val title: String, val message: String) : UiEvent
    data class Navigate(val route: String) : UiEvent
    object NavigateBack : UiEvent
}

/**
 * 通用数据操作事件
 */
sealed interface DataEvent : ViewEvent {
    object Load : DataEvent
    object Refresh : DataEvent
    data class Retry(val reason: String? = null) : DataEvent
    data class Search(val query: String) : DataEvent
    data class Filter(val filter: Any) : DataEvent
}
