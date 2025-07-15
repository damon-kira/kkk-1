package com.kira.learning.module.codeeditor

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import de.markusressel.kodehighlighter.language.python.PythonRuleBook
import io.github.kbiakov.codeview.editor.CodeEditorView

class CodeEditorFragment : Fragment() {

//    private lateinit var codeEditor: CodeEditor
    private lateinit var codeEditor: CodeEditorView
    private lateinit var fileName: TextView
    private lateinit var resetButton: Button
    private lateinit var runButton: Button
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 创建根布局
        val root = ConstraintLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // 创建主容器（垂直布局）
        val mainContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        }

        // 添加顶部操作栏
        createTopToolbar(requireContext(), mainContainer)

        // 添加代码标签切换
        createTabLayout(requireContext(), mainContainer)

        // 添加代码编辑器
        createCodeEditor(requireContext(), mainContainer)

        root.addView(mainContainer)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeEditor.text = getInitialPythonCode()
        // 设置初始代码
//        codeEditor.setText(getInitialPythonCode())
    }

    private fun createTopToolbar(context: Context, parent: LinearLayout) {
        // 创建工具栏容器
        val toolbar = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 8.dpToPx())
            }
            setBackgroundColor(Color.parseColor("#1E1E1E"))
        }

        // 文件名文本
        fileName = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            text = "Untitled.py"
            setTextColor(Color.parseColor("#CCCCCC"))
            textSize = 16f
        }

        // 重置按钮
        resetButton = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 8.dpToPx()
            }
            text = "Reset"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#424242"))
            setOnClickListener { resetCode() }
        }

        // 运行按钮
        runButton = Button(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = "Run"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#4CAF50"))
//            setOnClickListener { runCode() }
        }

        // 添加组件到工具栏
        toolbar.addView(fileName)
        toolbar.addView(resetButton)
        toolbar.addView(runButton)

        // 添加工具栏到父容器
        parent.addView(toolbar)
    }

    private fun createTabLayout(context: Context, parent: LinearLayout) {
        // 创建标签布局
        tabLayout = TabLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16.dpToPx(), 0, 16.dpToPx(), 8.dpToPx())
            }
            setBackgroundColor(Color.parseColor("#252526"))
        }

        // 设置标签颜色
        tabLayout.setTabTextColors(
            Color.parseColor("#BBBBBB"), // 未选中颜色
            Color.WHITE // 选中颜色
        )

        // 添加标签项
        tabLayout.addTab(tabLayout.newTab().setText("Starter code"))
        tabLayout.addTab(tabLayout.newTab().setText("Solution code"))

        // 添加标签切换监听
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> setCode(getStarterPythonCode())
                    1 -> setCode(getSolutionPythonCode())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // 添加到父容器
        parent.addView(tabLayout)
    }

    private fun createCodeEditor(context: Context, parent: LinearLayout) {
        codeEditor = CodeEditorView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f // 使用权重填充剩余空间
            ).apply {
                setMargins(16.dpToPx(), 0, 16.dpToPx(), 16.dpToPx())
            }

            // 设置初始代码
            text = getInitialPythonCode()

            setMaxZoom(30f)
            languageRuleBook = PythonRuleBook()
//            lineNumberGenerator = { lines ->
//                (1..lines).map { " $it " }
//            }
//            languageRuleBook = JavaRuleBook()
            setBackgroundColor(Color.parseColor("#1E1E1E"))
        }

        // 创建代码编辑器
