package com.kira.learning.bean.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    // 插入单条消息
    @Insert
    suspend fun insertMessage(message: ChatMessage): Long

    // 批量插入消息
    @Insert
    suspend fun insertMessages(messages: List<ChatMessage>)

    // 根据会话ID查询所有消息（按时间倒序DESC ,正序 ASC）
    @Query("SELECT * FROM ai_chat_message WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessagesByConversation(conversationId: Long): Flow<List<ChatMessage>>

    // 更新消息内容
    @Update
    suspend fun updateMessage(message: ChatMessage)

    // 删除单条消息
    @Query("DELETE FROM ai_chat_message WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Long)

    // 清空会话的所有消息
    @Query("DELETE FROM ai_chat_message WHERE conversationId = :conversationId")
    suspend fun clearConversation(conversationId: Long)
}