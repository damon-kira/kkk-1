package com.kira.learning.module.codeeditor

import android.content.Context
import com.cache.lib.getContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.regex.Pattern

class CodeExecutor(context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var process: Process? = null
    private lateinit var inputWriter: OutputStreamWriter
    private var outputJob: Job? = null
    private val listeners = mutableListOf<OutputListener>()

    interface OutputListener {
        fun onOutput(output: String)
        fun onVisualData(data: Float)
    }

    fun addOutputListener(listener: OutputListener) {
        listeners.add(listener)
    }

    fun removeOutputListener(listener: OutputListener) {
        listeners.remove(listener)
    }

    fun execute(code: String, language: String) {
        stopExecution() // 停止任何正在运行的进程

        scope.launch {
            try {
                // 创建临时文件保存代码
                val tempFile = File(getContext().filesDir, "temp.$language")
                tempFile.writeText(code)

                // 创建执行命令
                val command = when (language) {
                    "python" -> "python ${tempFile.absolutePath}"
                    "java" -> "java ${tempFile.absolutePath}"
                    "js" -> "node ${tempFile.absolutePath}"
                    else -> throw UnsupportedOperationException("Unsupported language: $language")
                }

                process = Runtime.getRuntime().exec(command)
                inputWriter = OutputStreamWriter(process!!.outputStream)

                // 处理输出
                handleOutput(process!!.inputStream)
                handleOutput(process!!.errorStream)

                // 等待进程完成
                process?.waitFor()

            } catch (e: Exception) {
                notifyOutput("执行错误: ${e.message}\n")
            }
        }
    }

    private fun handleOutput(inputStream: InputStream) {
        outputJob = scope.launch {
            val reader = BufferedReader(InputStreamReader(inputStream))
            try {
                while (isActive) {
                    val line = withContext(Dispatchers.IO) { reader.readLine() }
                    if (line == null) break

                    notifyOutput("$line\n")

                    // 尝试提取数值用于可视化
                    extractNumbers(line)?.let { number ->
                        listeners.forEach { it.onVisualData(number) }
                    }
                }
            } catch (e: IOException) {
                if (isActive) notifyOutput("输出错误: ${e.message}\n")
            }
        }
    }

    private fun extractNumbers(line: String): Float? {
        // 匹配数字计算结果，如 "计算结果：1.0 + 2.0 = 3.0"
        val pattern = Pattern.compile("=\\s*([\\d\\.]+)$")
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
            return matcher.group(1)?.toFloatOrNull()
        }
        return null
    }

    fun sendInput(input: String) {
        scope.launch(Dispatchers.IO) {
            try {
                inputWriter.write(input)
                inputWriter.flush()
            } catch (e: Exception) {
                notifyOutput("输入错误: ${e.message}\n")
            }
        }
    }

    fun stopExecution() {
        process?.destroy()
        process = null
        outputJob?.cancel()
        outputJob = null

        try {
            inputWriter.close()
        } catch (e: Exception) {
            // 忽略
        }
    }

    private fun notifyOutput(message: String) {
        // 切换到主线程通知监听器
        scope.launch(Dispatchers.Main) {
            listeners.forEach { it.onOutput(message) }
        }
    }

    fun destroy() {
        scope.cancel()
        stopExecution()
    }
}