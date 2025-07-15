package com.kira.learning.module.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.common.lib.base.BaseFragment
import com.common.lib.glide.GlideUtils
import com.kira.learning.R
import com.kira.learning.bean.QuizInfo
import com.kira.learning.expand.dpToPx
import com.util.lib.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : BaseFragment() {
    private var position: Int = 0

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//        position = arguments?.getInt("position") ?: 0
        // 根据题目类型加载不同布局
//        val quizInfo = (activity as QuizActivity).viewModel.questions[position]
//        return when (quizInfo) {
//            is QuizInfo.SingleChoice -> inflater.inflate(
//                R.layout.item_single_choice, container, false
//            )
//
//            is QuizInfo.MultipleChoice -> inflater.inflate(
//                R.layout.item_multiple_choice, container, false
//            )
//
//            is QuizInfo.TrueFalse -> inflater.inflate(R.layout.item_true_false, container, false)
//        }
//    }

    // 这里注意根据不同的题型，设置不同的UI
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val quizInfo = (activity as QuizActivity).viewModel.questions[position]
//
//        quizInfo.also {
//            Log.e(this.javaClass.simpleName, "onViewCreated: ${it.content}")
//            Log.e(this.javaClass.simpleName, "onViewCreated: ${it.imageUrl}")
//            Log.e(this.javaClass.simpleName, "onViewCreated: ${it.type}")
//            Log.e(this.javaClass.simpleName, "onViewCreated: ${it.id}")
//        }

        // 题目标题
//        view.findViewById<TextView>(R.id.tv_quiz_title).text = quizInfo.content

        // 图片加载
//        quizInfo.imageUrl?.let { url ->
//            GlideUtils.loadCornerImageFromUrl(
//                view.context,
//                url,
//                view.findViewById(R.id.iv_quiz_content),
//                4.dp(),
//                R.drawable.ic_normal_image
//            )
//        }

        // 根据题型初始化选项
//        when (quizInfo) {
//            is QuizInfo.SingleChoice -> setupSingleChoice(view, quizInfo)
//            is QuizInfo.MultipleChoice -> setupMultipleChoice(view, quizInfo)
//            is QuizInfo.TrueFalse -> setupTrueFalse(view, quizInfo)
//        }
    }

    // 单选题 选项设置
    private fun setupSingleChoice(view: View, QuizInfo: QuizInfo.SingleChoice) {
        val container = view.findViewById<RadioGroup>(R.id.optionsContainer)
        QuizInfo.options.forEachIndexed { index, text ->
            val radioButton = RadioButton(context).apply {
                this.text = text
                id = index
            }
            container.addView(radioButton)
        }
    }

    // 多选题 选项设置
    private fun setupMultipleChoice(view: View, question: QuizInfo.MultipleChoice) {
        val container = view.findViewById<LinearLayout>(R.id.llMultiOptions)
        container.removeAllViews()

        // 存储已选答案的集合
        val selectedAnswers = mutableSetOf<Int>()

        // 动态生成多选按钮
        question.options.forEachIndexed { index, optionText ->
            CheckBox(context).apply {
                text = optionText
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
                }
                // 选项状态变化监听
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedAnswers.add(index)
                    } else {
                        selectedAnswers.remove(index)
                    }
                    // 更新答案数据
                    (activity as QuizActivity).viewModel.saveAnswer(
                        position, selectedAnswers.toSortedSet()
                    )
                }
                container.addView(this)
            }
        }
    }
    // endregion

    // region 判断题处理
    private fun setupTrueFalse(view: View, question: QuizInfo.TrueFalse) {
        val switch = view.findViewById<SwitchCompat>(R.id.swTrueFalse)

        // 初始化开关样式
        switch.text = "判断结果"
        switch.showText = false
        switch.thumbTextPadding = 16.dpToPx()

        // 设置监听
        switch.setOnCheckedChangeListener { _, isChecked ->
            // 保存答案（正确对应true，错误对应false）
            (activity as QuizActivity).viewModel.saveAnswer(position, isChecked)
        }

        // 添加辅助说明
        view.findViewById<TextView>(R.id.tvAnswerTip).apply {
            text = "正确答案：${if (question.answer) "正确" else "错误"}"
            visibility = View.INVISIBLE // 初始隐藏，答题后显示
        }
    }

    companion object {
        fun newInstance(position: Int): QuizFragment {
            return QuizFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
        }
    }
}