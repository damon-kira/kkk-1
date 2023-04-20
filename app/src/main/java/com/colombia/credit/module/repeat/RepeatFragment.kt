package com.colombia.credit.module.repeat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.databinding.FragmentRepeatBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.SpaceItemDecoration
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeLoanViewModel
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.dp
import dagger.hilt.android.AndroidEntryPoint


//复贷首页
@AndroidEntryPoint
class RepeatFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentRepeatBinding::inflate)

    private val mAdapter: RepeatProductAdapter by lazy {
        RepeatProductAdapter(arrayListOf())
    }

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        mHomeViewModel.getHomeInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
        mBinding.repeatRecyclerview.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        mBinding.repeatRecyclerview.adapter = mAdapter
        mBinding.repeatRecyclerview.addItemDecoration(
            SpaceItemDecoration(
                SpaceItemDecoration.VERTICAL_LIST, 12f.dp())
        )

        mBinding.repeatTvApply.setBlockingOnClickListener {
            val list = mAdapter.getSelectorItems().map { it.eqOEs }
            Launch.skipRepeatConfirmActivity(getSupportContext(), list.joinToString(","))
        }
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

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            it.jBRR?.let { list ->
                mAdapter.setItems(list)
                mBinding.etvTag.text =
                    getString(R.string.hosta_s, getUnitString(list.first().g7tzi.orEmpty()))
            }
        }
    }
}