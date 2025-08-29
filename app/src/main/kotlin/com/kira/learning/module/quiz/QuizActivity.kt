package com.kira.learning.module.quiz

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.common.lib.base.BaseActivity
import com.common.lib.net.bean.Activity
import com.common.lib.net.bean.ActivityGroup
import com.common.lib.net.bean.ColumnBlock
import com.common.lib.net.bean.ColumnContent
import com.common.lib.net.bean.ContentItem
import com.common.lib.net.bean.Document
import com.common.lib.net.bean.EssayActivity
import com.common.lib.net.bean.FitBActivity
import com.common.lib.net.bean.FreeResponseActivity
import com.common.lib.net.bean.Heading
import com.common.lib.net.bean.KiraImage
import com.common.lib.net.bean.MultipleChoiceActivity
import com.common.lib.net.bean.Paragraph
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.ActivityQuizBinding
import com.kira.learning.module.quiz.vm.QuizViewModel
import com.util.lib.log.logger_d
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class QuizActivity : BaseActivity() {
    //    internal lateinit var viewModel: QuizViewModel
    private val mBinding by binding<ActivityQuizBinding>()
    internal val viewModel by lazyViewModel<QuizViewModel>()
//    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        // 初始化ViewModel
//        viewModel = ViewModelProvider(this)[QuizViewModel::class.kotlin]
        viewModel.getQuizInfo()

        // 设置ViewPager2
//        viewPager = findViewById(R.id.viewpage2_quiz)
        val adapter = QuestionPagerAdapter(this)
        mBinding.viewpage2Quiz.adapter = adapter
        mBinding.viewpage2Quiz.isUserInputEnabled = true // 允许滑动切换

        // 观察进度变化
        viewModel.currentPosition.observe(this) { position ->
            updateProgress(position)
        }

        viewModel.datas.observe(this) {
            if (it.isSuccess()) {
                logger_d(TAG, "onCreate: 获取题目成功: ${it.data}")
                logger_d(TAG, "onCreate: 共: ${it.data?.lesson?.steps?.size} Steps")
//                processDocument(it.data)
//                logger_d(TAG, "onCreate: Step 1 共计 : ${it.data?.lesson?.steps[0]?.content[0]?.type } 个模块")
//                logger_d(TAG, "onCreate: Step 1 : ${it.data?.lesson?.steps[0]?.content[0]?.content[0]?.content[3].} Steps")

                // 更新ViewPager适配器数据
                mBinding.viewpage2Quiz.adapter?.notifyDataSetChanged()
                // 初始化进度条
                updateProgress(0)
            } else {
                // 处理错误情况
//                showToast("获取题目失败: ${it.errorMsg}")
            }
        }

        // 按钮点击事件
        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            if (mBinding.viewpage2Quiz.currentItem > 0) {
                mBinding.viewpage2Quiz.currentItem = mBinding.viewpage2Quiz.currentItem - 1
            }
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            if (mBinding.viewpage2Quiz.currentItem < 6 - 1) {//viewModel.questions.size - 1) {
                mBinding.viewpage2Quiz.currentItem = mBinding.viewpage2Quiz.currentItem + 1
            }
        }

        // ViewPager页面切换监听
        mBinding.viewpage2Quiz.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setPosition(position)
            }
        })
    }
    // Step解析
    fun processDocument(document: Document?) {

        var index = 1
        document?.lesson?.steps?.forEach { step ->
            logger_d("Activities    "," ")
            logger_d("Activities    "," ")
            logger_d("Activities    ","Processing step: 第 ${index} Step")
            logger_d("Activities    ","Processing step: 类型-${step.type}")

            step.content.forEach { contentItem ->
                when (contentItem) {
                    is ColumnBlock -> {
                        logger_d("Activities    ","当前BlockType: ${contentItem.type}")
                        contentItem.columns.forEach { column ->
                            logger_d("Activities    ","type: ${column.type}  本Content数量: ${column.content.size}")
                            processColumnContent(column.content)
                        }
                    }
                    // 添加其他ContentItem类型的处理
                }
            }
            index++
        }
    }
    // 常规元素解析（非Activities）
    fun processColumnContent(contentList: List<ColumnContent>) {
        contentList.forEach { content ->
            when (content) {
                // 标题
                is Heading -> {
                    logger_d("Activities    ","-其他（标题）Heading: ${content.textContent.joinToString { it.text }}")
                    logger_d("Activities    ","     Level: ${content.attrs.level}, Align: ${content.attrs.textAlign}")
                }
                // 段落
                is Paragraph -> {
                    logger_d("Activities    ","-其他（段落）Paragraph: ${content.textContent.joinToString { it.text }}")
                }
                // 活动组
                is ActivityGroup -> {
                    logger_d("Activities    ","-活动组：Activity type: ${content.type} 活动数量: ${content.activities.size}")
                    content.activities.forEach { activity ->
                        processActivity(activity)
                    }
                }
                // 图片
                is KiraImage -> {
                    logger_d("Activities    ","-图片-Image: ${content.attrs.alt}")
                    logger_d("Activities    ","         -Source: ${content.attrs.src}")
                }
                // 添加其他ColumnContent类型的处理
            }
        }
    }

    fun processActivity(activity: Activity) {
        when (activity) {
            // 自由回答活动
            is FreeResponseActivity -> {
                logger_d("Activities    ","     自由回答活动-Free Response Activity )")
                logger_d("Activities    ","         题目-Question: ${activity.attrs.questionText}")
                activity.questions.forEach { question ->
                    logger_d("Activities    ","             其他内容-${question.contentType}: ${question.content?.joinToString { it.type.toString() }}")
                }
            }
            // 简答题活动
            is EssayActivity -> {
                logger_d("Activities    ","     文章类活动-Essay Activity")
                logger_d("Activities    ","         标题-Title: ${activity.attrs.heading.text}")
                activity.sections.forEach { section ->
                    logger_d("Activities    ","             其他内容-${section.type}: ${section.attrs?.text ?: section.attrs?.url}")
                }
            }
            // 多选题活动
            is MultipleChoiceActivity -> {
                logger_d("Activities    ","     选择题-Multiple Choice Activity （${if(activity.attrs.isMultipleAnswers) "多选题" else "单选题"}）")
                logger_d("Activities    ","         题目-Question: ${activity.attrs.questionText}")
                activity.attrs.choices.forEach { choice ->
                    logger_d("Activities    ","             ${choice.text} - ${if (choice.isCorrect) "Correct" else "Incorrect"}")
                }
            }
            // 添加其他Activity类型的处理
            is FitBActivity -> {
                logger_d("Activities    ","     填空题-Fill-in-the-Blank Activity")
                activity.question.forEach { question ->
                    logger_d("Activities    ","         题目-Question: ${question.content.joinToString { it.text }}")
                }
            }
        }
    }


    // 更新进度条
    private fun updateProgress(position: Int) {
        val progress = viewModel.data?.lesson?.steps?.size?.toInt()?.let { (position + 1) * 100 / it }
            ?: 1
        findViewById<ProgressBar>(R.id.progressBar).progress = progress
    }

    // ViewPager适配器
    private inner class QuestionPagerAdapter(context: Context) :
        FragmentStateAdapter(context as FragmentActivity) {
        override fun getItemCount(): Int = viewModel.data?.lesson?.steps?.size ?: 0

        override fun createFragment(position: Int): Fragment {
//            val data :ContentItem = viewModel.data?.lesson?.steps?.get(position)?.content?.firstOrNull()
//                ?: throw IllegalArgumentException("No content found for position $position")

            return ActivitiesFragment.newInstance(position)
        }
    }
}