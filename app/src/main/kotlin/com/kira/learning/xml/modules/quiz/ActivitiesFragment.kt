package com.kira.learning.xml.modules.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.lib.base.BaseFragment
import com.common.lib.net.bean.Activity
import com.common.lib.net.bean.ActivityGroup
import com.common.lib.net.bean.ColumnBlock
import com.common.lib.net.bean.ColumnContent
import com.common.lib.net.bean.ContentItem
import com.common.lib.net.bean.EssayActivity
import com.common.lib.net.bean.FitBActivity
import com.common.lib.net.bean.FreeResponseActivity
import com.common.lib.net.bean.Heading
import com.common.lib.net.bean.KiraImage
import com.common.lib.net.bean.MultipleChoiceActivity
import com.common.lib.net.bean.Paragraph
import com.common.lib.viewbinding.binding
import com.google.android.material.tabs.TabLayout
import com.kira.learning.R
import com.kira.learning.databinding.FragmentActivitiesBinding
import com.kira.learning.xml.modules.quiz.activity.ChoiceMultipleFragment
import com.kira.learning.xml.modules.quiz.activity.CodingFragment
import com.kira.learning.xml.modules.quiz.vm.QuizViewModel
import com.util.lib.log.logger_d
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import kotlin.collections.forEach

@AndroidEntryPoint
class ActivitiesFragment : BaseFragment() {

    private lateinit var coordinator: FragmentCoordinator
    private lateinit var tabLayout: TabLayout
    private val mBinding by binding(FragmentActivitiesBinding::inflate)

    internal val activityViewModel by lazyActivityViewModel<QuizViewModel>()
    private var position: Int = 0
    private lateinit var data: List<ContentItem>
    private var childFragmentAdded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = arguments?.getInt("position", 0) ?: 0
        data = activityViewModel.data?.lesson?.steps?.get(position)?.content ?: emptyList()

        logger_d("aaaaaaaa", "接收的参数:${activityViewModel.data?.lesson?.steps?.size ?: 0}")
        // 初始化TabLayout和Coordinator
        tabLayout = mBinding.tabLayout
        coordinator = FragmentCoordinator(childFragmentManager, R.id.ly_activities_container)

        // 恢复状态
        if (savedInstanceState != null) {
            childFragmentAdded = savedInstanceState.getBoolean("child_fragment_added", false)
        }

        if (!childFragmentAdded) {
            registerModules()
            childFragmentAdded = true
        }
//        if (coordinator.getAllFragments().keys.isEmpty()) {
////            logger_d(
////                "aaaaaaaa",
////                "Step${position}空了  执行开始-当前coordinator容量:${coordinator.getAllFragments().keys.size}"
////            )
//            logger_d(
//                "aaaaaaaa",
//                "Step${position}空了  执行开始-当前fragment容量:${childFragmentManager.fragments.size}"
//            )
//            registerModules()
////            logger_d(
////                "aaaaaaaa",
////                "Step${position}空了  执行结束-当前coordinator容量:${coordinator.getAllFragments().keys.size}"
////            )
//            logger_d(
//                "aaaaaaaa",
//                "Step${position}空了  执行结束-当前fragment容量:${childFragmentManager.fragments.size}"
//            )
//
//        }

        // 设置结果就绪回调
        coordinator.onAllResultsReady = { results ->
            processFinalResults(results)
        }

