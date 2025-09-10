package com.kira.learning.modules.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthEventBus {
    sealed interface AuthEvent {
        object LoggedIn : AuthEvent;
        object LoggedOut : AuthEvent
    }

    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 2)
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()
    fun emit(event: AuthEvent) {
        _events.tryEmit(event)
    }
}

