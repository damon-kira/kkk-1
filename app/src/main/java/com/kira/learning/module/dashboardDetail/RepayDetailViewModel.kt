package com.kira.learning.module.dashboardDetail

import com.kira.learning.bean.res.RspRepayDetail
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject


class RepayDetailViewModel @Inject constructor(private val repository: RepayDetailRepository) :
    BaseViewModel() {

    private val _detailLiveData = generatorLiveData<BaseResponse<RspRepayDetail>>()

    val detailLiveData = _detailLiveData

    fun getDetail(ids: String, showLoading: Boolean = false) {
        if (showLoading) {
            showloading()
        }
        _detailLiveData.addSourceLiveData(repository.getDetail(ids)) {
            hideLoading()
            _detailLiveData.postValue(it)
        }
    }
}