package com.colombia.credit.module.python
import android.content.Context
import com.chaquo.python.Python
import com.chaquo.python.PyException
import com.chaquo.python.android.AndroidPlatform

object PythonExecutor {
    private var initialized = false

    sealed class ExecutionResult {
        data class Success(
            val output: String,
            val result: String?
        ) : ExecutionResult()

        data class Error(
            val message: String
        ) : ExecutionResult()
    }

    fun initialize(context: Context) {
        if (!initialized && !Python.isStarted()) {
            Python.start(AndroidPlatform(context))
            initialized = true
        }
    }

    fun execute(code: String): ExecutionResult {
        return try {
            val python = Python.getInstance()
            val module = python.getModule("safe_exec")

            // 显式类型转换
            val resultObj: Map<String, Any> = module.callAttr("safe_exec", code).asMap() as Map<String, Any>

            when (val error = resultObj["error"]?.toString()) {
                null, "" -> ExecutionResult.Success(
                    output = resultObj["output"].toString(),
                    result = resultObj["result"]?.toString()
                )
                else -> ExecutionResult.Error(error)
            }
        } catch (e: PyException) {
            ExecutionResult.Error(
                when (e.message?.contains("SecurityError") == true) {
                    true -> e.message ?: "安全规则冲突"
                    else -> "Python错误: ${e.message}"
                }
            )
        }
    }
}