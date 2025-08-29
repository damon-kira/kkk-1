package com.kira.learning.bean

sealed class QuizInfo(
    val id: Int,
    val type: Int,       // 题目类型：1-单选，2-多选，3-判断
    val content: String, // 题目内容（支持HTML）
    val imageUrl: String? = null // 图片URL（可选）
) {
    // 单选题
    class SingleChoice(
        id: Int,
        content: String,
        imageUrl: String?,
        val options: List<String>, // 选项列表
        val answer: Int           // 正确答案索引
    ) : QuizInfo(id, 1, content, imageUrl)

    // 多选题
    class MultipleChoice(
        id: Int,
        content: String,
        imageUrl: String?,
        val options: List<String>,
        val answers: Set<Int>     // 正确答案索引集合
    ) : QuizInfo(id, 2, content, imageUrl)

    // 判断题
    class TrueFalse(
        id: Int,
        content: String,
        imageUrl: String?,
        val answer: Boolean       // 正确答案
    ) : QuizInfo(id, 3, content, imageUrl)
}