//        codeEditor = CodeEditor(context).apply {
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//            ).apply {
//                setMargins(16.dpToPx(), 0, 16.dpToPx(), 16.dpToPx())
//                weight = 1f
//            }
//
//            // 设置Python语言支持
//            setEditorLanguage(JavaLanguage())
//
//            // 设置高亮主题
//            setColorScheme(SchemeGitHub().apply {
//                applyDefault()
//                setColor(EditorColorScheme.WHOLE_BACKGROUND, Color.parseColor("#1E1E1E"))
//                setColor(EditorColorScheme.LINE_NUMBER_BACKGROUND, Color.parseColor("#1E1E1E"))
//                setColor(EditorColorScheme.CURRENT_LINE, Color.parseColor("#2A2A2A"))
//                setColor(EditorColorScheme.LINE_NUMBER, Color.parseColor("#858585"))
//                setColor(EditorColorScheme.SELECTION_HANDLE, Color.parseColor("#569CD6"))
//                setColor(EditorColorScheme.TEXT_NORMAL, Color.parseColor("#D4D4D4"))
//                setColor(EditorColorScheme.KEYWORD, Color.parseColor("#569CD6"))
//                setColor(EditorColorScheme.FUNCTION_NAME, Color.parseColor("#DCDCAA"))
//                setColor(EditorColorScheme.FUNCTION_CHAR_BACKGROUND_STROKE, Color.parseColor("#DCDCAA"))
//                setColor(EditorColorScheme.OPERATOR, Color.parseColor("#D4D4D4"))
//                setColor(EditorColorScheme.LITERAL, Color.parseColor("#B5CEA8"))
//                setColor(EditorColorScheme.IDENTIFIER_NAME, Color.parseColor("#9CDCFE"))
//                setColor(EditorColorScheme.COMMENT, Color.parseColor("#608B4E"))
//                setColor(EditorColorScheme.STRIKETHROUGH, Color.parseColor("#CE9178"))
//                setColor(EditorColorScheme.ANNOTATION, Color.parseColor("#646695"))
//            })
//
//            // 设置编辑器选项
//            nonPrintablePaintingFlags = 0
//            setLineNumberEnabled(true)
////            setLnPanelPadding(2.dpToPx())
////            setLineNumberAlignment(LineNumberAlign.SIDE_TO_TEXT)
////            setLineNumberScaleX(0.9f)
////            setLineNumberScaleY(0.9f)
//            setTypefaceText(Typeface.MONOSPACE)
//            setTextSize(14f)
//            setCursorAnimationEnabled(true)
//            setCursorBlinkPeriod(500)
////            blockLineEnabled = true
//            blockLineWidth = 3f
//
//            // 关闭放大镜功能
////            magnifier = Magnifier(this).apply {
////                enabled = false
////            }
//
//            // 添加焦点监听
////            setOnFocusChangeListener { _, hasFocus ->
////                if (hasFocus) {
////                    setBackgroundResource(R.drawable.editor_focused_border)
////                } else {
////                    setBackgroundResource(R.drawable.editor_normal_border)
////                }
////            }
//        }

        // 添加到父容器
        parent.addView(codeEditor)
    }

    private fun getStarterPythonCode(): String {
        return """
            # 简单的加法计算器
            def add_numbers():
                # 获取用户输入的两个数字
                num1 = float(input("请输入第一个数字："))
                num2 = float(input("请输入第二个数字："))

                # 计算和
                sum_result = num1 + num2

                # 输出结果
                print(f"计算结果：{num1} + {num2} = {sum_result}")

            add_numbers()
        """.trimIndent()
    }

    private fun getSolutionPythonCode(): String {
        return """
            # 增强版加法计算器
            def add_numbers():
                try:
                    # 获取用户输入的两个数字
                    num1 = float(input("请输入第一个数字："))
                    num2 = float(input("请输入第二个数字："))

                    # 计算和
                    sum_result = num1 + num2

                    # 输出结果
                    print(f"计算结果：{num1} + {num2} = {sum_result}")
                    return sum_result
                except ValueError:
                    print("错误：请输入有效的数字！")
                    return 0

            add_numbers()
        """.trimIndent()
    }

    private fun getInitialPythonCode() = getStarterPythonCode()

    fun setCode(code: String) {
//        codeEditor.codeEditText,
//        codeEditor.setText(code)
    }

    fun resetCode() {
        setCode(getStarterPythonCode())
        tabLayout.getTabAt(0)?.select()
    }

    fun runCode() {
        // 执行代码的逻辑（可连接到外部执行引擎）
//        (activity as? CodePlaygroundActivity)?.executeCode(codeEditor.text.toString())
    }

    override fun onResume() {
        super.onResume()
        codeEditor.isVisible = true
    }

    override fun onPause() {
        super.onPause()
        // 保存状态等操作
    }

    override fun onDestroy() {
        super.onDestroy()
//        codeEditor.release()
    }

    // 扩展函数：dp转px
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
    fun getCode(): String {
        return codeEditor.text.toString()
    }
}