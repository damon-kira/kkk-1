package com.colombia.credit.module.history

import android.os.Bundle
import com.colombia.credit.databinding.ActivityHistoryBinding
import com.colombia.credit.module.adapter.SafeLinearLayoutManager
import com.colombia.credit.module.adapter.linearLayoutManager
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : BaseActivity() {

    private val mBinding by binding<ActivityHistoryBinding>()

    private val mAdapter by lazy {
        HistoryAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.llNotOrders.show()
        mBinding.recyclerview.hide()
    }


    private fun setRecyclerView() {
        mBinding.recyclerview.layoutManager = linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter
    }
}