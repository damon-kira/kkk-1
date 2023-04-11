package com.colombia.credit.module.firstconfirm

import com.colombia.credit.bean.resp.RspResult
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstConfirmViewModel @Inject constructor(private val repository: FirstConfirmRepository) :
    BaseViewModel() {

    val confirmLiveData = generatorLiveData<BaseResponse<RspResult>>()

    fun confirmLoan(bankNo: String, productId: String) {
        showloading()
        confirmLiveData.addSourceLiveData(repository.confirmLoan(bankNo, productId)) {
            hideLoading()
            confirmLiveData.postValue(it)
        }
    }
}