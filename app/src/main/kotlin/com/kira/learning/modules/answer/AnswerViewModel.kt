package com.kira.learning.modules.answer

import androidx.lifecycle.viewModelScope
import com.kira.learning.models.AnswerEvaluator
import com.kira.learning.models.FillBlankQuestion
import com.kira.learning.models.FillBlankUserAnswer
import com.kira.learning.models.MarkResult
import com.kira.learning.models.MultiChoiceUserAnswer
import com.kira.learning.models.OpenExtUserAnswer
import com.kira.learning.models.QuestionBase
import com.kira.learning.models.ShortAnswerUserAnswer
import com.kira.learning.models.SingleChoiceUserAnswer
import com.kira.learning.models.UserAnswer
import com.kira.learning.network.ApiResult
import com.kira.learning.base.mvi.BaseApiViewModel
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.base.mvi.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay

data class AnswerUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val questions: List<QuestionBase> = emptyList(),
    val answers: Map<String, UserAnswer> = emptyMap(),
    val currentIndex: Int = 0,
    val elapsedSeconds: Int = 0,
    val submitted: Boolean = false,
    val results: Map<String, MarkResult> = emptyMap(),
    val showSubmitConfirm: Boolean = false
) : ViewState() {
    val answeredCount: Int get() = answers.size
}

sealed interface AnswerEvent : ViewEvent {
    object LoadQuestions : AnswerEvent
    object StartTimer : AnswerEvent
    object SubmitAnswers : AnswerEvent
    object Retry : AnswerEvent
    data class UpdateSingleChoice(val questionId: String, val index: Int) : AnswerEvent
    data class ToggleMultiChoice(val questionId: String, val index: Int) : AnswerEvent
    data class UpdateShortAnswer(val questionId: String, val content: String) : AnswerEvent
    data class UpdateFillBlank(val questionId: String, val index: Int, val content: String) :
        AnswerEvent

    data class UpdateOpenAnswer(val questionId: String, val content: String) : AnswerEvent
    object NextQuestion : AnswerEvent
    object PrevQuestion : AnswerEvent
    data class JumpToQuestion(val index: Int) : AnswerEvent
    object RequestSubmit : AnswerEvent
    object DismissSubmitConfirm : AnswerEvent
    object SubmitConfirmed : AnswerEvent
}

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val repo: AnswerRepository
) : BaseApiViewModel<AnswerUiState, AnswerEvent>(
    initialState = AnswerUiState()
) {

    // 为了兼容性，提供 uiState 属性
    val uiState: StateFlow<AnswerUiState> = viewState

    init {
        handleAction(AnswerEvent.LoadQuestions)
    }

    override fun handleAction(action: AnswerEvent) {
        when (action) {
            is AnswerEvent.LoadQuestions -> load()
            is AnswerEvent.StartTimer -> startTimer()
            is AnswerEvent.SubmitAnswers -> submitConfirmed()
            is AnswerEvent.Retry -> retry()
            is AnswerEvent.UpdateSingleChoice -> updateSingle(action.questionId, action.index)
            is AnswerEvent.ToggleMultiChoice -> toggleMulti(action.questionId, action.index)
            is AnswerEvent.UpdateShortAnswer -> updateShort(action.questionId, action.content)
            is AnswerEvent.UpdateFillBlank -> updateFillBlank(
                action.questionId,
                action.index,
                action.content
            )

            is AnswerEvent.UpdateOpenAnswer -> updateOpen(action.questionId, action.content)
            is AnswerEvent.NextQuestion -> nextQuestion()
            is AnswerEvent.PrevQuestion -> prevQuestion()
            is AnswerEvent.JumpToQuestion -> jumpTo(action.index)
            is AnswerEvent.RequestSubmit -> requestSubmit()
            is AnswerEvent.DismissSubmitConfirm -> dismissSubmitConfirm()
            is AnswerEvent.SubmitConfirmed -> submitConfirmed()
        }
    }

    override fun updateLoadingState(isLoading: Boolean, message: String) {
        updateState {
            copy(
                loading = isLoading,
                errorMessage = if (isLoading) null else errorMessage
            )
        }
    }

    private fun load() {
        executeApiCall(
            apiCall = { repo.fetchQuestions() },
            onSuccess = { questions ->
                updateState { copy(questions = questions, errorMessage = null) }
                startTimer()
            },
            onError = { error ->
                updateState { copy(errorMessage = error.message) }
            }
        )
    }

    private fun startTimer() = viewModelScope.launch {
        while (isActive && !currentState.submitted) {
            delay(1000)
            updateState { copy(elapsedSeconds = elapsedSeconds + 1) }
        }
    }

    // 公开方法供 UI 调用
    fun updateSingle(questionId: String, idx: Int) {
        updateState {
            copy(answers = answers + (questionId to SingleChoiceUserAnswer(questionId, idx)))
        }
    }

    fun toggleMulti(questionId: String, idx: Int) {
        updateState {
            val old = (answers[questionId] as? MultiChoiceUserAnswer)?.selected ?: emptySet()
            val new = old.toMutableSet().apply { if (!add(idx)) remove(idx) }
            copy(answers = answers + (questionId to MultiChoiceUserAnswer(questionId, new)))
        }
    }

    fun updateShort(questionId: String, content: String) {
        updateState {
            copy(answers = answers + (questionId to ShortAnswerUserAnswer(questionId, content)))
        }
    }

    fun updateFillBlank(questionId: String, index: Int, content: String) {
        updateState {
            val q = questions.find { it.id == questionId } as? FillBlankQuestion
                ?: return@updateState this
            val current = (answers[questionId] as? FillBlankUserAnswer)?.blanks?.toMutableList()
                ?: MutableList(q.answers.size) { "" }
            if (index in current.indices) current[index] = content
            copy(answers = answers + (questionId to FillBlankUserAnswer(questionId, current)))
        }
    }

    fun updateOpen(questionId: String, content: String) {
        updateState {
            copy(answers = answers + (questionId to OpenExtUserAnswer(questionId, content)))
        }
    }

    fun nextQuestion() {
        updateState {
            copy(currentIndex = (currentIndex + 1).coerceAtMost(questions.lastIndex))
        }
    }

    fun prevQuestion() {
        updateState {
            copy(currentIndex = (currentIndex - 1).coerceAtLeast(0))
        }
    }

    fun jumpTo(index: Int) {
        updateState {
            copy(currentIndex = index.coerceIn(0, questions.lastIndex))
        }
    }

    fun requestSubmit() {
        updateState { copy(showSubmitConfirm = true) }
    }

    fun dismissSubmitConfirm() {
        updateState { copy(showSubmitConfirm = false) }
    }

    fun retry() {
        handleAction(AnswerEvent.LoadQuestions)
    }

    fun submitConfirmed() {
        viewModelScope.launch {
            updateState {
                val results = questions.associate { q ->
                    q.id to AnswerEvaluator.evaluate(q, answers[q.id])
                }
                copy(
                    results = results,
                    submitted = true,
                    showSubmitConfirm = false
                )
            }
        }
    }
}
