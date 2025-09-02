package com.kira.learning.module.auth

import com.kira.learning.network.ComposeApiService
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.learning.network.InMemoryAuthTokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ComposeApiService,
    private val settings: SettingsManager,
    private val inMemory: InMemoryAuthTokenProvider
) {
    companion object {
        private const val TOKEN_TTL_MS = 60_000L
    }

    suspend fun login(email: String, pwd: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val resp = api.login(LoginRequest(username = email, password = pwd))
            if (resp.success == true && resp.data?.token != null) {
                val token = resp.data.token
                val expire = System.currentTimeMillis() + TOKEN_TTL_MS
                settings.apiToken = token
                settings.apiTokenExpireAt = expire
                inMemory.updateToken(token)
                AuthEventBus.emit(AuthEventBus.AuthEvent.LoggedIn)
                Result.success(Unit)
            } else {
                Result.failure(RuntimeException(resp.msg ?: "登录失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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
        val exp = settings.apiTokenExpireAt
        return !token.isNullOrBlank() && exp > System.currentTimeMillis()
    }

    fun tokenExpiry(): Long = settings.apiTokenExpireAt
}
