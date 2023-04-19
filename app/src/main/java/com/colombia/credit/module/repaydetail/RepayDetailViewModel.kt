package com.colombia.credit.module.repaydetail

import com.colombia.credit.bean.resp.RspRepayDetail
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
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