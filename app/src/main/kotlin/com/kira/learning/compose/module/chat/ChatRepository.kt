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
    // 标记当前是否使用本地模拟数据源（可由 VM 切换）
    @Volatile
    private var useMock = false
    fun setUseMock(v: Boolean) {
        useMock = v
    }

    // 根据开关动态选择实际数据源，避免上层出现分支
    private fun ds(): ChatRemoteDataSource = if (useMock) mock else real

    // 一次性请求
    suspend fun sendUserMessage(content: String): ApiResult<String> = ds().sendOnce(content)

    // 流式请求（逐步累积）
    fun streamUserMessage(content: String): Flow<ApiResult<String>> = ds().stream(content)
}
