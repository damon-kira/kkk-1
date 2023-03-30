package com.colombia.credit.module.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.databinding.FragmentAccountBinding
import com.common.lib.base.BaseFragment
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment: BaseFragment() {

    private val mBinding by binding(FragmentAccountBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mBinding.root
    }
}