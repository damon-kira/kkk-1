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
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
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

    private var mCurrMobile: String? = null

    // 输入手机号后是否自动触发获取验证码
    var isAutoGetCode: Boolean = false

    // 第一次触发验证码后，倒计时30s，若没有调用登录接口，则再次发送验证码
    private var mDown30Mill: Disposable? = null

    var isDown30Auto = false

    fun reqSmsCode(mobile: String, isAuto: Boolean) {
        if (!isDown30Auto)
            showloading()
        isAutoGetCode = isAuto
        mAuthSmsCodeLiveData.addSourceLiveData(repository.reqSmsCode(mobile)) {
            hideLoading()
            mCurrMobile = mobile
            if (it.isSuccess()) {
                if (!isDown30Auto)
                    startDown30Mill()
                mCodeUUid.add(it.getData()?.FSo4NScBct.orEmpty())
            }
            mAuthSmsCodeLiveData.postValue(it)
        }
    }

    fun reqLogin(mobile: String, smsCode: String) {
        showloading()
        cancelDown30()
        loginLiveData.addSourceLiveData(
            repository.loginSms(
                mobile,
                smsCode,
                mCodeUUid.joinToString(",")
            )
        ) {
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

    private fun startDown30Mill() {
        mDown30Mill = Flowable.just(1)
            .delay(30, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isDown30Auto = true
                reqSmsCode(mCurrMobile.orEmpty(), isAutoGetCode)
            }, {

            })
    }

    fun cancelDown30() {
        mDown30Mill?.dispose()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            mCountDownHelper.stopCountdown()
            mDown30Mill?.dispose()
            source.lifecycle.removeObserver(this)

        }
    }

    fun startCountdown(type: Int = CountDownHelper.TYPE_SMS, mobile: String) {
        if (isDown30Auto) return
        mCountDownHelper.stopCountdown()
        mCountDownHelper.startCountDown(type = type, mobile = mobile)
    }
}