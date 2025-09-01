package com.kira.learning.module.chat

import com.kira.learning.model.dao.AIResponseInfo
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.network.ComposeApiService
import com.kira.learning.network.toApiResult
import com.kira.learning.network.ApiResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

interface ChatRemoteDataSource {
    // 发送一次性完整回答，返回最终文本
    suspend fun sendOnce(prompt: String): ApiResult<String>
    // 流式发送：模拟/真实逐步返回累积文本；每个 Success.data = 到当前为止的完整内容
    fun stream(prompt: String): Flow<ApiResult<String>>
}

// RealChatRemoteDataSource: 使用后端接口（当前后端不支持真正流式 -> 本地拆分 token 模拟）
class RealChatRemoteDataSource @Inject constructor(private val api: ComposeApiService): ChatRemoteDataSource {
    private val json = "application/json; charset=utf-8".toMediaType()

    override suspend fun sendOnce(prompt: String): ApiResult<String> = runCatching {
        val body = buildBody(prompt)
        api.aiSendRequest(body).toApiResult() // 先拿原始 BaseResponse -> ApiResult
    }.fold(
        onSuccess = { it.mapData() }, // 转换成纯文本
        onFailure = { ApiResult.Error(message = it.message ?: "网络错误", throwable = it) }
    )

    override fun stream(prompt: String): Flow<ApiResult<String>> = flow {
        // 真实后端暂无 SSE/WebSocket，取完整回答后按固定长度切片模拟流式体验
        when(val once = sendOnce(prompt)) {
            is ApiResult.Success -> {
                val tokens = tokenize(once.data)
                val sb = StringBuilder()
                tokens.forEach { t ->
                    sb.append(t)
                    emit(ApiResult.Success(sb.toString()))
                    delay(30) // 控制“打字”速度
                }
            }
            is ApiResult.Error -> emit(once)
            ApiResult.NetworkUnavailable -> emit(ApiResult.NetworkUnavailable)
        }
    }

    private fun buildBody(prompt: String) = JSONObject().apply {
        put("messages", JSONArray().apply {
            put(JSONObject().apply { put("role","user"); put("content",prompt) })
        })
    }.toString().toRequestBody(json)
}

// Mock 数据源：完全本地拼接模拟，便于离线/后端未就绪调试
class MockChatRemoteDataSource @Inject constructor(): ChatRemoteDataSource {
    override suspend fun sendOnce(prompt: String): ApiResult<String> =
        ApiResult.Success("这是模拟回答: $prompt -> 完整解释内容。")

    override fun stream(prompt: String): Flow<ApiResult<String>> = flow {
        val fake = listOf("这是", " 模拟", " 回答", ": ", prompt.take(20), " -> ", "分步骤", " 详细", " 说明", "。")
        val sb = StringBuilder()
        fake.forEach { seg ->
            sb.append(seg)
            emit(ApiResult.Success(sb.toString()))
            delay(60)
        }
    }
}

// 将后端响应 (BaseResponse<AIResponseInfo>) 提取出最终文本；保持错误类型
private fun <T> ApiResult<T>.mapData(): ApiResult<String> = when (this) {
    is ApiResult.Success -> {
        val raw = data
        val extracted = when (raw) {
            is BaseResponse<*> -> {
                val inner = raw.data
                if (inner is AIResponseInfo) inner.choices.firstOrNull()?.message?.content else null
            }
            else -> null
        }
        ApiResult.Success(extracted ?: raw?.toString().orEmpty())
    }
    is ApiResult.Error -> ApiResult.Error(code, message, throwable)
    ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

// 简单“分词”策略：按固定长度切片（真实生产可替换为 token 分词）
private fun tokenize(full: String): List<String> = full.chunked(4)
