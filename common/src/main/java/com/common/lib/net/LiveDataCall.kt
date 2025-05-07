package com.common.lib.net

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.common.lib.net.bean.BaseResponse
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.net.ssl.SSLHandshakeException


class LiveDataCall<T>(
    private val clazz: Class<T>?,
    private val skipLogin: Boolean,
    private val flowable: () -> Flowable<BaseResponse<T>>
) : LiveData<BaseResponse<T>>() {

    private var mDispose: Disposable? = null

    @SuppressLint("CheckResult")
    override fun onActive() {
        mDispose = Flowable.just(0)
            .flatMap {
                flowable()
            }
            /*.doOnNext {
                if (!it.isSuccess()) {
                    throw HttpResponseException(it.code, it.msg, it.e)
                }*//* else {
                    it.parseT(clazz)
                }*//*
            }*/
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                logger_d("debug_LiveDataCall", "onActive success = $it")
                if (!it.isSuccess()) {
                    val exception = HttpResponseException(it.code, it.msg, it.e)
                    postValue(getErrorReponse(exception))
                    ServiceClient.getInstance().getGlobalFailedListener()
                        ?.onFailed(getErrorReponse(exception), skipLogin)
                } else {
                    postValue(it)
                }
            }, { throwable ->
                logger_e("debug_LiveDataCall", "onActive error ===  $throwable")
                postValue(getErrorReponse(throwable))
                ServiceClient.getInstance().getGlobalFailedListener()
                    ?.onFailed(getErrorReponse(throwable), skipLogin)
            })
    }


    override fun onInactive() {
        super.onInactive()
//        mDispose?.dispose()
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
        return BaseResponse(code, null, msg, throwable)
    }

    private fun getErrorCode(throwable: Throwable): Int {
        var code: Int = ResponseCode.OTHER_ERROR_CODE
        if (throwable is HttpResponseException) {
            code = throwable.code
        }
        return code
    }
}