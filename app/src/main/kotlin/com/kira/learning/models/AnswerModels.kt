package com.kira.learning.models

import androidx.compose.runtime.Immutable

enum class QuestionType { SINGLE_CHOICE, MULTI_CHOICE, SHORT_ANSWER, FILL_BLANK, OPEN_EXT }

@Immutable
sealed interface QuestionBase { val id: String; val stem: String; val imageUrls: List<String>; val type: QuestionType }

@Immutable
data class SingleChoiceQuestion(
    override val id: String,
    override val stem: String,
    val options: List<String>,
    val answerIndex: Int,
    override val imageUrls: List<String> = emptyList(),
) : QuestionBase { override val type = QuestionType.SINGLE_CHOICE }

@Immutable
data class MultiChoiceQuestion(
    override val id: String,
    override val stem: String,
    val options: List<String>,
    val answerIndexes: Set<Int>,
    override val imageUrls: List<String> = emptyList(),
) : QuestionBase { override val type = QuestionType.MULTI_CHOICE }

@Immutable
data class ShortAnswerQuestion(
    override val id: String,
    override val stem: String,
    val reference: String? = null,
    override val imageUrls: List<String> = emptyList(),
) : QuestionBase { override val type = QuestionType.SHORT_ANSWER }

@Immutable
data class FillBlankQuestion(
    override val id: String,
    override val stem: String,
    val answers: List<String>,
    override val imageUrls: List<String> = emptyList(),
) : QuestionBase { override val type = QuestionType.FILL_BLANK }

@Immutable
data class OpenExtQuestion(
    override val id: String,
    override val stem: String,
    val guide: String? = null,
    override val imageUrls: List<String> = emptyList(),
) : QuestionBase { override val type = QuestionType.OPEN_EXT }

sealed interface UserAnswer { val questionId: String }

data class SingleChoiceUserAnswer(override val questionId: String, val selected: Int?) : UserAnswer

data class MultiChoiceUserAnswer(override val questionId: String, val selected: Set<Int>) : UserAnswer

data class ShortAnswerUserAnswer(override val questionId: String, val content: String) : UserAnswer

data class FillBlankUserAnswer(override val questionId: String, val blanks: List<String>) : UserAnswer

data class OpenExtUserAnswer(override val questionId: String, val content: String) : UserAnswer

sealed interface MarkResult { val questionId: String; val correct: Boolean? }

data class ObjectiveMarkResult(
    override val questionId: String,
    override val correct: Boolean,
    val score: Int,
    val total: Int,
) : MarkResult

data class SubjectiveMarkResult(
    override val questionId: String,
    val reference: String?,
) : MarkResult { override val correct: Boolean? = null }

object AnswerEvaluator {
    fun evaluate(question: QuestionBase, ua: UserAnswer?): MarkResult = when (question) {
        is SingleChoiceQuestion -> {
            val ok = ua is SingleChoiceUserAnswer && ua.selected == question.answerIndex
            ObjectiveMarkResult(question.id, ok, if (ok) 1 else 0, 1)
        }
        is MultiChoiceQuestion -> {
            val total = question.answerIndexes.size
            val sel = (ua as? MultiChoiceUserAnswer)?.selected ?: emptySet()
            val correctChosen = sel.count { it in question.answerIndexes }
            val wrongChosen = sel.count { it !in question.answerIndexes }
            val score = (correctChosen - wrongChosen).coerceAtLeast(0)
            ObjectiveMarkResult(question.id, score == total && wrongChosen == 0, score, total)
        }
        is FillBlankQuestion -> {
            val total = question.answers.size
            val blanks = (ua as? FillBlankUserAnswer)?.blanks ?: emptyList()
            var score = 0
            question.answers.forEachIndexed { i, ans ->
                if (blanks.getOrNull(i)?.trim().equals(ans.trim(), ignoreCase = true)) score++
            }
            ObjectiveMarkResult(question.id, score == total, score, total)
        }
        is ShortAnswerQuestion -> SubjectiveMarkResult(question.id, question.reference)
        is OpenExtQuestion -> SubjectiveMarkResult(question.id, question.guide)
    }
}

