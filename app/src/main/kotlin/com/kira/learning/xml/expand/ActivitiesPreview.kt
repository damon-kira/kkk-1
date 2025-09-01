package com.kira.learning.xml.expand

import com.common.lib.net.bean.Activity
import com.common.lib.net.bean.ActivityGroup
import com.common.lib.net.bean.ColumnBlock
import com.common.lib.net.bean.ColumnContent
import com.common.lib.net.bean.Document
import com.common.lib.net.bean.EssayActivity
import com.common.lib.net.bean.FitBActivity
import com.common.lib.net.bean.FreeResponseActivity
import com.common.lib.net.bean.Heading
import com.common.lib.net.bean.KiraImage
import com.common.lib.net.bean.MultipleChoiceActivity
import com.common.lib.net.bean.Paragraph
import com.util.lib.log.logger_d
import kotlin.collections.forEach

fun processDocument(document: Document?) {

    var index = 1
    document?.lesson?.steps?.forEach { step ->
        logger_d("Activities    ", " ")
        logger_d("Activities    ", " ")
        logger_d("Activities    ", "Processing step: 第 ${index} Step")
        logger_d("Activities    ", "Processing step: 类型-${step.type}")

        step.content.forEach { contentItem ->
            when (contentItem) {
                is ColumnBlock -> {
                    logger_d("Activities    ", "当前BlockType: ${contentItem.type}")
                    contentItem.columns.forEach { column ->
                        logger_d(
                            "Activities    ",
                            "type: ${column.type}  本Content数量: ${column.content.size}"
                        )
                        processColumnContent(column.content)
                    }
                }
                // 添加其他ContentItem类型的处理
            }
        }
        index++
    }
}

// 常规元素解析（非Activities）
fun processColumnContent(contentList: List<ColumnContent>) {
    contentList.forEach { content ->
        when (content) {
            // 标题
            is Heading -> {
                logger_d(
                    "Activities    ",
                    "-其他（标题）Heading: ${content.textContent.joinToString { it.text }}"
                )
                logger_d(
                    "Activities    ",
                    "     Level: ${content.attrs.level}, Align: ${content.attrs.textAlign}"
                )
            }
            // 段落
            is Paragraph -> {
                logger_d(
                    "Activities    ",
                    "-其他（段落）Paragraph: ${content.textContent.joinToString { it.text }}"
                )
            }
            // 活动组
            is ActivityGroup -> {
                logger_d(
                    "Activities    ",
                    "-活动组：Activity type: ${content.type} 活动数量: ${content.activities.size}"
                )
                content.activities.forEach { activity ->
                    processActivity(activity)
                }
            }
            // 图片
            is KiraImage -> {
                logger_d("Activities    ", "-图片-Image: ${content.attrs.alt}")
                logger_d("Activities    ", "         -Source: ${content.attrs.src}")
            }
            // 添加其他ColumnContent类型的处理
        }
    }
}

fun processActivity(activity: Activity) {
    when (activity) {
        // 自由回答活动
        is FreeResponseActivity -> {
            logger_d("Activities    ", "     自由回答活动-Free Response Activity )")
            logger_d("Activities    ", "         题目-Question: ${activity.attrs.questionText}")
            activity.questions.forEach { question ->
                logger_d(
                    "Activities    ",
                    "             其他内容-${question.contentType}: ${question.content?.joinToString { it.type.toString() }}"
                )
            }
        }
        // 简答题活动
        is EssayActivity -> {
            logger_d("Activities    ", "     文章类活动-Essay Activity")
            logger_d("Activities    ", "         标题-Title: ${activity.attrs.heading.text}")
            activity.sections.forEach { section ->
                logger_d(
                    "Activities    ",
                    "             其他内容-${section.type}: ${section.attrs?.text ?: section.attrs?.url}"
                )
            }
        }
        // 多选题活动
        is MultipleChoiceActivity -> {
            logger_d(
                "Activities    ",
                "     选择题-Multiple Choice Activity （${if (activity.attrs.isMultipleAnswers) "多选题" else "单选题"}）"
            )
            logger_d("Activities    ", "         题目-Question: ${activity.attrs.questionText}")
            activity.attrs.choices.forEach { choice ->
                logger_d(
                    "Activities    ",
                    "             ${choice.text} - ${if (choice.isCorrect) "Correct" else "Incorrect"}"
                )
            }
        }
        // 添加其他Activity类型的处理
        is FitBActivity -> {
            logger_d("Activities    ", "     填空题-Fill-in-the-Blank Activity")
            activity.question.forEach { question ->
                logger_d(
                    "Activities    ",
                    "         题目-Question: ${question.content.joinToString { it.text }}"
                )
            }
        }
    }
}