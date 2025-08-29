package com.kira.learning.compose.module.chat

import com.kira.learning.bean.dao.AIResponseInfo
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.compose.network.ComposeApiService
import com.kira.learning.compose.network.toApiResult
import com.kira.learning.compose.network.ApiResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

interface ChatRemoteDataSource {
    suspend fun sendOnce(prompt: String): ApiResult<String>
    fun stream(prompt: String): Flow<ApiResult<String>> // 每次发出累积内容 (Success 持续发射, 结束发最后一次)
}

class RealChatRemoteDataSource @Inject constructor(private val api: ComposeApiService): ChatRemoteDataSource {
    private val json = "application/json; charset=utf-8".toMediaType()
    override suspend fun sendOnce(prompt: String): ApiResult<String> = runCatching {
        val body = buildBody(prompt)
        api.aiSendRequest(body).toApiResult()
    }.fold(onSuccess = { it.mapData() }, onFailure = { ApiResult.Error(message = it.message ?: "网络错误", throwable = it) })

    override fun stream(prompt: String): Flow<ApiResult<String>> = flow {
        // 后端暂无流式, 先模拟: 调用一次拿全量 -> 按 token 拆分
        when(val once = sendOnce(prompt)) {
            is ApiResult.Success -> {
                val tokens = tokenize(once.data)
                val sb = StringBuilder()
                tokens.forEach { t ->
                    sb.append(t)
                    emit(ApiResult.Success(sb.toString()))
                    delay(30)
                }
            }
            is ApiResult.Error -> emit(once)
            ApiResult.NetworkUnavailable -> emit(ApiResult.NetworkUnavailable)
        }
    }

    private fun buildBody(prompt: String) = JSONObject().apply {
        put("messages", JSONArray().apply { put(JSONObject().apply { put("role","user"); put("content",prompt) }) })
    }.toString().toRequestBody(json)
}

class MockChatRemoteDataSource @Inject constructor(): ChatRemoteDataSource {
    override suspend fun sendOnce(prompt: String): ApiResult<String> = ApiResult.Success("这是模拟回答: $prompt -> 完整解释内容。")
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

private fun tokenize(full: String): List<String> = full.chunked(4)
