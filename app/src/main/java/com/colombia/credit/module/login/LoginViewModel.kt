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
import com.datepicker.lib.Log
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

    companion object {
        private const val SMS_TYPE_NORMAL = "phone"
        private const val SMS_TYPE_VOICE = "phonesounds"

        const val TYPE_SMS = CountDownHelper.TYPE_SMS
        const val TYPE_VOICE = CountDownHelper.TYPE_VOICE

        fun type2ReqType(type: Int): String {
            return if (type == TYPE_SMS) SMS_TYPE_NORMAL else SMS_TYPE_VOICE
        }
    }

    private val mCountDownHelper by lazy(LazyThreadSafetyMode.NONE) {
        CountDownHelper.get()
    }

    val downTimerLiveData = mCountDownHelper.mCountDownLiveData // 验证码倒计时

    val mAuthSmsCodeLiveData = generatorLiveData<BaseResponse<RspSmsCode>>()

    val loginLiveData = generatorLiveData<BaseResponse<RspLoginInfo>>()

    private val _voiceLivedata = generatorLiveData<Boolean>()
    val mVoiceLiveData = _voiceLivedata

    private var mCodeUUid: ArrayList<String> = arrayListOf()

    private var mCurrMobile: String? = null

    private var mCurrCodeType: Int = -1

    private var mSmsCodeCount = 0
        private set(value) {
            field = value
            if (value > 2) {
                _voiceLivedata.postValue(true)
            }
        }

    // 输入手机号后是否自动触发获取验证码
    var isAutoGetCode: Boolean = false

    // 第一次触发验证码后，倒计时30s，若没有调用登录接口/没有读取到验证码，则再次发送验证码
    private var mDown30Mill: Disposable? = null

    var isDown30Auto = false

    fun reqSmsCode(mobile: String, isAuto: Boolean, type: Int = TYPE_SMS) {
        mCurrCodeType = type
        if (!isDown30Auto)
            showloading()
        isAutoGetCode = isAuto
        mAuthSmsCodeLiveData.addSourceLiveData(repository.reqSmsCode(type2ReqType(type), mobile)) {
            hideLoading()
            mCurrMobile = mobile
            if (it.isSuccess()) {
                if (!isDown30Auto) {
                    startDown30Mill()
                    mSmsCodeCount++
                }
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
//            Log.e("iiiit msg:   ",it.msg)
//            Log.e("iiiit data:   ",it.data.toString())
//            Log.e("iiiit code:   ",it.code.toString())
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
                reqSmsCode(mCurrMobile.orEmpty(), isAutoGetCode, getCurrCodeType())
            }, {

            })
    }

    fun cancelDown30() {
        mDown30Mill?.dispose()
    }

    fun getCurrCodeType(): Int {
        return mCurrCodeType
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