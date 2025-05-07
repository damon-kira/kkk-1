package com.colombia.credit.module.python

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.colombia.credit.module.python.PythonExecutor.ExecutionResult
import com.steve28.stedit.SteditField
import com.steve28.stedit.highlighter.PythonHighlighter

class CodingActivity : AppCompatActivity() {
    private val stedit by lazy {


        SteditField().apply {
            text = """
def add_multiple_numbers(*args):
    ""${'"'}计算任意数量数字的和""${'"'}
    return sum(args)

# 示例
print(add_multiple_numbers(1, 2, 3))       # 输出: 6
print(add_multiple_numbers(1.5, 2.5, 4))   # 输出: 8.0
        """.trimIndent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Main()
                }
            }
        }
    }

    @Composable
    fun Main() {
        Column {
            stedit.Compose(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                highlighter = PythonHighlighter
            )
            Space(16)
            Button({

                val code = stedit.text

                val result = PythonExecutor.execute(code)

                val endResult = buildString {
                    append("=== 执行结果 ===\n")
                    when (result) {
                        is PythonExecutor.ExecutionResult.Success -> {
                            append("输出:\n${result.output}\n")
                            if (result.result != null) {
                                append("返回值: ${result.result}")
                            }
                        }

                        is PythonExecutor.ExecutionResult.Error -> {
                            append("错误: ${result.message}")
                        }
                    }
                }
                Log.e("PythonLog", "结果：${endResult}")
            }) {
                Text("测试!")
            }
            Button({
                val result = executePythonCode(stedit.text)
                if (result is ExecutionResult.Success) {
                    Log.e("PythonLog", "结果：${result.result.toString()}")
                    Log.e("PythonLog", "结果：${result.output.toString()}")
                }
                if (result is ExecutionResult.Error) {
                    Log.e("PythonLog", "错误：${result.message.toString()}")
                }
            }) {
                Text("测试222")
            }
        }

    }

    @Composable
    fun Space(size: Int) {
        Spacer(modifier = Modifier.height(size.dp))
    }

    fun executePythonCode(code: String): ExecutionResult {
        return try {
            val python = Python.getInstance()
            val module = python.getModule("safe_exec")

            // 显式类型转换
            val resultObj = module.callAttr("safe_exec", code).asMap() as Map<String, Any>

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