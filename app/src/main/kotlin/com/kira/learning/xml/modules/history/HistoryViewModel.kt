package com.kira.learning.xml.modules.history

import com.kira.learning.model.res.RspHistoryInfo
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class HistoryViewModel @Inject constructor(private val repository: HistoryRepository) :
    BaseViewModel() {

    val mInfoLiveData = generatorLiveData<BaseResponse<RspHistoryInfo>>()

    fun getHistoryList() {
        showloading()
        mInfoLiveData.addSourceLiveData(repository.getHistoryList()) {
            hideLoading()
            mInfoLiveData.postValue(it)
        }
    }
}