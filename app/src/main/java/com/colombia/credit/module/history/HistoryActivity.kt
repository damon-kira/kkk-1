package com.colombia.credit.module.history

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.databinding.ActivityHistoryBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.module.home.MainEvent
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : BaseActivity() {

    private val mBinding by binding<ActivityHistoryBinding>()

    private val mViewModel by lazyViewModel<HistoryViewModel>()

    private val mAdapter by lazy {
        HistoryAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        change(false)
        setStatusBarColor(Color.WHITE, true)

        mBinding.toolbar.setOnbackListener {
            finish()
        }
        setRecyclerView()

        mViewModel.mInfoLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.jNgnZUXNGq?.let { list ->
                    change(list.isNotEmpty())
                    mAdapter.setItems(list)
                }
            } else {
                it.ShowErrorMsg {
                    mViewModel.getHistoryList()
                }
            }
        }
        mViewModel.getHistoryList()

        mBinding.historyTvApply.setBlockingOnClickListener {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
            finish()
        }

        mAdapter.mRepayListener = {
            Launch.skipRepayDetailActivity(this, it.KxX0GIRzo.orEmpty())
        }

        mAdapter.mFailureListener = {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
            finish()
        }
    }

    private fun change(isShow: Boolean) {
        if (isShow) {
            mBinding.llNotOrders.hide()
            mBinding.recyclerview.show()
        } else {
            mBinding.llNotOrders.show()
            mBinding.recyclerview.hide()
        }
    }


    private fun setRecyclerView() {
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter

    }
}