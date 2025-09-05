package com.kira.learning.module.auth

import com.kira.learning.model.LoginRequest
import com.kira.learning.network.ComposeApiService
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.learning.network.InMemoryAuthTokenProvider
import com.kira.learning.network.ApiResult
import com.kira.learning.network.toApiResult
import com.kira.learning.base.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ComposeApiService,
    private val settings: SettingsManager,
    private val inMemory: InMemoryAuthTokenProvider
) : BaseRepository() {

    suspend fun login(email: String, pwd: String): ApiResult<Unit> = withContext(Dispatchers.IO) {
        safeApiCall {
            val base = api.login(LoginRequest(username = email, password = pwd))
            when (val r = base.toApiResult()) {
                is ApiResult.Success -> {
                    val token = r.data.token
                    if (token.isNullOrBlank()) {
                        throw Exception("token为空")
                    } else {
                        settings.apiToken = token
                        settings.apiTokenExpireAt = Long.MAX_VALUE
                        inMemory.updateToken(token)
                        AuthEventBus.emit(AuthEventBus.AuthEvent.LoggedIn)
                    }
                }
                is ApiResult.Error -> throw Exception(r.message)
                is ApiResult.NetworkUnavailable -> throw Exception("网络不可用")
            }
        }
    }

    fun clear() {
        val had = settings.apiToken != null
        settings.apiToken = null
        settings.apiTokenExpireAt = 0
        inMemory.updateToken(null)
        if (had) AuthEventBus.emit(AuthEventBus.AuthEvent.LoggedOut)
    }

    fun tokenValid(): Boolean = settings.apiToken != null
}
