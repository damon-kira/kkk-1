package com.colombia.credit.module.login

import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository): BaseViewModel() {

    private val mCountDownHelper by lazy(LazyThreadSafetyMode.NONE) {
        CountDownHelper.get()
    }

    val downTimerLiveData = mCountDownHelper.mCountDownLiveData // 验证码倒计时

    val mAuthSmsCodeLiveData = generatorLiveData<BaseResponse<String>>()

    val loginLiveData = generatorLiveData<BaseResponse<String>>()

    fun reqSmsCode(mobile: String) {
        mAuthSmsCodeLiveData.addSourceLiveData(repository.reqSmsCode(mobile)) {
            mAuthSmsCodeLiveData.postValue(it)
        }
    }

    fun reqLogin(mobile: String, smsCode: String) {
        loginLiveData.addSourceLiveData(repository.login(mobile, smsCode)) {
            loginLiveData.postValue(it)
        }
    }


    fun startCountdown(type: Int = 0, mobile: String) {
        mCountDownHelper.stopCountdown()
        mCountDownHelper.startCountDown(type = type, mobile = mobile)
    }
}