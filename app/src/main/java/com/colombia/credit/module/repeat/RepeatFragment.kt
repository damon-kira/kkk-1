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
import com.colombia.credit.permission.HintDialog
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

    private val mHintDialog by lazy {
        HintDialog(getSupportContext()).also {
            it.showClose(true)
                .setTitleText(getString(R.string.repeat_error_title))
                .setIcon(R.drawable.ic_error_image)
                .setBtnText(getString(R.string.kyc_dialog_btn))
        }
    }

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
            val leftNum = mHomeViewModel.mRspInfoLiveData.value?.GbiDSBdW ?: 0
            val list = mAdapter.getSelectorItems().map { it.eqOEs }
            if (leftNum < list.size || leftNum == 0) {
                mHintDialog.setOnClickListener {
                    mAdapter.getSelectorItems().forEach { item ->
                        item.change()
                    }
                    mAdapter.notifyDataSetChanged()
                }.setMessage(getString(R.string.toast_product_app))
                getBaseActivity()?.addDialog(mHintDialog)
                return@setBlockingOnClickListener
            }

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
                val info = mHomeViewModel.mRspInfoLiveData.value
                val maxNum = info?.A04fSYQdHM ?: 0
                val item = mAdapter.getItemData<RepeatProductInfo>(position)
                if (item != null && !item.selector() && maxNum <= mAdapter.getSelectorItems().size) {
                    if (maxNum > 0) {
                        mHintDialog.setMessage(getString(R.string.toast_product_up, maxNum.toString()))
                            .setOnClickListener { }
                        getBaseActivity()?.addDialog(mHintDialog)
                    }
                    return
                }
                val leftNum = info?.GbiDSBdW ?: 0 // 最大可申请笔数
                if (item != null && item.selector() && mAdapter.getSelectorItems().size == 1 && leftNum > 0) {
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
            val params =
            if (it.isNotEmpty()) {
                it?.first()?.g7tzi.orEmpty()
            } else {
                "0"
            }
            mBinding.etvTag.text =
                getString(R.string.hosta_s, getUnitString(params))
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
//        if (mAdapter.itemCount < 3) return
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