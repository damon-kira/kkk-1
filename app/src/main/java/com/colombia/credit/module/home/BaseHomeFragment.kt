package com.colombia.credit.module.home

import com.common.lib.base.BaseFragment

abstract class BaseHomeFragment : BaseFragment() {

    fun parentRefresh() {
        (parentFragment as? HomeFragment)?.onRefresh()
    }
}