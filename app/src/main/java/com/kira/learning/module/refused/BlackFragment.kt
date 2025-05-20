package com.kira.learning.module.refused

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kira.learning.R
import com.kira.learning.databinding.FragmentBlackBinding
import com.kira.learning.manager.Launch
import com.kira.learning.module.home.BaseHomeFragment
import com.kira.learning.module.home.HomeLoanViewModel
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class BlackFragment: BaseHomeFragment() {

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
        mBinding.includeBlack.let {
            it.tvText1.setText(R.string.black_text1)
            it.tvText2.setText(R.string.refused_days)
            it.tvDays.text = "0"
            it.tvDesc.setText(R.string.black_desc)
        }
        mBinding.inclueDemoLayout1.let {
            it.tvDemoName.setText("拍照识别")
        }
        mBinding.inclueDemoLayout2.let {
            it.tvDemoName.setText("题目")
            it.tvBtn.setBlockingOnClickListener {
                Launch.skipAnswerActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout3.let {
            it.tvDemoName.setText("python编辑-运行")
        }
        mBinding.inclueDemoLayout4.let {
            it.tvDemoName.setText("AI对话")
        }
        mBinding.inclueDemoLayout5.let {
            it.tvDemoName.setText("数据库")
        }
        mBinding.inclueDemoLayout6.let {
            it.tvDemoName.setText("视频")
        }
        mBinding.inclueDemoLayout7.let {
            it.tvDemoName.setText("测验")
            it.tvBtn.setOnClickListener {
//                Log.d("Language", "当前语言: ${Locale.getDefault().language}")
//                Log.d("Language", "完整地区: ${Locale.getDefault()}")
//                activity?.recreate()
                Launch.skipQuizActivity(getSupportContext())
            }
        }

//        mBinding.inclueRepay.tvBtn.setBlockingOnClickListener{
//            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
//        }

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            mBinding.includeBlack.tvAmount.text = getString(R.string.days, it.WTvE5G.toString())

            val data = it.gQ1J
            if (data == null || data.isEmpty()) return@observe

//            mBinding.inclueRepay.llContent.show()
//            mBinding.inclueRepay.tvOrder.text = getString(R.string.orders, data.AMGH9kXswv)
//            mBinding.inclueRepay.tvAmount.text = getUnitString(data.RPBJ47rhC.orEmpty())
            mOrderIds = data.QLPGXTNU
        }

        initOnClickListener()
    }

    private fun initOnClickListener() {
        mBinding.inclueDemoLayout3.tvBtn.setOnClickListener {
            Launch.skipCodingActivity(getSupportContext())
        }

        mBinding.inclueDemoLayout4.tvBtn.setOnClickListener {
            Launch.skipAIIMActivity(getSupportContext())
        }

        mBinding.inclueDemoLayout5.tvBtn.setOnClickListener {
            Launch.skipChatActivity(getSupportContext())
        }
    }


}