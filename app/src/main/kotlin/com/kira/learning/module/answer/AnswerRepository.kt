package com.kira.learning.module.answer

import com.kira.learning.model.FillBlankQuestion
import com.kira.learning.model.MultiChoiceQuestion
import com.kira.learning.model.OpenExtQuestion
import com.kira.learning.model.QuestionBase
import com.kira.learning.model.ShortAnswerQuestion
import com.kira.learning.model.SingleChoiceQuestion
import com.kira.learning.network.ApiResult
import com.kira.learning.base.repository.BaseRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

interface AnswerRepository {
    suspend fun fetchQuestions(): ApiResult<List<QuestionBase>>
}

class MockAnswerRepository @Inject constructor() : BaseRepository(), AnswerRepository {
    override suspend fun fetchQuestions(): ApiResult<List<QuestionBase>> = safeApiCall {
        delay(500) // 模拟网络
        listOf(
            SingleChoiceQuestion(
                id = "q1",
                stem = "下列哪一个是 **Kotlin** 中用于声明不可变变量的关键字?",
                options = listOf("var", "val", "let", "const"),
                answerIndex = 1,
                imageUrls = listOf("https://placekitten.com/240/240")
            ),
            MultiChoiceQuestion(
                id = "q2",
                stem = "选择所有属于协程构建器的函数 (支持部分分):",
                options = listOf("launch", "async", "map", "withContext"),
                answerIndexes = setOf(0, 1, 3),
                imageUrls = emptyList()
            ),
            ShortAnswerQuestion(
                id = "q3",
                stem = "解释 `suspend` 关键字的作用。",
                reference = "标记可挂起函数, 不阻塞线程即可挂起/恢复。",
                imageUrls = listOf("https://placekitten.com/200/200")
            ),
            FillBlankQuestion(
                id = "q4",
                stem = "Compose 使用 __ 模型来驱动 UI, 并以 __ 为界限进行重组。",
                answers = listOf("声明式", "Composable 函数"),
                imageUrls = emptyList()
            ),
            OpenExtQuestion(
                id = "q5",
                stem = "请设计一个你认为理想的在线刷题交互, 并说明理由。包含交互流, 激励, 无障碍, 数据反馈, 个性化五方面。",
                guide = "结构化: 1) 概述 2) 关键交互 3) 反馈 4) 可访问性 5) 个性化。",
                imageUrls = emptyList()
            )
        )
    }
}
