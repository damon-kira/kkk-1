package com.kira.learning.xml.modules.chat.bean

data class ChatResponse(
    val message: String,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)