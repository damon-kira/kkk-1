package com.kira.learning.module.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.kira.learning.databinding.FragmentBlackBinding
import com.kira.learning.manager.Launch
import com.kira.learning.module.home.BaseHomeFragment
import com.kira.learning.module.home.MainActivity
import com.kira.learning.module.home.vm.HomeLoanViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 样例代码导航页
 */
@AndroidEntryPoint
class NavigationFragment : BaseHomeFragment() {

    private val mBinding by binding(FragmentBlackBinding::inflate)

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private var mOrderIds: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
//        mBinding.includeBlack.let {
//            it.tvText1.setText(R.string.black_text1)
//            it.tvText2.setText(R.string.refused_days)
//            it.tvDays.text = "0"
//            it.tvDesc.setText(R.string.black_desc)
//        }


//        mBinding.inclueRepay.tvBtn.setBlockingOnClickListener{
//            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
//        }

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
//            mBinding.includeBlack.tvAmount.text = getString(R.string.days, it.WTvE5G.toString())

            val data = it.gQ1J
            if (data == null || data.isEmpty()) return@observe

//            mBinding.inclueRepay.llContent.show()
//            mBinding.inclueRepay.tvOrder.text = getString(R.string.orders, data.AMGH9kXswv)
//            mBinding.inclueRepay.tvAmount.text = getUnitString(data.RPBJ47rhC.orEmpty())
            mOrderIds = data.QLPGXTNU
        }
        onBackListener()
        initViewSetting()
    }

    private fun getMainActivity() = activity as MainActivity?

    fun onBackListener() {
        mBinding.toolbar.setOnClickListener {
            getMainActivity()?.drawerToggle()
        }
    }

    private fun initViewSetting() {
        mBinding.inclueDemoLayout1.let {
            it.tvDemoName.text = "拍照识别"
            it.tvBtn.setOnClickListener {
                Launch.skipPhotographActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout2.let {
            it.tvDemoName.text = "题目"
            it.tvBtn.setBlockingOnClickListener {
                Launch.skipAnswerActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout3.let {
            it.tvDemoName.text = "python编辑-运行"
            it.tvBtn.setOnClickListener {
                Launch.skipCodingActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout4.let {
            it.tvDemoName.text = "AI对话"
            it.tvBtn.setOnClickListener {
                Launch.skipAIIMActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout5.let {
            it.tvDemoName.text = "数据库"
            it.tvBtn.setOnClickListener {
                Launch.skipChatActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout6.let {
            it.tvDemoName.text = "视频"
            it.tvBtn.setOnClickListener {
                Launch.skipPlayerManageActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout7.let {
            it.tvDemoName.text = "测验"
            it.tvBtn.setOnClickListener {
//                Log.d("Language", "当前语言: ${Locale.getDefault().language}")
//                Log.d("Language", "完整地区: ${Locale.getDefault()}")
//                activity?.recreate()
                Launch.skipQuizActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout8.let {
            it.tvDemoName.text = "上传"
            it.tvBtn.setOnClickListener {
                Launch.skipUploadActivity(getSupportContext())
            }
        }
    }


}