package com.kira.learning.modules.auth

import com.kira.learning.models.LoginData
import com.kira.learning.models.UserRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    sealed interface AuthEvent {
        data class LoggedIn(val userData: LoginData) : AuthEvent
        data class LoggedOut(val userData: LoginData) : AuthEvent
        data class RoleSwitched(val newRole: UserRole, val userData: LoginData) : AuthEvent
        data class TokenRefreshed(val newToken: String) : AuthEvent
        object SessionExpired : AuthEvent
        data class LoginFailed(val reason: String) : AuthEvent
        object NetworkError : AuthEvent
    }

    private val _events = MutableSharedFlow<AuthEvent>(
        extraBufferCapacity = 10,
        replay = 0
    )
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    fun sendEvent(event: AuthEvent) {
        _events.tryEmit(event)
    }

    // 兼容旧方法
    @Deprecated("Use sendEvent instead", ReplaceWith("sendEvent(event)"))
    fun emit(event: AuthEvent) = sendEvent(event)
}
