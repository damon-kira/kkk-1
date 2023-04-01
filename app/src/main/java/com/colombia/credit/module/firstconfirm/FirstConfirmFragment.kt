package com.colombia.credit.module.firstconfirm

import android.view.View
import com.colombia.credit.databinding.FragmentFirstConfirmBinding
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 首贷确认额度页面
 */
@AndroidEntryPoint
class FirstConfirmFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentFirstConfirmBinding::inflate)

    private val mViewModel by lazyViewModel<FirstConfirmViewModel>()

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        stopRefresh()
    }
}