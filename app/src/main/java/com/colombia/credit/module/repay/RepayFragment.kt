package com.colombia.credit.module.repay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.databinding.FragmentRepayBinding
import com.common.lib.base.BaseFragment
import com.common.lib.viewbinding.binding
import com.common.lib.viewbinding.doOnDestroyView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepayFragment : BaseFragment() {

    private val mBinding by binding(FragmentRepayBinding::inflate)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doOnDestroyView {  }
    }
}