package com.colombia.credit.module.home

import android.os.Bundle
import android.view.View
import com.colombia.credit.databinding.FragmentHomeLoanBinding
import com.common.lib.viewbinding.binding
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeLoanFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentHomeLoanBinding::inflate)

    private val mViewModel by lazyViewModel<HomeLoanViewModel>()

    override fun contentView(): View {
        return mBinding.root
    }

    override fun onPullToRefresh() {
        stopRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.homeToolbar.showCustomIcon(true)
    }

    /**
     * @param noProduct true:无产品信息
     */
    private fun changePage(noProduct: Boolean) {
        if (noProduct) {
            mBinding.noProduct.root.show()
            mBinding.loanClProduct.hide()
            mBinding.inclueHint.root.hide()
        } else {
            mBinding.noProduct.root.hide()
            mBinding.loanClProduct.show()
            mBinding.inclueHint.root.show()
        }
    }
}