package com.colombia.credit.module.login

import android.content.Context
import com.bigdata.lib.WifiHelper
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.resp.RspLoginInfo
import com.colombia.credit.bean.resp.RspSmsCode
import com.colombia.credit.expand.saveUserInfo
import com.colombia.credit.expand.setUserToken
import com.colombia.credit.util.registIP
import com.colombia.credit.util.registWifi
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import com.util.lib.MainHandler
import com.util.lib.NetWorkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: LoginRepository
) :
    BaseViewModel() {

    private val mCountDownHelper by lazy(LazyThreadSafetyMode.NONE) {
        CountDownHelper.get()
    }

    val downTimerLiveData = mCountDownHelper.mCountDownLiveData // 验证码倒计时

    val mAuthSmsCodeLiveData = generatorLiveData<BaseResponse<RspSmsCode>>()

    val loginLiveData = generatorLiveData<BaseResponse<RspLoginInfo>>()

    private var mCodeUUid: String? = null

    fun reqSmsCode(mobile: String) {
        showloading()
        mAuthSmsCodeLiveData.addSourceLiveData(repository.reqSmsCode(mobile)) {
            hideLoading()
            if (it.isSuccess()) {
                mCodeUUid = it.getData()?.data
            }
            mAuthSmsCodeLiveData.postValue(it)
        }
    }

    fun reqLogin(mobile: String, smsCode: String) {
        showloading()
        loginLiveData.addSourceLiveData(repository.loginSms(mobile, smsCode, mCodeUUid.orEmpty())) {
            hideLoading()
            if (it.isSuccess()) {
                registWifi = WifiHelper.getSSid(context)
                registIP = WifiHelper.getIp(context)
                it.getData()?.let { data ->
                    saveUserInfo(data)
                }
            }
            loginLiveData.postValue(it)
        }
    }


    fun startCountdown(type: Int = 0, mobile: String) {
        mCountDownHelper.stopCountdown()
        mCountDownHelper.startCountDown(type = type, mobile = mobile)
    }
}