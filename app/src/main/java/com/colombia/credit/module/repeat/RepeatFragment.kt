package com.colombia.credit.module.repeat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.databinding.FragmentRepeatBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.SpaceItemDecoration
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.home.MainEvent
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.util.lib.dp
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint


//复贷首页
@AndroidEntryPoint
class RepeatFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentRepeatBinding::inflate)

    private val mAdapter: RepeatProductAdapter by lazy {
        RepeatProductAdapter(arrayListOf())
    }

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private var mOrderIds: String? = null

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        mHomeViewModel.getHomeInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
        setOffset()
        initRecyclerview(view)

        mBinding.repeatTvApply.setBlockingOnClickListener {
            val list = mAdapter.getSelectorItems().map { it.eqOEs }
            Launch.skipRepeatConfirmActivity(getSupportContext(), list.joinToString(","))
        }

        mBinding.includeOrders.tvBtn.setBlockingOnClickListener {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
        }

        initObserver()
    }

    private fun initRecyclerview(view: View) {
        mBinding.repeatRecyclerview.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        mBinding.repeatRecyclerview.adapter = mAdapter
        mBinding.repeatRecyclerview.addItemDecoration(
            SpaceItemDecoration(
                SpaceItemDecoration.VERTICAL_LIST, 12f.dp()
            )
        )
        mBinding.repeatRecyclerview.itemAnimator?.changeDuration = 0
        mBinding.repeatRecyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val maxNum = mHomeViewModel.mRspInfoLiveData.value?.A04fSYQdHM ?: 0
                val item = mAdapter.getItemData<RepeatProductInfo>(position)
                if (item != null && !item.selector() && maxNum <= mAdapter.getSelectorItems().size) {
                    if (maxNum > 0) {
                        toast(getString(R.string.toast_product_up, maxNum.toString()))
                    } else {
                        toast(R.string.toast_product_app)
                    }
                    return
                }
                if (item != null && item.selector() && mAdapter.getSelectorItems().size == 1) {
                    toast(R.string.toast_min_product)
                    return
                }
                item?.change()
                mAdapter.notifyItemChanged(position)
            }
        })
    }

    private fun initObserver() {
        mHomeViewModel.repeatProductLiveData.observe(viewLifecycleOwner) {
            mAdapter.setItems(it)
            mBinding.etvTag.text =
                getString(R.string.hosta_s, getUnitString(it?.first()?.g7tzi.orEmpty()))
        }

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
    }

    private fun setOffset() {
        if (mBinding.etvTag.top > 0) {
            changeListPadding(mBinding.etvTag.top)
        } else {
            mBinding.etvTag.post {
                if (isDestroyView()) return@post
                changeListPadding(mBinding.etvTag.top)
            }
        }
    }

    private fun changeListPadding(offset: Int) {
        val padding = mBinding.clContent.height - offset
        mBinding.repeatRecyclerview.setPadding(
            mBinding.repeatRecyclerview.paddingLeft,
            mBinding.repeatRecyclerview.paddingTop,
            mBinding.repeatRecyclerview.paddingRight,
            padding
        )
    }
}