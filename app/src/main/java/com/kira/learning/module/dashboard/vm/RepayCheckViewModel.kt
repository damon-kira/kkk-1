package com.kira.learning.module.dashboard.vm

import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.bean.resp.RspCheckOrder
import com.kira.learning.module.dashboard.repo.RepayCheckRepository
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