package com.common.lib.net.bean

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

// 2. 主数据结构
data class Document(
    @SerializedName("owner") val owner: String,
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("lesson") val lesson: Lesson
)

data class Lesson(
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("steps") val steps: List<Step>,
    @SerializedName("title") val title: String,
    @SerializedName("themeSettings") val themeSettings: ThemeSettings
)

data class ThemeSettings(
    @SerializedName("theme") val theme: String,
    @SerializedName("font") val font: String
)

// 3. 步骤数据结构
data class Step(
    @SerializedName("type") val type: String,
    @SerializedName("content") val content: List<ContentItem>,
    @SerializedName("attrs") val attrs: StepAttrs
)

data class StepAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("isNew") val isNew: Boolean
)

// 4. 内容项多态处理（密封类）
sealed class ContentItem {
    @get:SerializedName("type")
    abstract val type: String
}

// 4.1 列块类型
data class ColumnBlock(
    @SerializedName("type") override val type: String = "columnBlock",
    @SerializedName("content") val columns: List<Column>,
    @SerializedName("attrs") val attrs: ColumnBlockAttrs
) : ContentItem()

data class ColumnBlockAttrs(@SerializedName("id") val id: String)

// 4.2 列类型
data class Column(
    @SerializedName("type") val type: String = "column",
    @SerializedName("attrs") val attrs: ColumnAttrs,
    @SerializedName("content") val content: List<ColumnContent>
)

data class ColumnAttrs(@SerializedName("id") val id: String)

// 5. 列内容多态处理
sealed class ColumnContent {
    @get:SerializedName("type")
    abstract val type: String
}

// 5.1 标题
data class Heading(
    @SerializedName("type") override val type: String = "heading",
    @SerializedName("content") val textContent: List<TextContent>,
    @SerializedName("attrs") val attrs: HeadingAttrs
) : ColumnContent()

data class HeadingAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("textAlign") val textAlign: String,
    @SerializedName("level") val level: Int,
    @SerializedName("width") val width: String
)

// 5.2 段落
data class Paragraph(
    @SerializedName("type") override val type: String = "paragraph",
    @SerializedName("content") val textContent: List<TextContent>,
    @SerializedName("attrs") val attrs: ParagraphAttrs
) : ColumnContent()

data class ParagraphAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("level") val level: Int,
    @SerializedName("width") val width: String,
    @SerializedName("textAlign") val textAlign: String
)

// 5.3 图片
data class KiraImage(
    @SerializedName("type") override val type: String = "kiraImage",
    @SerializedName("attrs") val attrs: ImageAttrs
) : ColumnContent()

data class ImageAttrs(
    @SerializedName("src") val src: String,
    @SerializedName("alt") val alt: String,
    @SerializedName("srcType") val srcType: String,
    @SerializedName("width") val width: String,
    @SerializedName("cover") val cover: String,
    @SerializedName("isLoading") val isLoading: Boolean
)

// 5.4 活动组
data class ActivityGroup(
    @SerializedName("type") override val type: String = "activityGroup",
    @SerializedName("content") val activities: List<Activity>,
    @SerializedName("attrs") val attrs: ActivityGroupAttrs
) : ColumnContent()

data class ActivityGroupAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("isNew") val isNew: Boolean,
    @SerializedName("groupType") val groupType: String?
)

// 6. 活动多态处理
sealed class Activity {
    @get:SerializedName("type")
    abstract val type: String
}

// 6.1 自由回答活动
data class FreeResponseActivity(
    @SerializedName("type") override val type: String = "freeResponse",
    @SerializedName("content") val questions: List<FreeResponseContent>,
    @SerializedName("attrs") val attrs: FreeResponseAttrs
) : Activity()

data class FreeResponseContent(
    @SerializedName("type") val contentType: String,
    @SerializedName("content") val content: List<FreeResponseInnerContent>?,
    @SerializedName("attrs") val attrs: FreeResponseContentAttrs
)

data class FreeResponseInnerContent(
    @SerializedName("type") val type: String,
    @SerializedName("text") val text: String?
)

data class FreeResponseAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("mode") val mode: String,
    @SerializedName("version") val version: String,
    @SerializedName("questionText") val questionText: String
)

