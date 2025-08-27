package com.common.lib.net

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.common.lib.net.bean.BaseResponse
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.net.ssl.SSLHandshakeException

class LiveDataCall<T>(
    private val clazz: Class<T>?,
    private val skipLogin: Boolean,
    private val flowable: () -> Flowable<BaseResponse<T>>
) : LiveData<BaseResponse<T>>() {

    // 保存RxJava订阅
    private var mDispose: Disposable? = null

    @SuppressLint("CheckResult")
    override fun onActive() {
        mDispose = Flowable.just(0)
            .flatMap {
                flowable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                logger_d("debug_LiveDataCall", "onActive success = $response")
                if (!response.isSuccess()) {
                    // 服务端返回错误码
                    val exception = HttpResponseException(response.code, response.msg, response.e)
                    postValue(getErrorResponse(exception))
                    ServiceClient.getInstance().getGlobalFailedListener()
                        ?.onFailed(getErrorResponse(exception), skipLogin)
                } else {
                    postValue(response)
                }
            }, { throwable ->
                // 失败回调（网络异常）
                logger_e("debug_LiveDataCall", "onActive error === $throwable")
                postValue(getErrorResponse(throwable))
                ServiceClient.getInstance().getGlobalFailedListener()
                    ?.onFailed(getErrorResponse(throwable), skipLogin)
            })
    }

    override fun onInactive() {
        super.onInactive()
        mDispose?.dispose()
    }

    private fun getErrorResponse(throwable: Throwable): BaseResponse<T> {
        var msg: String = ""
        var code: Int = ResponseCode.OTHER_ERROR_CODE
        when (throwable) {
            is HttpResponseException -> {
                code = throwable.code
                msg = throwable.message.orEmpty()
            }
            is SSLHandshakeException -> {
                code = ResponseCode.SSL_ERROR_CODE
            }
        }
        return BaseResponse(code, null, msg, throwable)
    }

    private fun getErrorCode(throwable: Throwable): Int {
        return if (throwable is HttpResponseException) {
            throwable.code
        } else {
            ResponseCode.OTHER_ERROR_CODE
        }
    }
}