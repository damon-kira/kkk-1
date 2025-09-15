package com.kira.learning.modules.auth

import com.kira.learning.network.ApiResult
import com.kira.learning.network.ComposeApiService
import com.kira.learning.base.repository.BaseRepository
import com.kira.learning.base.keyvalue.SettingsManager
import com.kira.learning.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ComposeApiService, private val settingsManager: SettingsManager
) : BaseRepository() {

    private val _currentSession = MutableStateFlow<UserSession?>(null)
    val currentSession: StateFlow<UserSession?> = _currentSession.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        // 应用启动时检查本地存储的会话
        restoreSession()
    }

    /**
     * 用户登录 - 统一返回Flow<ApiResult<LoginData>>
     */
    fun login(
        username: String, password: String, rememberMe: Boolean = false
    ): Flow<ApiResult<LoginData>> {
        val request = LoginRequest(username = username, password = password)
        return executeBaseResponseFlow {
            apiService.login(request).also { response ->
                // 登录成功后保存会话信息
                if (response.isSuccess() && response.data != null) {
                    saveSession(UserSession(response.data, isRememberMe = rememberMe))
                }
            }
        }
    }

    /**
     * 刷新Token - 统一使用executeApiCall
     */
    suspend fun refreshToken(): ApiResult<LoginData> {
        val currentRefreshToken = _currentSession.value?.loginData?.refreshToken
        if (currentRefreshToken.isNullOrEmpty()) {
            return ApiResult.Error(message = "No refresh token available")
        }

        return executeBaseResponseCall {
            val request = RefreshTokenRequest(currentRefreshToken)
            val response = apiService.refreshToken(request)
            response.data?.let { newLoginData ->
                // 更新会话信息
                _currentSession.value?.let { currentSession ->
                    saveSession(currentSession.copy(loginData = newLoginData))
                }
            }
            response
        }
    }

    /**
     * 用户注册 - 统一使用executeApiCall
     */
    suspend fun register(
        username: String, password: String, email: String, preferredLocale: String = "en-US"
    ): ApiResult<LoginData> {
        return executeBaseResponseCall {
            val request = RegisterRequest(username, password, email, preferredLocale)
            apiService.register(request)
        }
    }

    /**
     * 用户登出 - 统一错误处理
     */
    suspend fun logout(): ApiResult<Unit> {
        val result = executeApiCall {
            apiService.logout()
        }

        // 无论API调用是否成功，都清除本地会话
        clearSession()

        return when (result) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> result
            is ApiResult.NetworkUnavailable -> {
                // 网络不可用时也认为登出成功（本地已清除）
                ApiResult.Success(Unit)
            }

            else -> ApiResult.Success(Unit)
        }
    }

    /**
     * 验证当前Token是否有效
     */
    suspend fun validateCurrentToken(): ApiResult<Boolean> {
        val token = getCurrentToken()
        if (token.isNullOrEmpty()) {
            return ApiResult.Success(false)
        }

        return executeApiCall {
            val result = apiService.validateToken(token)
            result.isValid
        }
    }

    /**
     * 切换用户角色
     */
    suspend fun switchRole(newRole: UserRole): ApiResult<LoginData> {
        val currentSession = _currentSession.value
        if (currentSession == null || !currentSession.loginData.hasRole(newRole)) {
            return ApiResult.Error(message = "Invalid role or not logged in")
        }

        return executeBaseResponseCall {
            val request = RoleSwitchRequest(newRole.code)
            val response = apiService.switchRole(request)
            response.data?.let { newLoginData ->
                saveSession(currentSession.copy(loginData = newLoginData))
            }
            response
        }
    }

    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(): LoginData? = _currentSession.value?.loginData

    /**
     * 获取当前Token
     */
    fun getCurrentToken(): String? = _currentSession.value?.loginData?.token

    /**
     * 检查Token是否有效
     */
    fun tokenValid(): Boolean {
        val session = _currentSession.value ?: return false
        return !session.isExpired && session.loginData.isTokenValid
    }

    /**
     * 检查用户是否有特定角色
     */
    fun hasRole(role: UserRole): Boolean = _currentSession.value?.loginData?.hasRole(role) ?: false

    /**
     * 保存会话信息
     */
    private fun saveSession(session: UserSession) {
        _currentSession.value = session
        _isLoggedIn.value = true

        // 保存到本地存储
        if (session.isRememberMe) {
            settingsManager.apiToken = session.loginData.token
            // TODO: 扩展 SettingsManager 支持这些属性
            // settingsManager.refreshToken = session.loginData.refreshToken
            // settingsManager.userRole = session.loginData.currentRole
            // settingsManager.loginTimestamp = session.loginTimestamp
        }

        // 发送登录事件
        AuthEventBus.sendEvent(AuthEventBus.AuthEvent.LoggedIn(session.loginData))
    }

    /**
     * 恢复会话信息
     */
    private fun restoreSession() {
        val token = settingsManager.apiToken
        // TODO: 从 SettingsManager 恢复更多会话数据

        if (!token.isNullOrEmpty()) {
            val loginData = LoginData(
                token = token,
                refreshToken = "", // 临时值，需要从设置中恢复
                currentRole = "0", // 临时值，需要从设置中恢复
                prove = "restored",
                loginDate = System.currentTimeMillis().toString(),
                currentVersion = "1.0",
                preferredLocale = "en-US"
            )

            val session = UserSession(
                loginData = loginData,
                loginTimestamp = System.currentTimeMillis(),
                isRememberMe = true
            )

            if (!session.isExpired) {
                _currentSession.value = session
                _isLoggedIn.value = true
            } else {
                // 会话过期，清除本地数据
                clearSession()
            }
        }
    }

    /**
     * 清除会话信息
     */
    fun clearSession() {
        val currentUser = _currentSession.value?.loginData
        _currentSession.value = null
        _isLoggedIn.value = false

        // 清除本地存储
        settingsManager.apiToken = null
        // TODO: 清除扩展的设置字段

        // 发送登出事件
        currentUser?.let {
            AuthEventBus.sendEvent(AuthEventBus.AuthEvent.LoggedOut(it))
        }
    }

    /**
     * 移除特定会话
     */
    fun removeSession(session: UserSession) {
        // 如果移除的是当前会话，清除所有状态
        if (_currentSession.value?.loginTimestamp == session.loginTimestamp) {
            clearSession()
        }
        // 这里可以扩展为从本地存储中移除特定会话记录
        // 目前简化处理为清除当前会话
    }

    /**
     * 清除所有数据（用于测试或重置）
     */
    fun clear() = clearSession()
}
