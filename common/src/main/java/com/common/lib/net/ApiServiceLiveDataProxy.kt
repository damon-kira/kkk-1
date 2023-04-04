package com.common.lib.net

import androidx.lifecycle.LiveData
import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable

/**
 * Created by weisl on 2019/8/28.
 */

object ApiServiceLiveDataProxy {

    @JvmStatic
    fun <T>request(flowable: () -> Flowable<BaseResponse<T>>): LiveData<BaseResponse<T>> =
        LiveDataCall(true, flowable)

    @JvmStatic
    fun <T> requestIgnoreLogin(flowable: () -> Flowable<BaseResponse<T>>): LiveData<BaseResponse<T>> =
        LiveDataCall(false, flowable)
}
