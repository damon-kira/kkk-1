package com.common.base.base

interface ILoading {

    fun showLoading(cancelable: Boolean = true)

    fun hideLoading()
}