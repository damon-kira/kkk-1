package com.colombia.credit.module.setting

import com.colombia.credit.bean.resp.RspResult
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repository: SettingRepository) :
    BaseViewModel() {

    val mLogoutLivedata = generatorLiveData<BaseResponse<RspResult>>()

    fun logout() {
        mLogoutLivedata.addSourceLiveData(repository.logout()) {
            mLogoutLivedata.postValue(it)
        }
    }
}