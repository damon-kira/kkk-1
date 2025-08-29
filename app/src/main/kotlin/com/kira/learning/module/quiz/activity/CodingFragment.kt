package com.kira.learning.module.quiz.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.common.lib.glide.GlideUtils
import com.common.lib.net.bean.MultipleChoiceActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.FragmentChoiceMultipleBinding
import com.kira.learning.databinding.FragmentCodingBinding
import com.kira.learning.expand.dpToPx
import com.kira.learning.module.quiz.BaseFunctionFragment
import com.kira.learning.module.quiz.vm.QuizViewModel
import com.util.lib.dp
import com.util.lib.log.logger_d
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodingFragment : BaseFunctionFragment() {
    override val moduleId: String by lazy {
        arguments?.getString("moduleId", "-1") ?: "-1"
    }
    override val moduleName: String = "choice_multiple"

    private var processedImage: Bitmap? = null
    private val mBinding by binding(FragmentCodingBinding::inflate)
    internal val activityViewModel by lazyActivityViewModel<QuizViewModel>()
    private var mData: MultipleChoiceActivity? = null
    private val _selectedAnswers = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        if (activityViewModel.mapActivity[moduleId] is MultipleChoiceActivity) {
//            mData = activityViewModel.mapActivity[moduleId] as MultipleChoiceActivity
//        }
//        if (null == mData) return TextView(context).apply {
//            text = "非图片类型Fragment"
//        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            _selectedAnswers.addAll(savedInstanceState.getStringArrayList("selected_answers") ?: mutableListOf())
        }
        mBinding.tvChoiceMultipleTitle.text = mData?.attrs?.questionText
        mBinding.wvCoding.loadUrl("https://onecompiler.com/embed?language=java");
        setupMultipleChoice()
//        mBinding.processButton.setOnClickListener {
//            processImage()
//        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("selected_answers", ArrayList(_selectedAnswers))
    }

    // 多选题 选项设置
    private fun setupMultipleChoice() {
//        val container = view.findViewById<LinearLayout>(R.id.llMultiOptions)
        mBinding.llMultiOptions.removeAllViews()
        // 存储已选答案的集合
        mData?.attrs?.choices?.forEach { choice ->
            CheckBox(context).apply {
                text = choice.text
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
                }
                isChecked = choice.id in _selectedAnswers
                // 选项状态变化监听
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        _selectedAnswers.add(choice.id)
                    } else {
                        _selectedAnswers.remove(choice.id)
                    }
                    // 更新答案数据
//                    (activity as QuizActivity).viewModel.saveAnswer(
//                        position, selectedAnswers.toSortedSet()
//                    )
                }
                mBinding.llMultiOptions.addView(this)
            }
        }

        mBinding.btnConfirm.setOnClickListener {
            _selectedAnswers.forEach {
                // 提交选中的答案
                // 这里可以调用 ViewModel 或其他方法来保存答案
//                activityViewModel.saveAnswer(moduleId, selectedAnswers.toSortedSet())
                logger_d("aaaaaa","已选ID: $it ")
            }
        }

        // 动态生成多选按钮
//        question.options.forEachIndexed { index, optionText ->
//
//        }
    }

    private fun processImage() {
        // 图像处理逻辑
//        processedImage = processImageInternal()

        // 提交部分结果
        val result = Bundle().apply {
            putString("status", "processed")
            putInt("width", processedImage?.width ?: 0)
            putInt("height", processedImage?.height ?: 0)
        }
        resultListener?.onPartialResult(moduleId, result)
    }

    override fun collectResult(): Bundle {
        return Bundle().apply {
            putParcelable("processed_image", processedImage)
            putLong("timestamp", System.currentTimeMillis())
        }
    }

    override fun onMessageReceived(senderId: String, message: Bundle) {
        when (message.getString("action")) {
            "update_image" -> {
                val imageUri = message.getString("image_uri")
//                loadImage(imageUri)
            }
        }
    }

    companion object {
        fun newInstance(id: String): CodingFragment {
            return CodingFragment().apply {
                arguments = Bundle().apply {
                    putString("moduleId", id)
                }
            }
        }
    }
}