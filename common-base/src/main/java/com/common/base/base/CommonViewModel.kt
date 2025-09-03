package com.common.base.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class CommonViewModel: ViewModel() {

    protected val TAG = "debug_${this.javaClass.simpleName}"

    val loading = MutableLiveData<Boolean>()

    fun showloading() {
        loading.postValue(true)
    }

    fun hideLoading() {
        loading.postValue(false)
    }

    protected fun <T> generatorLiveData() = MediatorLiveData<T>()

    protected fun <T, R> MediatorLiveData<R>.addSourceLiveData(source: LiveData<T>, onChange: (t: T) -> Unit) {
        addSource(source) {
            removeSource(source)
            onChange.invoke(it)
        }
    }
}