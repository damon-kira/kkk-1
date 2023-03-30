package com.common.lib.net

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.common.lib.net.bean.BaseResponse
import com.util.lib.log.logger_e
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.net.ssl.SSLHandshakeException


/**
 * Created by weisl on 2019/8/12.
 */
class LiveDataCall<T>(
    private val skipLogin: Boolean,
    private val clazz: Class<T>,
    private val flowable: () -> Flowable<BaseResponse<T>>
) : LiveData<BaseResponse<T>>() {

    private var mDispose: Disposable? = null
    @SuppressLint("CheckResult")
    override fun onActive() {
        mDispose = Flowable.just(0)
            .flatMap {
                flowable()
            }.doOnNext {
                if (it.isSuccess()) {
                    it.parseT(clazz)
                } else if (!it.isAppForcedUpdate() || it.data.isEmpty()) {
                    throw HttpResponseException(it.code, it.message)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isAppForcedUpdate()) {
                    ServiceClient.getInstance().getGlobalFailedListener()?.onFailed(it,skipLogin)
                } else {
                    postValue(it)
                }
            }, { throwable ->
                logger_e("debug_LiveDataCall", "onActive error ===  $throwable")
                postValue(getErrorReponse(throwable))
                ServiceClient.getInstance().getGlobalFailedListener()?.onFailed(getErrorReponse(throwable),skipLogin)
            })
    }

    override fun onInactive() {
        super.onInactive()
        mDispose?.dispose()
    }

    private fun getErrorReponse(throwable: Throwable): BaseResponse<T> {
        var msg: String = ""
        var code: Int = ResponseCode.OTHER_ERROR_CODE
        if (throwable is HttpResponseException) {
            code = throwable.code
            msg = throwable.message.orEmpty()
        } else if (throwable is SSLHandshakeException) {
            code = ResponseCode.SSL_ERROR_CODE
        }
        return BaseResponse(code, "", msg)
    }

    private fun getErrorCode(throwable: Throwable): Int {
        var code: Int = ResponseCode.OTHER_ERROR_CODE
        if (throwable is HttpResponseException) {
            code = throwable.code
        }
        return code
    }
}