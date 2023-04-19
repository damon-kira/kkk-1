package com.colombia.credit.module.repay

import com.colombia.credit.bean.resp.RspRepayOrders
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepayTabViewModel @Inject constructor(private val repository: RepayTabRepository) :
    BaseViewModel() {

    private val _ordersLivedata = generatorLiveData<BaseResponse<RspRepayOrders>>()
    val ordersLivedata = _ordersLivedata

    fun getRepayOrders() {
        _ordersLivedata.addSourceLiveData(repository.getRepayOrders()) {
            _ordersLivedata.postValue(it)
        }
    }
}