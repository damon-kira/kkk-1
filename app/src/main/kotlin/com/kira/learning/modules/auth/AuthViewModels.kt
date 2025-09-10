package com.kira.learning.modules.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.network.ApiResult
import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.base.mvi.UiEvent
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.base.validation.FormState
import com.kira.learning.base.validation.FormField
import com.kira.learning.base.validation.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 重构后的登录状态
data class AuthViewState(
    val isLoading: Boolean = false,
    val formState: FormState = FormState(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : ViewState()

// 认证事件
sealed interface AuthEvent {
    object Login : AuthEvent
    data class UpdateEmail(val email: String) : AuthEvent
    data class UpdatePassword(val password: String) : AuthEvent
    object ClearError : AuthEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : BaseViewModel<AuthViewState, UiEvent>(
    initialState = AuthViewState()
) {

    init {
        setupFormValidation()
        // 预填充测试数据
        updateState {
            copy(
                formState = formState.updateField("email", "test+teacher@kira-learning.com")
                    .updateField("password", "cd5ikb3R&XLZpTi5ryXg")
            )
        }
    }

    override fun handleAction(action: Any) {
        when (action) {
            is AuthEvent.Login -> login()
            is AuthEvent.UpdateEmail -> updateEmail(action.email)
            is AuthEvent.UpdatePassword -> updatePassword(action.password)
            is AuthEvent.ClearError -> clearError()
        }
    }

    private fun setupFormValidation() {
        val initialFormState = FormState(
            fields = mapOf(
                "email" to FormField(
                    validators = listOf(
                        Validators.required("请输入邮箱"),
                        Validators.email("请输入有效的邮箱地址")
                    )
                ),
                "password" to FormField(
                    validators = listOf(
                        Validators.required("请输入密码"),
                        Validators.minLength(6, "密码至少6位")
                    )
                )
            )
        )
        updateState { copy(formState = initialFormState) }
    }

    private fun updateEmail(email: String) {
        updateState {
            copy(
                formState = formState.updateField("email", email),
                errorMessage = null
            )
        }
    }

    private fun updatePassword(password: String) {
        updateState {
            copy(
                formState = formState.updateField("password", password),
                errorMessage = null
            )
        }
    }

    private fun clearError() {
        updateState { copy(errorMessage = null) }
    }

    private fun login() {
        val validatedForm = currentState.formState.validateAll()
        updateState { copy(formState = validatedForm) }

        if (!validatedForm.isValid) {
            sendEvent(UiEvent.ShowSnackbar("请检查输入信息"))
            return
        }

        val email = validatedForm.fields["email"]?.value?.trim() ?: ""
        val password = validatedForm.fields["password"]?.value ?: ""

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = repo.login(email, password)) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar("登录成功"))
                    sendEvent(UiEvent.Navigate("main"))
                }

                is ApiResult.Error -> {
                    val message = result.message.ifBlank { "登录失败" }
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }

                ApiResult.NetworkUnavailable -> {
                    val message = "网络不可用"
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }
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

    fun notifyLoginSuccess() {
        _loggedIn.value = true
    }

    fun forceLogout() {
        repo.clear()
    }
}
