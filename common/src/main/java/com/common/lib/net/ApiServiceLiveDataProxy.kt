package com.common.lib.net

import androidx.lifecycle.LiveData
import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable

/**
 * Created by weisl on 2019/8/28.
 */

object ApiServiceLiveDataProxy {

    @JvmStatic
    fun <T> request(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(clazz, true, flowable)

    @JvmStatic
    fun <T> requestIgnoreLogin(
        clazz: Class<T>,
        flowable: () -> Flowable<BaseResponse<T>>
    ): LiveData<BaseResponse<T>> =
        LiveDataCall(clazz, false, flowable)
}
