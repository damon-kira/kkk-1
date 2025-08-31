package com.kira.learning.xml.modules.chat.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null
)