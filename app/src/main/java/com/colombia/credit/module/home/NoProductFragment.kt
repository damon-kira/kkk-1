package com.colombia.credit.module.home

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutNoProductBinding
import com.colombia.credit.expand.getUnitString
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.common.lib.expand.setBlockingOnClickListener
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

// 无产品
@AndroidEntryPoint
class NoProductFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(LayoutNoProductBinding::inflate)

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private var mOrderIds: String? = null

    override fun contentView(): View {
        return mBinding.root
    }

    override fun onPullToRefresh() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            val data = it.gQ1J
            if (data == null || data.isEmpty()) {
                mBinding.includeOrders.llContent.hide()
                return@observe
            }
            mBinding.includeOrders.llContent.show()
            mBinding.includeOrders.tvOrder.text = getString(R.string.orders, data.AMGH9kXswv)
            mBinding.includeOrders.tvAmount.text = getUnitString(data.RPBJ47rhC.orEmpty())
            mOrderIds = data.QLPGXTNU?.joinToString(",")
        }

        mBinding.includeOrders.tvBtn.setBlockingOnClickListener {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
        }

    }
}