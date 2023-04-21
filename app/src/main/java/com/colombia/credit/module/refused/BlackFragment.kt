package com.colombia.credit.module.refused

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentBlackBinding
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.module.home.BaseHomeFragment
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.home.MainEvent
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

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

        mBinding.inclueRepay.tvBtn.setBlockingOnClickListener{
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
        }

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            // 黑名单和拒绝用的一个布局，黑名单下面显示的是借款账期
            mBinding.includeBlack.tvAmount.text = getString(R.string.days, it.WTvE5G.toString())

            val data = it.gQ1J
            if (data == null || data.isEmpty()) return@observe

            mBinding.inclueRepay.llContent.show()
            mBinding.inclueRepay.tvOrder.text = getString(R.string.orders, data.AMGH9kXswv)
            mBinding.inclueRepay.tvAmount.text = getUnitString(data.RPBJ47rhC.orEmpty())
            mOrderIds = data.QLPGXTNU
        }
    }


}