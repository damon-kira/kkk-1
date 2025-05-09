package com.colombia.credit.module.setting

import com.colombia.credit.bean.resp.RspResult
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class SettingViewModel @Inject constructor(private val repository: SettingRepository) :
    BaseViewModel() {

    val mLogoutLivedata = generatorLiveData<BaseResponse<RspResult>>()

    fun logout() {
        showloading()
        mLogoutLivedata.addSourceLiveData(repository.logout()) {
            hideLoading()
            mLogoutLivedata.postValue(it)
        }
    }
}