package com.kira.learning.xml.modules.codeeditor

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kira.learning.R
import kotlin.math.max

class ConsoleFragment : Fragment() {

    private lateinit var consoleOutput: TextView
    private val messageQueue = mutableListOf<String>()
    private val lock = Object()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_console, container, false)
        consoleOutput = view.findViewById(R.id.console_output)

        // 设置深色主题
        consoleOutput.setBackgroundColor(Color.BLACK)
        consoleOutput.setTextColor(Color.WHITE)
        consoleOutput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

        // 添加输入处理
        val inputEditText = view.findViewById<EditText>(R.id.console_input)
        view.findViewById<Button>(R.id.console_send).setOnClickListener {
            val input = inputEditText.text.toString()
            if (input.isNotEmpty()) {
                addConsoleOutput("> $input")
                (activity as? CodePlaygroundActivity)?.sendInput(input + "\n")
                inputEditText.text.clear()
            }
        }

        return view
    }

    fun addConsoleOutput(message: String) {
        synchronized(lock) {
            messageQueue.add(message)
            activity?.runOnUiThread {
                synchronized(lock) {
                    consoleOutput.append("$message\n")
                    // 自动滚动到底部
                    val scrollAmount = consoleOutput.layout.getLineTop(consoleOutput.lineCount) - consoleOutput.height
                    consoleOutput.scrollTo(0, max(scrollAmount, 0))
                }
            }
        }
    }

    fun clearConsole() {
        synchronized(lock) {
            messageQueue.clear()
            activity?.runOnUiThread {
                consoleOutput.text = ""
            }
        }
    }
}