data class FreeResponseContentAttrs(
    @SerializedName("content") val content: String?,
    @SerializedName("version") val version: String?,
    @SerializedName("mode") val mode: String?
)

// 6.2 作文活动
data class EssayActivity(
    @SerializedName("type") override val type: String = "essay",
    @SerializedName("content") val sections: List<EssaySection>,
    @SerializedName("attrs") val attrs: EssayAttrs
) : Activity()

data class EssaySection(
    @SerializedName("type") val type: String,
    @SerializedName("content") val content: List<JsonElement>?,
    @SerializedName("attrs") val attrs: EssaySectionAttrs?
)

data class EssayAttrs(
    @SerializedName("type") val type: String,
    @SerializedName("heading") val heading: HeadingInfo,
    @SerializedName("version") val version: String
)

data class HeadingInfo(@SerializedName("text") val text: String)

data class EssaySectionAttrs(
    @SerializedName("url") val url: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("type") val type: String?
)

// 6.3 多选题活动
data class MultipleChoiceActivity(
    @SerializedName("type") override val type: String = "multipleChoice",
    @SerializedName("attrs") val attrs: MultipleChoiceAttrs
) : Activity()

data class MultipleChoiceAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("points") val points: Int,
    @SerializedName("choices") val choices: List<Choice>,
    @SerializedName("version") val version: String,
    @SerializedName("questionText") val questionText: String,
    @SerializedName("isMultipleAnswers") val isMultipleAnswers: Boolean
)

data class Choice(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("isCorrect") val isCorrect: Boolean
)

// 6.4 填空题活动
data class FitBActivity(
    @SerializedName("type") override val type: String = "fitB",
    @SerializedName("content") val question: List<FitBQuestion>,
    @SerializedName("attrs") val attrs: FitBAttrs
) : Activity()

data class FitBQuestion(
    @SerializedName("type") val type: String,
    @SerializedName("content") val content: List<FitBText>,
    @SerializedName("attrs") val attrs: FitBQuestionAttrs
)

data class FitBText(
    @SerializedName("type") val type: String,
    @SerializedName("text") val text: String
)

data class FitBAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("answer") val answer: List<Any>, // 根据实际使用调整
    @SerializedName("question") val question: String
)

data class FitBQuestionAttrs(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String
)

// 7. 文本内容类型
data class TextContent(
    @SerializedName("type") val type: String,
    @SerializedName("text") val text: String
)

// 8. Gson多态适配器
class ContentItemAdapter : JsonDeserializer<ContentItem> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ContentItem {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val type = jsonObject.get("type").asString

        return when (type) {
            "columnBlock" -> context?.deserialize<ColumnBlock>(json, ColumnBlock::class.java)
            else -> throw JsonParseException("Unknown content type: $type")
        } ?: throw JsonParseException("Failed to parse content item")
    }
}

class ColumnContentAdapter : JsonDeserializer<ColumnContent> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ColumnContent {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val type = jsonObject.get("type").asString

        return when (type) {
            "heading" -> context?.deserialize<Heading>(json, Heading::class.java)
            "paragraph" -> context?.deserialize<Paragraph>(json, Paragraph::class.java)
            "kiraImage" -> context?.deserialize<KiraImage>(json, KiraImage::class.java)
            "activityGroup" -> context?.deserialize<ActivityGroup>(json, ActivityGroup::class.java)
            else -> throw JsonParseException("Unknown column content type: $type")
        } ?: throw JsonParseException("Failed to parse column content")
    }
}

class ActivityAdapter : JsonDeserializer<Activity> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Activity {
        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Invalid JSON")
        val type = jsonObject.get("type").asString

        return when (type) {
            "freeResponse" -> context?.deserialize<FreeResponseActivity>(
                json,
                FreeResponseActivity::class.java
            )

            "essay" -> context?.deserialize<EssayActivity>(json, EssayActivity::class.java)
            "multipleChoice" -> context?.deserialize<MultipleChoiceActivity>(
                json,
                MultipleChoiceActivity::class.java
            )

            "fitB" -> context?.deserialize<FitBActivity>(json, FitBActivity::class.java)
            else -> throw JsonParseException("Unknown activity type: $type")
        } ?: throw JsonParseException("Failed to parse activity")
    }
}