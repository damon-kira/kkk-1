package com.kira.learning.module.chat.repository

import com.kira.learning.module.chat.bean.ChatEntity
import com.kira.learning.module.chat.database.ChatDao
import com.kira.learning.net.ApiService
//import com.kira.learning.module.chat.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

// app/src/main/kotlin/com/example/ai/chatdemo/repository/ChatRepository.kt
//@HiltViewModel
class ChatRepository @Inject constructor(
    private val apiService: ApiService,
    private val chatDao: ChatDao
) {
    // 获取聊天记录（合并网络和本地数据）
    fun getMessages(): Flow<List<ChatEntity>> {
        return chatDao.getAllMessages()
    }

    // 发送消息（先保存到本地，再模拟网络请求）
    suspend fun sendMessage(message: String, isUser: Boolean) {
        // 保存到本地数据库
        val entity = ChatEntity(
            message = message,
            isUser = isUser,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(entity)

        // 模拟网络请求
        val response = withContext(Dispatchers.IO) {
//            apiService.sendMessage(
//                ApiService.SendMessageRequest(message, System.currentTimeMillis())
//            )
        }

        // 更新消息状态（实际项目中需要处理错误情况）
        val updatedEntity = entity.copy(
//            imageUrl = response.imageUrl
        )
        chatDao.insertMessage(updatedEntity)
    }
}