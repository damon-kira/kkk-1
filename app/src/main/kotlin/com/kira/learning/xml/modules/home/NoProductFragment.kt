package com.kira.learning.xml.modules.home

import android.os.Bundle
import android.view.View
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.FragmentNoProductBinding
import com.kira.learning.xml.expand.getUnitString
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoProductFragment : BaseHomeRefreshFragment() {

    private val mBinding by binding(FragmentNoProductBinding::inflate)

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