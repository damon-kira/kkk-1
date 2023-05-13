package com.colombia.credit.module.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.bigdata.lib.WifiHelper
import com.bigdata.lib.registIP
import com.bigdata.lib.registWifi
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.resp.RspLoginInfo
import com.colombia.credit.bean.resp.RspSmsCode
import com.colombia.credit.expand.saveMobile
import com.colombia.credit.expand.saveUserInfo
import com.colombia.credit.util.GPInfoUtils
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

// 登录页面
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel(), LifecycleEventObserver {

    private val mCountDownHelper by lazy(LazyThreadSafetyMode.NONE) {
        CountDownHelper.get()
    }

    val downTimerLiveData = mCountDownHelper.mCountDownLiveData // 验证码倒计时

    val mAuthSmsCodeLiveData = generatorLiveData<BaseResponse<RspSmsCode>>()

    val loginLiveData = generatorLiveData<BaseResponse<RspLoginInfo>>()

    private var mCodeUUid: ArrayList<String> = arrayListOf()

    fun reqSmsCode(mobile: String) {
        showloading()
        mAuthSmsCodeLiveData.addSourceLiveData(repository.reqSmsCode(mobile)) {
            hideLoading()
            if (it.isSuccess()) {
                mCodeUUid.add(it.getData()?.FSo4NScBct.orEmpty())
            }
            mAuthSmsCodeLiveData.postValue(it)
        }
    }

    fun reqLogin(mobile: String, smsCode: String) {
        showloading()
        loginLiveData.addSourceLiveData(repository.loginSms(mobile, smsCode, mCodeUUid.joinToString(","))) {
            hideLoading()
            if (it.isSuccess()) {
                val ctx = getAppContext()
                registWifi = WifiHelper.getSSid(ctx)
                registIP = WifiHelper.getIp()
                saveMobile(mobile)
                it.getData()?.let { data ->
                    saveUserInfo(data)
                    if (data.rSXY6ttC3w == "1") {
                        GPInfoUtils.saveTag(GPInfoUtils.TAG1)
                    }
                }
            }
            loginLiveData.postValue(it)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            mCountDownHelper.stopCountdown()
            source.lifecycle.removeObserver(this)

        }
    }


    fun startCountdown(type: Int = CountDownHelper.TYPE_SMS, mobile: String) {
        mCountDownHelper.stopCountdown()
        mCountDownHelper.startCountDown(type = type, mobile = mobile)
    }
}