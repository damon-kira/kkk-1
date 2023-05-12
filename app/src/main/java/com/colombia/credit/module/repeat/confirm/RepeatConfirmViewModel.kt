package com.colombia.credit.module.repeat.confirm

import com.colombia.credit.bean.resp.RspRepeatCalcul
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject


// 复贷确认额度
class RepeatConfirmViewModel @Inject constructor(private val repository: RepeatConfirmRepository) :
    BaseViewModel() {

    val mConfirmInfoLiveData = generatorLiveData<BaseResponse<RspRepeatCalcul>>()

    val mProListLiveData = generatorLiveData<ArrayList<RspRepeatCalcul.CalculDetail>>()

    fun getConfirmInfo(productIds: String) {
        mConfirmInfoLiveData.addSourceLiveData(repository.getConfirmInfo(productIds)) {
            mConfirmInfoLiveData.postValue(it)

            if (it.isSuccess()) {
                it.getData()?.vQDdL?.apply {
                    this.forEach {info ->
                        info.isCheck = 1
                    }
                    mProListLiveData.postValue(this)
                }
            }
        }
    }
}