package com.kira.learning.xml.modules.firstconfirm.vm

import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.model.res.RspResult
import com.kira.learning.xml.modules.firstconfirm.repo.FirstConfirmRepository
import javax.inject.Inject

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