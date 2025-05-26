package com.common.lib.base

interface ILoading {

    fun showLoading(cancelable: Boolean = true)

    fun hideLoading()
}