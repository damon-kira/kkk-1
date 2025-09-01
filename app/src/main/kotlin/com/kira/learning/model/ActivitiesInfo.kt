package com.kira.learning.model

data class ActivitiesInfo(
    val owner: String,
    val deleted: Boolean,
    val lesson: Lesson
)

data class Lesson(
    val description: String,
    val id: String,
    val steps: List<LessonStep>
)

data class LessonStep(
    val type: String,
    val content: List<ColumnBlock>,
    val attrs: StepAttrs
)

data class ColumnBlock(
    val type: String,
    val content: List<Column>,
    val attrs: ColumnBlockAttrs
)

data class Column(
    val type: String,
    val attrs: ColumnAttrs,
    val content: List<ColumnContent>
)

sealed class ColumnContent

data class Heading(
    val type: String,
    val content: List<Text>,
    val attrs: HeadingAttrs
) : ColumnContent()

data class ActivityGroup(
    val type: String,
    val content: List<Activity>,
    val attrs: ActivityGroupAttrs
) : ColumnContent()

data class KiraImage(
    val type: String,
    val attrs: KiraImageAttrs
) : ColumnContent()

data class Paragraph(
    val type: String,
    val content: List<Text>,
    val attrs: ParagraphAttrs
) : ColumnContent()

sealed class Activity

data class FreeResponse(
    val type: String,
    val content: List<FreeResponseContent>,
    val attrs: FreeResponseAttrs
) : Activity()

sealed class FreeResponseContent

data class FreeResponseQuestion(
    val type: String,
    val content: List<Paragraph>,
    val attrs: FreeResponseQuestionAttrs
) : FreeResponseContent()

data class FreeResponseAnswer(
    val type: String,
    val content: List<Paragraph>,
    val attrs: FreeResponseAnswerAttrs
) : FreeResponseContent()

data class Essay(
    val type: String,
    val content: List<EssayContent>,
    val attrs: EssayAttrs
) : Activity()

sealed class EssayContent

data class EssayHeader(
    val type: String,
    val content: List<Text>,
    val attrs: EssayHeaderAttrs
) : EssayContent()

data class EssayBody(
    val type: String,
    val content: List<Paragraph>,
    val attrs: EssayBodyAttrs
) : EssayContent()

data class MultipleChoice(
    val type: String,
    val attrs: MultipleChoiceAttrs
) : Activity()

data class StepAttrs(
    val id: String,
    val isNew: Boolean
)

data class ColumnBlockAttrs(
    val id: String
)

data class ColumnAttrs(
    val id: String
)

data class HeadingAttrs(
    val id: String,
    val textAlign: String,
    val level: Int,
    val width: String
)

data class ActivityGroupAttrs(
    val id: String,
    val isNew: Boolean,
    val groupType: String
)

data class FreeResponseAttrs(
    val id: String,
    val mode: String,
    val version: String,
    val questionText: String
)

data class FreeResponseQuestionAttrs(
    val content: String,
    val version: String
)

data class FreeResponseAnswerAttrs(
    val mode: String,
    val content: String,
    val version: String
)

data class EssayAttrs(
    val type: String,
    val heading: HeadingInfo,
    val version: String
)

data class HeadingInfo(
    val text: String
)

data class EssayHeaderAttrs(
    val url: String,
    val text: String
)

data class EssayBodyAttrs(
    val type: String
)

data class MultipleChoiceAttrs(
    val id: String,
    val points: Int,
    val choices: List<Choice>,
    val version: String,
    val questionText: String,
    val isMultipleAnswers: Boolean
)

data class Choice(
    val id: String,
    val text: String,
    val isCorrect: Boolean
)

data class KiraImageAttrs(
    val src: String,
    val alt: String,
    val srcType: String,
    val width: String,
    val cover: String,
    val isLoading: Boolean
)

data class ParagraphAttrs(
    val id: String,
    val level: Int,
    val width: String,
    val textAlign: String
)

data class Text(
    val type: String,
    val text: String
)