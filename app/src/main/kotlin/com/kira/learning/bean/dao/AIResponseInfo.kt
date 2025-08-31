package com.kira.learning.bean.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kira.learning.xml.modules.ai.ChoiceConverters

@Entity(tableName = "ai_responses")
@TypeConverters(ChoiceConverters::class)
data class AIResponseInfo(
    @PrimaryKey(autoGenerate = true) val idKey: Long = 0L,
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)

@Entity(tableName = "ai_chat_message")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) // 主键自增
    val id: Long = 0L,
    val content: String = "",
    val isUser: String = "USER",
    val timestamp: Long = System.currentTimeMillis(),
    var conversationId: Long = 0L
)