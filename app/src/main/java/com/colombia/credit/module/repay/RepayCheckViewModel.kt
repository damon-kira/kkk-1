package com.colombia.credit.module.repay

import com.colombia.credit.bean.resp.RspCheckOrder
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject


// 查询订单是否已还
class RepayCheckViewModel @Inject constructor(private val repository: RepayCheckRepository): BaseViewModel() {

    val mCheckLiveData = generatorLiveData<BaseResponse<RspCheckOrder>>()

    fun checkStatus(loanId: String){
        showloading()
        mCheckLiveData.addSourceLiveData(repository.checkStatus(loanId)) {
            hideLoading()
            mCheckLiveData.postValue(it)
        }
    }
}