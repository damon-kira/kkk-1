//package com.kira.learning.xml.modules.chat.network
//
//import com.kira.learning.xml.modules.chat.bean.ChatResponse
//import retrofit2.http.Body
//import retrofit2.http.POST
//
//interface ApiService {
//    // 模拟发送消息的API
//    @POST("chat/send")
//    suspend fun sendMessage(@Body request: SendMessageRequest): ChatResponse
//
//    data class SendMessageRequest(
//        val message: String,
//        val timestamp: Long
//    )
//}