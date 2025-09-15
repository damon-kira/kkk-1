package com.kira.learning.network

import com.kira.learning.modules.auth.AuthEventBus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber

/**
 * 全局错误处理器
 * 统一处理应用中的所有错误，提供一致的用户体验
 */
object GlobalErrorHandler {

    private val _errorEvents = Channel<ErrorEvent>(Channel.BUFFERED)
    val errorEvents: Flow<ErrorEvent> = _errorEvents.receiveAsFlow()

    /**
     * 错误事件类型
     */
    sealed class ErrorEvent {
        data class ShowToast(val message: String) : ErrorEvent()
        data class ShowSnackbar(val message: String, val action: String? = null) : ErrorEvent()
        data class ShowDialog(val title: String, val message: String, val action: String? = null) :
            ErrorEvent()

        data class RequireLogin(val message: String = "请先登录") : ErrorEvent()
        data class NavigateToLogin(val message: String = "登录已过期") : ErrorEvent()
    }

    /**
     * 处理API错误结果
     */
    fun handleError(
        error: ApiResult.Error,
        showUserMessage: Boolean = true,
        customHandler: ((ApiResult.Error) -> Boolean)? = null
    ) {
        // 先尝试自定义处理器
        if (customHandler?.invoke(error) == true) {
            return
        }

        // 根据错误类型进行不同处理
        when (error.errorType) {
            ErrorType.AUTHENTICATION -> {
                // 认证错误，需要重新登录
                handleAuthenticationError(error)
            }

            ErrorType.AUTHORIZATION -> {
                // 权限错误
                if (showUserMessage) {
                    _errorEvents.trySend(
                        ErrorEvent.ShowDialog(
                            title = "权限不足",
                            message = error.message,
                            action = "确定"
                        )
                    )
                }
            }

            ErrorType.NETWORK -> {
                // 网络错误，提供重试选项
                if (showUserMessage) {
                    _errorEvents.trySend(
                        ErrorEvent.ShowSnackbar(
                            message = error.message,
                            action = "重试"
                        )
                    )
                }
            }

            ErrorType.SERVER -> {
                // 服务器错误
                if (showUserMessage) {
                    _errorEvents.trySend(ErrorEvent.ShowToast(error.message))
                }
            }

            ErrorType.VALIDATION -> {
                // 验证错误
                if (showUserMessage) {
                    _errorEvents.trySend(ErrorEvent.ShowSnackbar(error.message))
                }
            }

            ErrorType.TIMEOUT -> {
                // 超时错误，提供重试
                if (showUserMessage) {
                    _errorEvents.trySend(
                        ErrorEvent.ShowSnackbar(
                            message = error.message,
                            action = "重试"
                        )
                    )
                }
            }

            ErrorType.UNKNOWN -> {
                // 未知错误
                if (showUserMessage) {
                    _errorEvents.trySend(
                        ErrorEvent.ShowToast(
                            "操作失败，请重试"
                        )
                    )
                }
            }
        }

        // 记录错误日志
        logError(error)
    }

    /**
     * 处理认证错误
     */
    private fun handleAuthenticationError(error: ApiResult.Error) {
        when (error.code) {
            401 -> {
                // Token过期，清除登录状态并跳转到登录页
                AuthEventBus.sendEvent(AuthEventBus.AuthEvent.SessionExpired)
                _errorEvents.trySend(ErrorEvent.NavigateToLogin("登录已过期，请重新登录"))
            }

            else -> {
                // 其他认证错误
                _errorEvents.trySend(ErrorEvent.RequireLogin(error.message))
            }
        }
    }

    /**
     * 处理网络不可用错误
     */
    fun handleNetworkUnavailable(showUserMessage: Boolean = true) {
        if (showUserMessage) {
            _errorEvents.trySend(
                ErrorEvent.ShowSnackbar(
                    message = "网络连接不可用，请检查网络设置",
                    action = "重试"
                )
            )
        }
    }

    /**
     * 记录错误日志
     */
    private fun logError(error: ApiResult.Error) {
        Timber.e(
            error.throwable,
            "API Error - Code: ${error.code}, Type: ${error.errorType}, Message: ${error.message}"
        )

        // 如果集成了Firebase Crashlytics，可以在这里上报非致命错误
        try {
            com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance()
                .recordException(error.throwable ?: Exception(error.message))
        } catch (_: Exception) {
            // 忽略Crashlytics错误
        }
    }
}
