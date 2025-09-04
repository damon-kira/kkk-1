package com.kira.learning.module.auth

import com.kira.learning.model.LoginRequest
import com.kira.learning.network.ComposeApiService
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.learning.network.InMemoryAuthTokenProvider
import com.kira.learning.network.ApiResult
import com.kira.learning.network.toApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ComposeApiService,
    private val settings: SettingsManager,
    private val inMemory: InMemoryAuthTokenProvider
) {
    // 去除 TOKEN_TTL_MS 与本地过期时间控制，仅由服务端 401 决定失效

    // 返回 ApiResult<Unit>，封装成功与错误。避免外部直接依赖 BaseResponse。
    suspend fun login(email: String, pwd: String): ApiResult<Unit> = withContext(Dispatchers.IO) {
        try {
            val base = api.login(LoginRequest(username = email, password = pwd))
            when (val r = base.toApiResult()) {
                is ApiResult.Success -> {
                    val token = r.data.token
                    if (token.isNullOrBlank()) {
                        ApiResult.Error(code = base.code, message = "token为空")
                    } else {
                        settings.apiToken = token
                        // 设成一个极大值避免残留逻辑读取到 0 误判；后续不再依赖该字段
                        settings.apiTokenExpireAt = Long.MAX_VALUE
                        inMemory.updateToken(token)
                        AuthEventBus.emit(AuthEventBus.AuthEvent.LoggedIn)
                        ApiResult.Success(Unit)
                    }
                }
                is ApiResult.Error -> r
                is ApiResult.NetworkUnavailable -> r
            }
        } catch (e: Exception) {
            ApiResult.Error(message = e.message ?: "未知错误", throwable = e)
        }
    }

    fun clear() {
        val had = settings.apiToken != null
        settings.apiToken = null
        settings.apiTokenExpireAt = 0
        inMemory.updateToken(null)
        if (had) AuthEventBus.emit(AuthEventBus.AuthEvent.LoggedOut)
    }

    fun tokenValid(): Boolean {
        val token = settings.apiToken
        return !token.isNullOrBlank()
    }

    // tokenExpiry 不再需要，如外部仍引用可保留返回极大值；当前已无调用点，删除。
}
