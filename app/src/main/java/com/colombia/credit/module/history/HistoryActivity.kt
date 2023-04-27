package com.colombia.credit.module.history

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.colombia.credit.R
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

        mBinding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        mBinding.swipeRefresh.setOnRefreshListener {
            getInfo()
        }

        mViewModel.mInfoLiveData.observerNonSticky(this) {
            stopRefresh()
            if (it.isSuccess()) {
                it.getData()?.jNgnZUXNGq?.let { list ->
                    change(list.isNotEmpty())
                    mAdapter.setItems(list)
                }
            } else {
                it.ShowErrorMsg {
                    getInfo()
                }
            }
        }
        getInfo()

        mBinding.layoutEmpty.tvApply.setBlockingOnClickListener {
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


    private fun stopRefresh() {
        mBinding.swipeRefresh.run {
            if (isRefreshing) {
                isRefreshing = false
            }
        }
    }

    private fun getInfo() {
        mViewModel.getHistoryList()
    }

    private fun change(isShow: Boolean) {
        if (isShow) {
            mBinding.layoutEmpty.llEmpty.hide()
            mBinding.recyclerview.show()
        } else {
            mBinding.layoutEmpty.llEmpty.show()
            mBinding.recyclerview.hide()
        }
    }


    private fun setRecyclerView() {
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter

    }
}