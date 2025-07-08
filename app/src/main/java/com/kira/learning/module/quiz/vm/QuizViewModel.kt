package com.kira.learning.module.quiz.vm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.Document
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.module.quiz.repo.QuizRepository
import javax.inject.Inject


class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
) : BaseViewModel(), LifecycleEventObserver {
    // 使用LiveData管理当前题目位置
    val datas = generatorLiveData<BaseResponse<Document>>()
    private val _currentPosition = MutableLiveData(0)
    val currentPosition: LiveData<Int> = _currentPosition

    fun getQuizInfo() {
        showloading()
        datas.addSourceLiveData(
            repository.searchQuestion("")
        ) {

            hideLoading()
            datas.postValue(it)
        }
    }

    // 题目数据源（示例数据）
//    val questions = listOf(
//        QuizInfo.SingleChoice(
//            id = 1,
//            content = "以下哪个是最大的行星？<br/><b>A.</b> 地球<br/><b>B.</b> 木星",
//            imageUrl = "https://example.com/planet.jpg",
//            options = listOf("A. 地球", "B. 木星"),
//            answer = 1
//        ),
//        QuizInfo.MultipleChoice(
//            id = 2,
//            content = "选择所有正确的说法：",
//            options = listOf("1+1=2", "地球是方的", "水在0℃结冰"),
//            imageUrl = "",
//            answers = setOf(0, 2)
//        ),
//        QuizInfo.TrueFalse(
//            id = 3,
//            content = "太阳从西边升起",
//            imageUrl = "",
//            answer = false
//        )
//    )

    // 更新当前题目位置
    fun setPosition(position: Int) {
        _currentPosition.value = position
    }

    fun saveAnswer(position: Int, answer: Any) {
//        datas[position] = answer
    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
        }
    }
}