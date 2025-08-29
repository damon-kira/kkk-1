package com.kira.learning.module.answer.vm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.bean.QuestionProcessInfo
import com.kira.learning.module.answer.repo.AnswerRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AnswerViewModel @Inject constructor(
    private val repository: AnswerRepository,
) : BaseViewModel(), LifecycleEventObserver {

    val datas = generatorLiveData<BaseResponse<QuestionProcessInfo>>()

    suspend fun searchQuestion(questionContent: String){
        showloading()
        delay(2000)
        datas.addSourceLiveData(
            repository.searchQuestion(
                questionContent
            )
        ) {
            hideLoading()
            datas.postValue(it)
        }
    }

//    fun sendMessage(sendMessage: String) {
//        showloading()
//        aiimLiveData.addSourceLiveData(
//            repository.aiSendRequest(
//                sendMessage, ""
//            )
//        ) {
//            hideLoading()
//            aiimLiveData.postValue(it)
//        }
//    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
        }
    }

}