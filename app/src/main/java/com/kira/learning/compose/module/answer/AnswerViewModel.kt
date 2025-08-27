package com.kira.learning.compose.module.answer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay

@HiltViewModel
class AnswerViewModel @Inject constructor(
    private val repo: AnswerRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AnswerUiState())
    val uiState: StateFlow<AnswerUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val list = repo.fetchQuestions()
            _uiState.update { it.copy(questions = list, loading = false) }
            startTimer()
        }
    }

    private fun startTimer() = viewModelScope.launch {
        while (isActive) {
            delay(1000)
            _uiState.update { s -> if (s.submitted) s else s.copy(elapsedSeconds = s.elapsedSeconds + 1) }
        }
    }

    fun updateSingle(questionId: String, idx: Int) = _uiState.update { s ->
        s.copy(answers = s.answers + (questionId to SingleChoiceUserAnswer(questionId, idx)))
    }
    fun toggleMulti(questionId: String, idx: Int) = _uiState.update { s ->
        val old = (s.answers[questionId] as? MultiChoiceUserAnswer)?.selected ?: emptySet()
        val new = old.toMutableSet().apply { if (!add(idx)) remove(idx) }
        s.copy(answers = s.answers + (questionId to MultiChoiceUserAnswer(questionId, new)))
    }
    fun updateShort(questionId: String, content: String) = _uiState.update { s ->
        s.copy(answers = s.answers + (questionId to ShortAnswerUserAnswer(questionId, content)))
    }
    fun updateFillBlank(questionId: String, index: Int, content: String) = _uiState.update { s ->
        val q = s.questions.find { it.id == questionId } as? FillBlankQuestion ?: return
        val current = (s.answers[questionId] as? FillBlankUserAnswer)?.blanks?.toMutableList() ?: MutableList(q.answers.size){""}
        if (index in current.indices) current[index] = content
        s.copy(answers = s.answers + (questionId to FillBlankUserAnswer(questionId, current)))
    }
    fun updateOpen(questionId: String, content: String) = _uiState.update { s ->
        s.copy(answers = s.answers + (questionId to OpenExtUserAnswer(questionId, content)))
    }

    fun nextQuestion() = _uiState.update { s ->
        s.copy(currentIndex = (s.currentIndex + 1).coerceAtMost(s.questions.lastIndex))
    }
    fun prevQuestion() = _uiState.update { s ->
        s.copy(currentIndex = (s.currentIndex - 1).coerceAtLeast(0))
    }
    fun jumpTo(index: Int) = _uiState.update { s ->
        s.copy(currentIndex = index.coerceIn(0, s.questions.lastIndex))
    }

    fun requestSubmit() = _uiState.update { it.copy(showSubmitConfirm = true) }
    fun dismissSubmitConfirm() = _uiState.update { it.copy(showSubmitConfirm = false) }

    fun submitConfirmed() {
        viewModelScope.launch {
            _uiState.update { s ->
                val results = s.questions.associate { q -> q.id to AnswerEvaluator.evaluate(q, s.answers[q.id]) }
                s.copy(results = results, submitted = true, showSubmitConfirm = false)
            }
        }
    }
}

data class AnswerUiState(
    val loading: Boolean = false,
    val questions: List<QuestionBase> = emptyList(),
    val answers: Map<String, UserAnswer> = emptyMap(),
    val results: Map<String, MarkResult> = emptyMap(),
    val submitted: Boolean = false,
    val currentIndex: Int = 0,
    val elapsedSeconds: Int = 0,
    val showSubmitConfirm: Boolean = false,
) {
    val answeredCount: Int get() = answers.size
}
