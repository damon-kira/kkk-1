package com.kira.learning.module.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.lib.base.BaseFragment
import com.common.lib.viewbinding.binding
import com.google.android.material.tabs.TabLayout
import com.kira.learning.R
import com.kira.learning.databinding.FragmentActivitiesBinding
import com.kira.learning.module.firstconfirm.FirstConfirmFragment
import com.util.lib.log.logger_d

class ActivitiesFragment : BaseFragment() {

    private lateinit var coordinator: FragmentCoordinator
    private lateinit var tabLayout: TabLayout
    private val mBinding by binding(FragmentActivitiesBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化TabLayout和Coordinator
        tabLayout = mBinding.tabLayout
        coordinator = FragmentCoordinator(childFragmentManager, R.id.ly_activities_container)

//        getInstance(getSupportContext(), FirstConfirmFragment::class.java, null)
        // 注册功能模块
        coordinator.registerModule(ImageProcessingFragment())
        coordinator.registerModule(ImageProcessingFragment())
        coordinator.registerModule(ImageProcessingFragment())


        // 设置结果就绪回调
        coordinator.onAllResultsReady = { results ->
            processFinalResults(results)
        }

        // 配置TabLayout
        setupTabLayout()
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