package com.kira.learning.xml.modules.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kira.learning.xml.modules.chat.bean.ChatEntity
import com.kira.learning.xml.modules.chat.repository.ChatRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

// app/src/main/kotlin/com/example/ai/chatdemo/ui/viewmodel/ChatViewModel.kt
//@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    // LiveData用于UI观察
    val messages: LiveData<List<ChatEntity>> = repository.getMessages().asLiveData()

    // 发送消息的LiveData
    private val _sendMessageResult = MutableLiveData<Boolean>()
    val sendMessageResult: LiveData<Boolean> get() = _sendMessageResult

    // 发送消息函数
    fun sendMessage(message: String, isUser: Boolean) = viewModelScope.launch {
        try {
            repository.sendMessage(message, isUser)
            _sendMessageResult.postValue(true)
        } catch (e: Exception) {
            _sendMessageResult.postValue(false)
        }
    }
}