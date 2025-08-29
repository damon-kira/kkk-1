//package com.kira.learning.module.quiz
//
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.CheckBox
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import com.common.lib.glide.GlideUtils
//import com.kira.learning.R
//import com.kira.learning.bean.QuizInfo
//import com.kira.learning.expand.dpToPx
//import com.util.lib.dp
//import kotlin.util.UUID
//
//class ChoiceMultipleFragment : BaseFunctionFragment() {
//    override val moduleId: String = UUID.randomUUID().toString()
//    override val moduleName: String = "image_processing"
//
//    private lateinit var imageView: ImageView
//    private var processedImage: Bitmap? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_choice_multiple, container, false).apply {
//            imageView = findViewById(R.id.image_view)
//            findViewById<Button>(R.id.process_button).setOnClickListener {
//                processImage()
//            }
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // 题目标题
////        view.findViewById<TextView>(R.id.tv_quiz_title).text = quizInfo.content
//
//        // 图片加载
//        "".let { url ->
//            GlideUtils.loadCornerImageFromUrl(
//                view.context,
//                url,
//                view.findViewById(R.id.iv_quiz_content),
//                4.dp(),
//                R.drawable.ic_normal_image
//            )
//        }
//
//        // 获取题目信息
////        val quizInfo = (activity as QuizActivity).viewModel.questions[position]
////        if (quizInfo is QuizInfo.MultipleChoice) {
////            setupMultipleChoice(view, quizInfo)
////        }
//    }
//
//    private fun processImage() {
//        // 图像处理逻辑
////        processedImage = processImageInternal()
//
//        // 提交部分结果
//        val result = Bundle().apply {
//            putString("status", "processed")
//            putInt("width", processedImage?.width ?: 0)
//            putInt("height", processedImage?.height ?: 0)
//        }
//        resultListener?.onPartialResult(moduleId, result)
//    }
//
//    override fun collectResult(): Bundle {
//        return Bundle().apply {
//            putParcelable("processed_image", processedImage)
//            putLong("timestamp", System.currentTimeMillis())
//        }
//    }
//
//    // 多选题 选项设置
//    private fun setupMultipleChoice(view: View, question: QuizInfo.MultipleChoice) {
//        val container = view.findViewById<LinearLayout>(R.id.llMultiOptions)
//        container.removeAllViews()
//
//        // 存储已选答案的集合
//        val selectedAnswers = mutableSetOf<Int>()
//
//        // 动态生成多选按钮
//        question.options.forEachIndexed { index, optionText ->
//            CheckBox(context).apply {
//                text = optionText
//                layoutParams = LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
//                }
//                // 选项状态变化监听
//                setOnCheckedChangeListener { _, isChecked ->
//                    if (isChecked) {
//                        selectedAnswers.add(index)
//                    } else {
//                        selectedAnswers.remove(index)
//                    }
//                    // 更新答案数据
////                    (activity as QuizActivity).viewModel.saveAnswer(
////                        position, selectedAnswers.toSortedSet()
////                    )
//                }
//                container.addView(this)
//            }
//        }
//    }
//
//    override fun onMessageReceived(senderId: String, message: Bundle) {
//        when (message.getString("action")) {
//            "update_image" -> {
//                val imageUri = message.getString("image_uri")
////                loadImage(imageUri)
//            }
//        }
//    }
//}