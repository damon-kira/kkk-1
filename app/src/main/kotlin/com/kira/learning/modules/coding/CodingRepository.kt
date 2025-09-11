package com.kira.learning.modules.coding

import com.kira.learning.network.ApiResult
import com.kira.learning.network.ComposeApiService
import com.kira.learning.network.safeApiCall
import com.kira.learning.base.repository.BaseRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CodingRepository @Inject constructor(
    private val apiService: ComposeApiService
) : BaseRepository() {

    suspend fun executeCode(request: CodeExecutionRequest): ApiResult<CodeExecutionResponse> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // val response = apiService.executeCode(request)
            // return response.toCodeExecutionResponse()

            // 目前使用模拟数据
            simulateCodeExecution(request)
        }
    }

    private suspend fun simulateCodeExecution(request: CodeExecutionRequest): CodeExecutionResponse {
        // 模拟网络延迟
        delay(1000)

        return when (request.language.lowercase()) {
            "python" -> simulatePythonExecution(request.code, request.input)
            "java" -> simulateJavaExecution(request.code, request.input)
            "kotlin" -> simulateKotlinExecution(request.code, request.input)
            "javascript" -> simulateJavaScriptExecution(request.code, request.input)
            else -> CodeExecutionResponse(
                output = "Language ${request.language} is not supported yet",
                error = "Unsupported language"
            )
        }
    }

    private fun simulatePythonExecution(code: String, input: String): CodeExecutionResponse {
        return when {
            code.contains("print") -> {
                val output = if (code.contains("input()") && input.isNotEmpty()) {
                    "Enter input: $input\nResult: ${simulateCalculation(input)}"
                } else {
                    "Hello, World from Python!"
                }
                CodeExecutionResponse(output = output, executionTime = 245)
            }
            code.contains("def") -> {
                CodeExecutionResponse(
                    output = "Function defined successfully\n${if (input.isNotEmpty()) "Input: $input\nResult: ${simulateCalculation(input)}" else ""}",
                    executionTime = 180
                )
            }
            else -> CodeExecutionResponse(
                output = "Code executed successfully",
                executionTime = 120
            )
        }
    }

    private fun simulateJavaExecution(code: String, input: String): CodeExecutionResponse {
        return if (code.contains("System.out.println")) {
            CodeExecutionResponse(
                output = "Hello, World from Java!\n${if (input.isNotEmpty()) "Input: $input" else ""}",
                executionTime = 890
            )
        } else {
            CodeExecutionResponse(
                output = "Java program compiled and executed successfully",
                executionTime = 1200
            )
        }
    }

    private fun simulateKotlinExecution(code: String, input: String): CodeExecutionResponse {
        return if (code.contains("println")) {
            CodeExecutionResponse(
                output = "Hello, World from Kotlin!\n${if (input.isNotEmpty()) "Input: $input" else ""}",
                executionTime = 678
            )
        } else {
            CodeExecutionResponse(
                output = "Kotlin program executed successfully",
                executionTime = 450
            )
        }
    }

    private fun simulateJavaScriptExecution(code: String, input: String): CodeExecutionResponse {
        return if (code.contains("console.log")) {
            CodeExecutionResponse(
                output = "Hello, World from JavaScript!\n${if (input.isNotEmpty()) "Input: $input" else ""}",
                executionTime = 89
            )
        } else {
            CodeExecutionResponse(
                output = "JavaScript executed successfully",
                executionTime = 156
            )
        }
    }

    private fun simulateCalculation(input: String): String {
        return try {
            when {
                input.contains("+") -> {
                    val parts = input.split("+")
                    if (parts.size == 2) {
                        val result = parts[0].trim().toDouble() + parts[1].trim().toDouble()
                        result.toString()
                    } else "Invalid calculation"
                }
                input.contains("-") -> {
                    val parts = input.split("-")
                    if (parts.size == 2) {
                        val result = parts[0].trim().toDouble() - parts[1].trim().toDouble()
                        result.toString()
                    } else "Invalid calculation"
                }
                input.contains("*") -> {
                    val parts = input.split("*")
                    if (parts.size == 2) {
                        val result = parts[0].trim().toDouble() * parts[1].trim().toDouble()
                        result.toString()
                    } else "Invalid calculation"
                }
                else -> "Processed: $input"
            }
        } catch (e: Exception) {
            "Error processing input: ${e.message}"
        }
    }
}
