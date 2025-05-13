package com.kira.learning.module.firstconfirm

import com.bigdata.lib.WifiHelper
import com.bigdata.lib.loanWifi
import com.kira.learning.app.getAppContext
import com.kira.learning.bean.resp.RspResult
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class FirstConfirmViewModel @Inject constructor(private val repository: FirstConfirmRepository) :
    BaseViewModel() {

    val confirmLiveData = generatorLiveData<BaseResponse<RspResult>>()

    fun confirmLoan(bankNo: String, productId: String) {
        showloading()
        loanWifi = WifiHelper.getSSid(getAppContext())
        confirmLiveData.addSourceLiveData(repository.confirmLoan(bankNo, productId)) {
            hideLoading()
            confirmLiveData.postValue(it)
        }
    }
}