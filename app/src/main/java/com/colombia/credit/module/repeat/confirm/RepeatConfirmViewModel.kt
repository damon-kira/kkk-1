package com.colombia.credit.module.repeat.confirm

import com.colombia.credit.bean.resp.RspRepeatConfirmInfo
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepeatConfirmViewModel @Inject constructor(private val repository: RepeatConfirmRepository) :
    BaseViewModel() {

    val mConfirmInfoLiveData = generatorLiveData<BaseResponse<RspRepeatConfirmInfo>>()

    fun getConfirmInfo(productIds: String) {
        mConfirmInfoLiveData.addSourceLiveData(repository.getConfirmInfo(productIds)) {
            mConfirmInfoLiveData.postValue(it)
        }
    }

}