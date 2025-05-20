package com.kira.learning.module.repeat

import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.util.lib.MainHandler
import com.util.lib.log.logger_d
import io.reactivex.rxjava3.core.Flowable // Changed to RxJava3
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers // Changed to RxJava3
import io.reactivex.rxjava3.disposables.Disposable // Changed to RxJava3
import io.reactivex.rxjava3.schedulers.Schedulers // Changed to RxJava3
import java.util.concurrent.TimeUnit

// 推荐弹窗帮助类
class RecommHelper : LifecycleEventObserver {

    private val TAG = "debug_RecommHelper"

    private var mCountDownTime: Long = 0

    private var mDisposable: Disposable? = null

    val mCountDownLivedata = MutableLiveData<Int>()

    fun startCountDown() {
        start()
    }

    private fun start() {
        logger_d(TAG, "countdown time = $mCountDownTime")
        if (mCountDownTime == 0L) return
        mDisposable = Flowable.just(1)
            .delay(mCountDownTime, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback()
            }, {
                // Error handling remains the same
            })
    }

    private fun callback() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mCountDownLivedata.postValue(1)
        } else {
            MainHandler.post {
                mCountDownLivedata.postValue(1)
            }
        }
    }

    fun setCountDownTime(time: Long) {
        mCountDownTime = time
    }

    fun reset() {
        logger_d(TAG, "reset: ===================")
        cancel()
        start()
    }

    fun cancel() {
        logger_d(TAG, "cancel: ===================")
        mDisposable?.let {
            if (!it.isDisposed) it.dispose()
        }
        mDisposable = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            cancel()
        }
    }
}