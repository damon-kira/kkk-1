package com.kira.learning.bean

sealed class SolutionItem {
    // Final Answer 类型
    data class FinalAnswer(
        val title: String,
        val equations: List<String>,
        val conclusion: String
    ) : SolutionItem()

    // Question 类型
    data class Question(
        val title: String,
        val steps: List<Step>
    ) : SolutionItem()

    data class Step(
        val stepTitle: String,
        val content: String
    )
}