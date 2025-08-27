package com.common.lib.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.common.lib.net.bean.BaseResponse

class OneShotResponseObserver<T>(
    private val data: LiveData<BaseResponse<T>>,
    private val changedFn: (BaseResponse<T>?) -> Unit
) : Observer<BaseResponse<T>> {
    override fun onChanged(value: BaseResponse<T>) {
        data.removeObserver(this)
        changedFn(value)
    }
}

class OneShotObserver<T>(
    private val data: LiveData<T>,
    private val changedFn: (T?) -> Unit
) : Observer<T> {
    override fun onChanged(value: T) {
        data.removeObserver(this)
        changedFn(value)
    }
}

fun <T> LiveData<BaseResponse<T>>.addOneShotResponseObserver(changedFn: (BaseResponse<T>?) -> Unit) {
    observeForever(OneShotResponseObserver(this, changedFn))
}

fun <T> LiveData<BaseResponse<T>>.addOneShotResponseObserver(
    owner: LifecycleOwner,
    changedFn: (BaseResponse<T>?) -> Unit
) {
    observe(owner, OneShotResponseObserver(this, changedFn))
}

fun <T> LiveData<T>.addOneShotObserver(
    owner: LifecycleOwner,
    changedFn: (T?) -> Unit
) {
    observe(owner, OneShotObserver(this, changedFn))
}

fun <T> LiveData<T>.addOneShotObserveForever(
    changedFn: (T?) -> Unit
) {
    observeForever(OneShotObserver(this, changedFn))
}


fun <T> LiveData<BaseResponse<T>>.load() {
    observeForever(OneShotResponseObserver(this) {
        // Do nothing. Empty observer is added only to kick off resource loading.
    })
}