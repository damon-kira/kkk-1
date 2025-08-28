package com.kira.learning.compose.module.chat

import com.kira.learning.compose.network.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val real: RealChatRemoteDataSource,
    private val mock: MockChatRemoteDataSource
) {
    @Volatile private var useMock = false
    fun setUseMock(v: Boolean) { useMock = v }
    private fun ds(): ChatRemoteDataSource = if (useMock) mock else real
    suspend fun sendUserMessage(content: String): ApiResult<String> = ds().sendOnce(content)
    fun streamUserMessage(content: String): Flow<ApiResult<String>> = ds().stream(content)
}
