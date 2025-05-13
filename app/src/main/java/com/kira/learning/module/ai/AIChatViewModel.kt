package com.kira.learning.module.ai

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kira.learning.module.ai.bean.AIResponseInfo
import com.kira.learning.module.ai.bean.ChatMessage
import com.kira.learning.module.ai.dao.AIResponseDao
import com.kira.learning.module.ai.dao.ChatMessageDao
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
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

}