package com.kira.learning.module.coding

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.annotation.RawRes
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.databinding.ActivityKcodingeditorBinding
import dagger.hilt.android.AndroidEntryPoint
import de.markusressel.kodehighlighter.language.python.PythonRuleBook
import io.github.kbiakov.codeview.editor.dpToPx

@AndroidEntryPoint
class KCodingEditorActivity : BaseActivity() {

    private val mBinding by binding<ActivityKcodingeditorBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.codeEditorLayout.apply {
            languageRuleBook = PythonRuleBook()
            lineNumberGenerator = { lines ->
                (1..lines).map { " $it " }
            }
            editable = false
            showDivider = true
            showMinimap = true
            minimapBorderWidth = 1.dpToPx(context)
            minimapBorderColor = Color.BLACK
            minimapIndicatorColor = Color.GREEN
            minimapMaxDimension = 150.dpToPx(context)
            minimapGravity = Gravity.BOTTOM or Gravity.END
        }

        initEditorText()
    }

    private fun initEditorText() {
        mBinding.codeEditorLayout.text = """
            # 简单的加法计算器
            def add_numbers():
                # 获取用户输入的两个数字
                num1 = float(input("请输入第一个数字: "))
                num2 = float(input("请输入第二个数字: "))
                
                # 计算和
                sum_result = num1 + num2
                
                # 输出结果
                print(f"计算结果: {num1} + {num2} = {sum_result}")

            # 调用函数
            add_numbers()
        """.trimIndent()
        mBinding.codeEditorLayout.editable = true
    }

    private fun readResourceFileAsText(@RawRes resourceId: Int): String {
        return resources.openRawResource(resourceId).bufferedReader().readText()
    }

}
