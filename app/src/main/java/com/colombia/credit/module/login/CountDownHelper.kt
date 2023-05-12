package com.colombia.credit.module.login

import android.os.CountDownTimer
import androidx.lifecycle.MediatorLiveData
import com.util.lib.TimerUtil

// 倒计时
class CountDownHelper {

    companion object {
        fun get() = Holder.INSTANCE

        private object Holder {
            val INSTANCE = CountDownHelper()
        }

        const val TYPE_SMS = 1
        const val TYPE_VOICE = 2
        const val TYPE_AUTO = 3
    }

    private var mCurrCodeType = -1 // 短信 or 语音

    private var mCountDownTimer: CountDownTimer? = null

    val mCountDownLiveData = MediatorLiveData<Long>()

    private var isDownEnd = true // 是否倒计时结束

    private var mCurrMobile: String = "" // 记录当前正在倒计时的手机号,倒计时结束后清空

    /**
     * @param downMill 默认60s
     * @param type
     */
    fun startCountDown(
        downMill: Long = TimerUtil.ONE_MIN_MILLISECONDS + TimerUtil.COUNTDOWNTIMER_IV,
        type: Int,
        mobile: String
    ) {
        mCurrCodeType = type
        mCurrMobile = mobile
        mCountDownTimer = createCountDownTimer(
            totalTimer = downMill,
            onTimeTick = { second ->
                isDownEnd = second == 0L
                mCountDownLiveData.postValue(second)
            },
            onTimerFinish = {
                reset()
                mCountDownLiveData.postValue(-1L)
            })
    }



    /**
     * 停止倒计时
     */
    fun stopCountdown() {
        reset()
        mCountDownTimer?.let {
            it.cancel()
            mCountDownTimer = null
        }
    }

    fun getCodeType() = mCurrCodeType

    fun isEnd() = isDownEnd

    fun getCurrMobile() = mCurrMobile

    private fun reset() {
        mCurrMobile = ""
        isDownEnd = true
        mCurrCodeType = -1
    }
}

fun createCountDownTimer(
    totalTimer: Long = (TimerUtil.ONE_MIN_MILLISECONDS + TimerUtil.COUNTDOWNTIMER_IV),
    countDownInterval: Long = TimerUtil.ONE_SECCOND_MILLISECONDS,
    onTimeTick: (second: Long) -> Unit,
    onTimerFinish: () -> Unit = {}
): CountDownTimer? {
    return object : CountDownTimer(totalTimer, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val second = millisUntilFinished / countDownInterval - 1
            if (second >= 0L) {
                onTimeTick(second)
            }
        }

        override fun onFinish() {
            onTimerFinish()
        }
    }.start()
}
