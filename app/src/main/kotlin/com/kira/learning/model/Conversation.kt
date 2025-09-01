package com.kira.learning.model

import com.kira.learning.model.dao.ChatMessage

class Conversation //this.conversationId = conversationId;
    (// 设置一个随机数
    var conversationId: Long, // 设置为当前时间
    var startTimeStamp: Long,
    var chatMessages: ArrayList<ChatMessage>,
    var conPayerName: String?
) {
    val lastMsg: ChatMessage
        get() = chatMessages[chatMessages.size - 1]
}