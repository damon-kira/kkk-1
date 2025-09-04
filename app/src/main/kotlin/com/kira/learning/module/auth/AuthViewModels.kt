package com.kira.learning.module.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Error(val message: String) : LoginUiState
    object Success : LoginUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    //    var email = MutableStateFlow("damon@kira-learning.com")
    var email = MutableStateFlow("test+teacher@kira-learning.com")
        private set
    var password = MutableStateFlow("cd5ikb3R&XLZpTi5ryXg")
        private set

    fun onEmailChange(v: String) {
        email.value = v
    }

    fun onPasswordChange(v: String) {
        password.value = v
    }

    fun login() {
        val e = email.value.trim();
        val p = password.value
        if (e.isEmpty() || p.isEmpty()) {
            _uiState.value = LoginUiState.Error("请输入邮箱与密码"); return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val r = repo.login(e, p)) {
                is ApiResult.Success -> _uiState.value = LoginUiState.Success
                is ApiResult.Error -> _uiState.value = LoginUiState.Error(r.message.ifBlank { "登录失败" })
                is ApiResult.NetworkUnavailable -> _uiState.value = LoginUiState.Error("网络不可用")
            }
        }
    }
}

@HiltViewModel
class SessionViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {
    private val _loggedIn = MutableStateFlow(repo.tokenValid())
    val loggedIn: StateFlow<Boolean> = _loggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            AuthEventBus.events.collect { ev ->
                when (ev) {
                    is AuthEventBus.AuthEvent.LoggedIn -> _loggedIn.value = true
                    is AuthEventBus.AuthEvent.LoggedOut -> _loggedIn.value = false
                }
            }
        }
    }

    fun notifyLoginSuccess() { _loggedIn.value = true }
    fun forceLogout() { repo.clear() }
}
