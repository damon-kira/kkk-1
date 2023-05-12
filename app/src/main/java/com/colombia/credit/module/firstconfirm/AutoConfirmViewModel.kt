package com.colombia.credit.module.firstconfirm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.expand.getMobile
import com.colombia.credit.module.login.CountDownHelper
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

// 自动确认额度
class AutoConfirmViewModel @Inject constructor(
    private val repository: AutoConfirmRepository
) : BaseViewModel(), LifecycleEventObserver {

    private val mCountDownHelper by lazy(LazyThreadSafetyMode.NONE) {
        CountDownHelper.get()
    }

    val downTimerLiveData = mCountDownHelper.mCountDownLiveData // 验证码倒计时

    val cancelLiveData = generatorLiveData<BaseResponse<RspResult>>()

    fun getDownTimeMill(id1: String, productId: String) {
        downTimerLiveData.addSourceLiveData(repository.getTimeMill(id1, productId)) {
            if (it.isSuccess()) {
                if ((it.getData() ?: 0) > 0) {
                    startCountdown((it.getData() ?: 0) * 1000)
                }
            }
        }
    }

    fun cancel(id1: String, productId: String) {
        showloading()
        cancelLiveData.addSourceLiveData(repository.cancel(id1, productId)) {
            hideLoading()
            cancelLiveData.postValue(it)
        }
    }

    /**
     * @param millTime 毫秒值
     */
    fun startCountdown(millTime: Long) {
        mCountDownHelper.stopCountdown()
        mCountDownHelper.startCountDown(
            type = CountDownHelper.TYPE_AUTO,
            mobile = getMobile(),
            downMill = millTime
        )
    }

    fun stopCountDown() {
        mCountDownHelper.stopCountdown()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            mCountDownHelper.stopCountdown()
            source.lifecycle.removeObserver(this)
        }
    }
}