        // 配置TabLayout
        setupTabLayout()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("child_fragment_added", childFragmentAdded)
    }

    private fun registerModules() {
        data.forEach { contentItem ->
            when (contentItem) {
                is ColumnBlock -> {
                    logger_d("Activities    ", "当前BlockType: ${contentItem.type}")
                    contentItem.columns.forEach { column ->
                        logger_d(
                            "Activities    ",
                            "type: ${column.type}  本Content数量: ${column.content.size}"
                        )
                        processColumnContent(column.content)
                    }
                }
                // 添加其他ContentItem类型的处理
            }
        }
    }

    // 常规元素解析（非Activities）
    fun processColumnContent(contentList: List<ColumnContent>) {
        contentList.forEach { content ->
            val moduleId: String = UUID.randomUUID().toString()
            activityViewModel.mapColumnContents.put(moduleId, content)
            when (content) {
                // 标题
                is Heading -> {
                    // TODO : 这里可以注册标题模块
                    coordinator.registerModule(ImageProcessingFragment.newInstance(moduleId))
                    logger_d(
                        "Activities    ",
                        "-其他（标题）Heading: ${content.textContent.joinToString { it.text }}"
                    )
                    logger_d(
                        "Activities    ",
                        "     Level: ${content.attrs.level}, Align: ${content.attrs.textAlign}"
                    )
                }
                // 段落
                is Paragraph -> {
                    // TODO : 这里可以注册段落模块
                    coordinator.registerModule(ImageProcessingFragment.newInstance(moduleId))
                    logger_d(
                        "Activities    ",
                        "-其他（段落）Paragraph: ${content.textContent.joinToString { it.text }}"
                    )
                }
                // 活动组
                is ActivityGroup -> {
                    logger_d(
                        "Activities    ",
                        "-活动组：Activity type: ${content.type} 活动数量: ${content.activities.size}"
                    )
                    content.activities.forEach { activity ->
                        processActivity(activity)
                    }
                }
                // 图片
                is KiraImage -> {
                    // TODO : 这里可以注册图片模块
                    coordinator.registerModule(ImageProcessingFragment.newInstance(moduleId))
                    logger_d("Activities    ", "-图片-Image: ${content.attrs.alt}")
                    logger_d("Activities    ", "         -Source: ${content.attrs.src}")
                }
                // 添加其他ColumnContent类型的处理
            }
        }
    }

    fun processActivity(activity: Activity) {
        val moduleId: String = UUID.randomUUID().toString()
        activityViewModel.mapActivity.put(moduleId, activity)
        when (activity) {
            // 自由回答活动
            is FreeResponseActivity -> {
                // TODO : 这里可以注册自由回答活动模块
                coordinator.registerModule(CodingFragment.newInstance(moduleId))
                logger_d("Activities    ", "     自由回答活动-Free Response Activity )")
                logger_d("Activities    ", "         题目-Question: ${activity.attrs.questionText}")
                activity.questions.forEach { question ->
                    logger_d(
                        "Activities    ",
                        "             其他内容-${question.contentType}: ${question.content?.joinToString { it.type.toString() }}"
                    )
                }
            }
            // 简答题活动
            is EssayActivity -> {
                // TODO : 这里可以注册简答题活动模块
                coordinator.registerModule(CodingFragment.newInstance(moduleId))
                logger_d("Activities    ", "     文章类活动-Essay Activity")
                logger_d("Activities    ", "         标题-Title: ${activity.attrs.heading.text}")
                activity.sections.forEach { section ->
                    logger_d(
                        "Activities    ",
                        "             其他内容-${section.type}: ${section.attrs?.text ?: section.attrs?.url}"
                    )
                }
            }
            // 多选题活动
            is MultipleChoiceActivity -> {
                // TODO : 这里可以注册多选题活动模块
                coordinator.registerModule(ChoiceMultipleFragment.newInstance(moduleId))
                logger_d(
                    "Activities    ",
                    "     选择题-Multiple Choice Activity （${if (activity.attrs.isMultipleAnswers) "多选题" else "单选题"}）"
                )
                logger_d("Activities    ", "         题目-Question: ${activity.attrs.questionText}")
                activity.attrs.choices.forEach { choice ->
                    logger_d(
                        "Activities    ",
                        "             ${choice.text} - ${if (choice.isCorrect) "Correct" else "Incorrect"}"
                    )
                }
            }

            is FitBActivity -> {
                // TODO : 这里可以注册填空题活动模块
                coordinator.registerModule(ImageProcessingFragment.newInstance(moduleId))
                logger_d("Activities    ", "     填空题-Fill-in-the-Blank Activity")
                activity.question.forEach { question ->
                    logger_d(
                        "Activities    ",
                        "         题目-Question: ${question.content.joinToString { it.text }}"
                    )
                }
            }
        }
    }

    private fun setupTabLayout() {
        // 动态添加Tab
        coordinator.getAllFragments().keys.forEach { moduleId ->
//            tabLayout.addTab(tabLayout.newTab().setText(getTabTitle(moduleId)))
            logger_d(
                TAG,
                "setupTabLayout: Adding tab for moduleName ${coordinator.getAllFragments()[moduleId]?.moduleName}   moduleId: $moduleId"
            )
        }

//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                val position = tab.position
//                val moduleId = coordinator.fragments.keys.elementAt(position)
//                coordinator.showModule(moduleId)
//            }
//
//            // 其他方法实现...
//        })
    }

    private fun processFinalResults(results: Map<String, Bundle>) {
        // 处理最终结果
        val imageResult = results["image_processing"]
        val dataResult = results["data_analysis"]

        // 构建最终结果对象
//        val finalResult = FinalResult(
//            imageResult = imageResult.getParcelable("processed_image"),
//            analysis = dataResult?.getSerializable("analysis_result") as? AnalysisResult
//        )

        // 上传或保存结果
//        saveOrUploadResult(finalResult)
    }

    // 提交最终结果按钮
    fun onSubmitClick(view: View) {
        val allResults = coordinator.collectAllResults()
        processFinalResults(allResults)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coordinator.clear()
//        logger_d(
//            "aaaaaaaa",
//            "Step${position} - onDestroyView的clear执行了  当前coordinator容量:${coordinator.getAllFragments().keys.size}"
//        )

        logger_d(
            "aaaaaaaa",
            "Step${position} - onDestroyView的clear执行了  当前fragment容量:${childFragmentManager.fragments.size}"
        )
    }

    companion object {
        fun newInstance(position: Int): ActivitiesFragment {
            return ActivitiesFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
        }
    }
}