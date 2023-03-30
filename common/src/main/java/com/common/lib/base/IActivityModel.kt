package com.common.lib.base

/**
 * Created by weisl on 2019/10/23.
 */
interface IActivityModel<T : BaseViewModel> {

    fun getViewModel(): T
}