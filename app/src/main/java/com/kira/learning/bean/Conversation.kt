package com.kira.learning.bean

class Conversation //this.conversationId = conversationId;
    (// 设置一个随机数
    var conversationId: Long, // 设置为当前时间
    var startTimeStamp: Long,
    var chatMessages: ArrayList<ChatMessage>,
    var conPayerName: String?
) {
    val lastMsg: ChatMessage
        get() = chatMessages.get(chatMessages.size - 1)
}