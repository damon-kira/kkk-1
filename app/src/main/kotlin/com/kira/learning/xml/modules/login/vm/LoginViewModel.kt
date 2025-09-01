package com.kira.learning.xml.modules.login.vm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.model.res.RspLoginInfo
import com.kira.learning.model.res.RspSmsCode
import com.kira.learning.xml.expand.saveMobile
import com.kira.learning.xml.expand.saveUserInfo
import com.kira.learning.xml.modules.login.CountDownHelper
import com.kira.learning.xml.modules.login.repo.LoginRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// 登录页面
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel(), LifecycleEventObserver {

    companion object {
        private const val SMS_TYPE_NORMAL = "phone"
        private const val SMS_TYPE_VOICE = "phonesounds"

        const val TYPE_SMS = CountDownHelper.Companion.TYPE_SMS
        const val TYPE_VOICE = CountDownHelper.Companion.TYPE_VOICE

        fun type2ReqType(type: Int): String {
            return if (type == TYPE_SMS) SMS_TYPE_NORMAL else SMS_TYPE_VOICE
        }
    }

    private val mCountDownHelper by lazy(LazyThreadSafetyMode.NONE) {
        CountDownHelper.Companion.get()
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
            hideLoading()
            if (it.isSuccess()) {
                saveMobile(mobile)
                it.getData()?.let { data ->
                    saveUserInfo(data)
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

    fun startCountdown(type: Int = CountDownHelper.Companion.TYPE_SMS, mobile: String) {
        if (isDown30Auto) return
        mCountDownHelper.stopCountdown()
        mCountDownHelper.startCountDown(type = type, mobile = mobile)
    }
}