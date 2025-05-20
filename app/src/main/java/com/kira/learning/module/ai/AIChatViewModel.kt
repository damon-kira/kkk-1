package com.kira.learning.module.ai

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kira.learning.bean.AIResponseInfo
import com.kira.learning.bean.ChatMessage
import com.kira.learning.db.dao.AIResponseDao
import com.kira.learning.db.dao.ChatMessageDao
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class AIChatViewModel @Inject constructor(
    private val repository: AIChatRepository,
    private val aiResponseDao: AIResponseDao,
    private val chatMessageDao: ChatMessageDao
) : BaseViewModel(), LifecycleEventObserver {

    val aiimLiveData = generatorLiveData<BaseResponse<AIResponseInfo>>()

    fun sendMessage(sendMessage: String) {
        showloading()
        aiimLiveData.addSourceLiveData(
            repository.aiSendRequest(
                sendMessage, ""
            )
        ) {
            hideLoading()
            aiimLiveData.postValue(it)
        }
    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)

        }
    }

    // 获取所有响应数据（以 Flow 形式观察）
    val responses: LiveData<List<AIResponseInfo>> =
        aiResponseDao.getAllResponses()
            .asLiveData(viewModelScope.coroutineContext)
//    val responses = aiResponseDao.getAllResponses()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )

    // 插入新响应
    fun insertResponse(response: AIResponseInfo) {
        viewModelScope.launch {
            aiResponseDao.insert(response)
        }
    }

    // 批量插入响应
    fun insertBatch(responses: List<AIResponseInfo>) {
        viewModelScope.launch {
            aiResponseDao.batchInsert(responses)
        }
    }

    fun insertMessage(message: ChatMessage) {
        viewModelScope.launch {
            chatMessageDao.insertMessage(message)
        }
    }

    // 当前会话ID（可通过外部传入更新）
    private val _currentConversationId = MutableStateFlow<Long?>(null)

    // 对外暴露的消息列表（UI层监听此Flow）
    val messages: Flow<List<ChatMessage>> = _currentConversationId
        .filterNotNull() // 过滤null值，确保conversationId有效
        .flatMapLatest { conversationId ->
            // 当conversationId变化时，自动取消旧查询并启动新查询
            chatMessageDao.getMessagesByConversation(conversationId)
//                .catch { e ->
//                    // 捕获数据库查询异常
//                    _errorEvent.emit(e)
//                    emit(emptyList()) // 发生错误时返回空列表避免UI崩溃
//                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 5秒无订阅停止流
            initialValue = emptyList() // 初始值
        )

    // 错误事件（UI层可通过此Flow显示错误提示）
//    private val _errorEvent = MutableSharedFlow<Throwable>()
//    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()
//
    fun setConversation(conversationId: Long) {
        _currentConversationId.value = conversationId
    }
//
//    fun sendMessage(content: String) {
//        viewModelScope.launch {
//            try {
//                val newMessage = ChatMessage(
//                    content = content,
//                    isFromUser = true,
//                    conversationId = _currentConversationId.value ?: return@launch
//                )
//                chatRepository.insertMessage(newMessage)
//            } catch (e: Exception) {
//                _errorEvent.emit(e)
//            }
//        }
//    }

}