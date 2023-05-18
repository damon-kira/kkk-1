package com.common.lib.net

import androidx.lifecycle.LiveData
import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable


object ApiServiceLiveDataProxy {

    @JvmStatic
    fun <T> request(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(clazz, true, flowable)

    @JvmStatic
    fun <T> request(
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(null, true, flowable)

    @JvmStatic
    fun <T> requestIgnoreLogin(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(clazz, false, flowable)
}
