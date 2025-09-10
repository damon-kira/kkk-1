package com.kira.learning.module.coding

import kotlinx.serialization.Serializable

@Serializable
data class CodeExecutionRequest(
    val code: String,
    val language: String,
    val input: String = ""
)

@Serializable
data class CodeExecutionResponse(
    val output: String,
    val error: String? = null,
    val executionTime: Long = 0
)

enum class ProgrammingLanguage(val displayName: String, val extension: String) {
    PYTHON("Python", "py"),
    JAVA("Java", "java"),
    KOTLIN("Kotlin", "kt"),
    JAVASCRIPT("JavaScript", "js"),
    CPP("C++", "cpp"),
    C("C", "c")
}
