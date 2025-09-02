package com.kira.learning.xml.modules.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.lib.base.BaseFragment
import com.common.lib.viewbinding.binding
import com.kira.learning.databinding.FragmentBlackBinding
import com.kira.learning.xml.Launch
import dagger.hilt.android.AndroidEntryPoint

/**
 * 样例代码导航页
 */
@AndroidEntryPoint
class NavigationFragment : BaseFragment() {

    private val mBinding by binding(FragmentBlackBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewSetting()

    }

    private fun initViewSetting() {
        mBinding.inclueDemoLayout6.let {
            it.tvDemoName.text = "Vedio-player"
            it.tvBtn.setOnClickListener {
                Launch.skipPlayerManageActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout8.let {
            it.tvDemoName.text = "Upload"
            it.tvBtn.setOnClickListener {

            }
        }
        mBinding.inclueDemoLayout11.let {
            it.tvDemoName.text = "Crash"
            it.tvBtn.setOnClickListener {
//                throw RuntimeException("Test Crash") // Force a crash
                val testNum = 100
//                val result = 100 / 0 // 这行代码会导致崩溃
                throw RuntimeException("${System.currentTimeMillis().toString()} 测试崩溃")
//                getBaseActivity()?.showAppUpgradeDialog(it)
            }
        }
        mBinding.inclueDemoLayout12.let {
            it.tvDemoName.text = "MathView"
            it.tvBtn.setOnClickListener {
                Launch.skipZoomImageActivity(getSupportContext())
            }
        }

        mBinding.inclueDemoLayout13.let {
            it.tvDemoName.text = "StepBar"
            it.tvBtn.setOnClickListener {
                Launch.skipStepBarViewActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout15.let {
            it.tvDemoName.text = "Coding"
            it.tvBtn.setOnClickListener {
                Launch.skipCodePlaygroundActivity(getSupportContext())
//                Launch.skipKCodingEditorActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout16.let {
            it.tvDemoName.text = "SuperEditor"
            it.tvBtn.setOnClickListener {
                Launch.skipSuperEditorActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout17.let {
            it.tvDemoName.text = "Ai-Chat"
            it.tvBtn.setOnClickListener {
                Launch.skipAiChatActivity(getSupportContext())
            }
        }
    }

}