package com.common.lib.base

/**
 * Created by weisl on 2019/10/16.
 */
interface ILoading {

    fun showLoading(cancelable: Boolean = true)

    fun hideLoading()
}