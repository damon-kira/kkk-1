package com.colombia.credit.module.repeat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentRepeatBinding
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint


//复贷首页
@AndroidEntryPoint
class RepeatFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentRepeatBinding::inflate)

    private val mItems: ArrayList<String> = arrayListOf()

    private val mAdapter: RepeatProductAdapter by lazy {
        RepeatProductAdapter(mItems, R.layout.layout_repeat_product_item)
    }

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        stopRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.repeatRecyclerview.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        mBinding.repeatRecyclerview.adapter = mAdapter
    }
}