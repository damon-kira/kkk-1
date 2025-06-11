package com.kira.learning.module.answer.repo

import androidx.lifecycle.LiveData
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.kira.learning.app.BaseRepository
import com.kira.learning.bean.db.AIResponseInfo
import com.kira.learning.bean.QuestionProcessInfo
import javax.inject.Inject

class AnswerRepository @Inject constructor() : BaseRepository() {

    private val gson = Gson()
    private val questionRequestJson: JsonObject = JsonObject()

    fun searchQuestion(questionContent: String): LiveData<BaseResponse<QuestionProcessInfo>> {
        questionRequestJson.addProperty("question", questionContent)
        return ApiServiceLiveDataProxy.request(QuestionProcessInfo::class.java) {
            apiService.searchQuestion(createRequestBody(questionContent))
        }
    }

    fun aiSendRequest(userSend: String, APIKEY: String) =
        ApiServiceLiveDataProxy.request(AIResponseInfo::class.java) {
            saveUserSend(userSend)
            apiService.aiSendRequest(createRequestBody(jsonRequest.toString()))
        }

    private val jsonRequest: JsonObject = initModelJSON()
    private val parsedMessages: JsonArray = jsonRequest.getAsJsonArray("messages")

    // 保存用户对话内容，用于连续对话
    private fun saveUserSend(userSend: String?) {
        val newMessage = JsonObject()
        newMessage.addProperty("role", "user")
        newMessage.addProperty("content", userSend)
        parsedMessages.add(newMessage)
        jsonRequest.add("messages", parsedMessages)
    }

    companion object {
        fun initModelJSON(): JsonObject {
            // 创建JSON请求体
            val jsonRequest = JsonObject()
            // 添加模型信息
            jsonRequest.addProperty("model", "moonshot-v1-32k")
            // 添加消息数组
            val messages = JsonArray()
            val system = JsonObject()
            system.addProperty("role", "system")
            system.addProperty(
                "content", "你是 KunKun，由 进进菜鸟 提供的人工智能助手，你更擅长中文和英文的对话。" +
                        "你会为用户提供安全，有帮助，准确的回答。同时，你会拒绝一些涉及恐怖主义，种族歧视，黄色暴力等问题的回答。" +
                        "进进菜鸟 为专有名词，不可翻译成其他语言。"
            )
            messages.add(system)
            jsonRequest.add("messages", messages)
            jsonRequest.addProperty("temperature", 0.3)
            // jsonRequest.addProperty("stream", true)
            return jsonRequest
        }
    }
}