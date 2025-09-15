package com.kira.learning.modules.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.network.ApiResult
import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.base.mvi.UiEvent
import com.kira.learning.base.mvi.ViewEvent
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.base.validation.FormState
import com.kira.learning.base.validation.FormField
import com.kira.learning.base.validation.Validators
import com.kira.learning.models.LoginData
import com.kira.learning.models.UserRole
import com.kira.learning.models.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 重构后的登录状态
data class AuthViewState(
    val isLoading: Boolean = false,
    val formState: FormState = FormState(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val rememberMe: Boolean = false,
    val currentUser: LoginData? = null,
    val availableRoles: List<UserRole> = emptyList(),
    val isRoleSwitching: Boolean = false
) : ViewState()

// 认证事件
sealed interface AuthEvent : ViewEvent {
    object Login : AuthEvent
    object Logout : AuthEvent
    object RefreshToken : AuthEvent
    data class UpdateEmail(val email: String) : AuthEvent
    data class UpdatePassword(val password: String) : AuthEvent
    data class UpdateRememberMe(val rememberMe: Boolean) : AuthEvent
    data class SwitchRole(val role: UserRole) : AuthEvent
    object ClearError : AuthEvent
    object ValidateSession : AuthEvent
    data class QuickLogin(val session: UserSession) : AuthEvent
    data class RemoveSession(val session: UserSession) : AuthEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : BaseViewModel<AuthViewState, AuthEvent>(
    initialState = AuthViewState()
) {

    init {
        setupFormValidation()
        observeUserSession()
        // 预填充测试数据
        updateState {
            copy(
                formState = formState.updateField("email", "test+teacher@kira-learning.com")
                    .updateField("password", "cd5ikb3R&XLZpTi5ryXg")
            )
        }
    }

    override fun handleAction(action: AuthEvent) {
        when (action) {
            is AuthEvent.Login -> login()
            is AuthEvent.Logout -> logout()
            is AuthEvent.RefreshToken -> refreshToken()
            is AuthEvent.UpdateEmail -> updateEmail(action.email)
            is AuthEvent.UpdatePassword -> updatePassword(action.password)
            is AuthEvent.UpdateRememberMe -> updateRememberMe(action.rememberMe)
            is AuthEvent.SwitchRole -> switchRole(action.role)
            is AuthEvent.ClearError -> clearError()
            is AuthEvent.ValidateSession -> validateSession()
            is AuthEvent.QuickLogin -> quickLogin(action.session)
            is AuthEvent.RemoveSession -> removeSession(action.session)
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

    private fun observeUserSession() {
        viewModelScope.launch {
            repo.currentSession.collect { session ->
                updateState {
                    copy(
                        currentUser = session?.loginData,
                        availableRoles = session?.loginData?.userRoles ?: emptyList(),
                        isSuccess = session != null
                    )
                }
            }
        }
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

    private fun updateRememberMe(rememberMe: Boolean) {
        updateState { copy(rememberMe = rememberMe) }
    }

    private fun clearError() {
        updateState { copy(errorMessage = null) }
    }

    private fun login() {
        val validatedForm = currentState.formState.validateAll()
        updateState { copy(formState = validatedForm) }

        if (!validatedForm.isValid) {
            sendUiEvent(UiEvent.ShowSnackbar("请检查输入信息"))
            return
        }

        val email = validatedForm.fields["email"]?.value?.trim() ?: ""
        val password = validatedForm.fields["password"]?.value ?: ""
        val rememberMe = currentState.rememberMe

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            repo.login(email, password, rememberMe).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        updateState {
                            copy(
                                isLoading = false,
                                isSuccess = true,
                                errorMessage = null,
                                currentUser = result.data,
                                availableRoles = result.data.userRoles
                            )
                        }
                        sendUiEvent(UiEvent.ShowSnackbar("登录成功"))
                        sendUiEvent(UiEvent.Navigate("main"))
                    }

                    is ApiResult.Error -> {
                        val message = result.message.ifBlank { "登录失败" }
                        updateState {
                            copy(
                                isLoading = false,
                                errorMessage = message
                            )
                        }
                        sendUiEvent(UiEvent.ShowSnackbar(message))
                    }

                    is ApiResult.NetworkUnavailable -> {
                        val message = "网络不可用，请检查网络连接"
                        updateState {
                            copy(
                                isLoading = false,
                                errorMessage = message
                            )
                        }
                        sendUiEvent(UiEvent.ShowSnackbar(message))
                    }

                    is ApiResult.Loading -> {
                        // Loading状态已在开始时设置
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            when (val result = repo.logout()) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            isSuccess = false,
                            currentUser = null,
                            availableRoles = emptyList(),
                            errorMessage = null
                        )
                    }
                    sendUiEvent(UiEvent.ShowSnackbar("已退出登录"))
                    sendUiEvent(UiEvent.Navigate("login"))
                }

                is ApiResult.Error -> {
                    updateState { copy(isLoading = false) }
                    sendUiEvent(UiEvent.ShowSnackbar("退出登录失败: ${result.message}"))
                }

                is ApiResult.NetworkUnavailable -> {
                    updateState { copy(isLoading = false) }
                    sendUiEvent(UiEvent.ShowSnackbar("网络不可用"))
                }

                is ApiResult.Loading -> {
                    // Loading状态已在开始时设置
                }
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            when (val result = repo.refreshToken()) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            currentUser = result.data,
                            errorMessage = null
                        )
                    }
                    sendUiEvent(UiEvent.ShowSnackbar("Token已刷新"))
                }

                is ApiResult.Error -> {
                    updateState { copy(errorMessage = result.message) }
                    sendUiEvent(UiEvent.ShowSnackbar("Token刷新失败: ${result.message}"))
                    // Token刷新失败，可能需要重新登录
                    repo.clearSession()
                    sendUiEvent(UiEvent.Navigate("login"))
                }

                is ApiResult.NetworkUnavailable -> {
                    sendUiEvent(UiEvent.ShowSnackbar("网络不可用"))
                }

                is ApiResult.Loading -> {
                    // Loading处理
                }
            }
        }
    }

    private fun switchRole(newRole: UserRole) {
        if (currentState.isRoleSwitching) return

        viewModelScope.launch {
            updateState { copy(isRoleSwitching = true) }

            when (val result = repo.switchRole(newRole)) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            isRoleSwitching = false,
                            currentUser = result.data,
                            errorMessage = null
                        )
                    }
                    sendUiEvent(UiEvent.ShowSnackbar("已切换到${newRole.displayName}模式"))
                }

                is ApiResult.Error -> {
                    updateState {
                        copy(
                            isRoleSwitching = false,
                            errorMessage = result.message
                        )
                    }
                    sendUiEvent(UiEvent.ShowSnackbar("角色切换失败: ${result.message}"))
                }

                is ApiResult.NetworkUnavailable -> {
                    updateState { copy(isRoleSwitching = false) }
                    sendUiEvent(UiEvent.ShowSnackbar("网络不可用"))
                }

                is ApiResult.Loading -> {
                    // Loading状态已在开始时设置
                }
            }
        }
    }

    private fun validateSession() {
        viewModelScope.launch {
            when (val result = repo.validateCurrentToken()) {
                is ApiResult.Success -> {
                    if (!result.data) {
                        // Token无效，清除会话并导航到登录页
                        repo.clearSession()
                        sendUiEvent(UiEvent.ShowSnackbar("会话已过期，请重新登录"))
                        sendUiEvent(UiEvent.Navigate("login"))
                    }
                }

                is ApiResult.Error -> {
                    sendUiEvent(UiEvent.ShowSnackbar("会话验证失败: ${result.message}"))
                }

                is ApiResult.NetworkUnavailable -> {
                    sendUiEvent(UiEvent.ShowSnackbar("网络不可用，无法验证会话"))
                }

                is ApiResult.Loading -> {
                    // Loading处理
                }
            }
        }
    }

    private fun quickLogin(session: UserSession) {
        viewModelScope.launch {
            updateState {
                copy(
                    currentUser = session.loginData,
                    availableRoles = session.loginData.userRoles,
                    isSuccess = true,
                    errorMessage = null
                )
            }
            sendUiEvent(UiEvent.ShowSnackbar("快速登录成功"))
            sendUiEvent(UiEvent.Navigate("main"))
        }
    }

    private fun removeSession(session: UserSession) {
        viewModelScope.launch {
            repo.removeSession(session)
            updateState {
                copy(
                    currentUser = null,
                    availableRoles = emptyList(),
                    isSuccess = false,
                    errorMessage = null
                )
            }
            sendUiEvent(UiEvent.ShowSnackbar("会话已移除"))
        }
    }
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    val loggedIn: StateFlow<Boolean> = repo.isLoggedIn
    val currentSession: StateFlow<UserSession?> = repo.currentSession

    init {
        viewModelScope.launch {
            AuthEventBus.events.collect { event ->
                when (event) {
                    is AuthEventBus.AuthEvent.LoggedIn -> {
                        // 可以在这里处理登录后的额外逻辑
                    }

                    is AuthEventBus.AuthEvent.LoggedOut -> {
                        // 可以在这里处理登出后的清理逻辑
                    }

                    else -> {}
                }
            }
        }
    }

    fun notifyLoginSuccess() {
        // 这个方法现在主要用于兼容性，实际的登录状态由 AuthRepository 管理
    }

    fun forceLogout() {
        viewModelScope.launch {
            repo.logout()
        }
    }

    fun getCurrentUser(): LoginData? = repo.getCurrentUser()

    fun hasRole(role: UserRole): Boolean = repo.hasRole(role)

    fun validateSession() {
        if (!repo.tokenValid()) {
            repo.clearSession()
        }
    }
}
