package com.kira.learning.model

data class QuestionProcessInfo(
    val title: String = "",
    val steps: ArrayList<Step>,
    val conclusion: String = "",
) {
    data class Step(
        val stepTitle: String = "",
        val content: String = "",
        val tips: String = ""
    )
}
