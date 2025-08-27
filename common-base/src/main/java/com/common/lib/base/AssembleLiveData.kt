package com.common.lib.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * 组装信息的LiveData，可以清除数据源
 * 功能：多个数据源组合的数据，根据数据源的数量判断是否组合完成
 * 可以清除所有数据源
 */
open class AssembleLiveData<T>(protected val data: T) : MediatorLiveData<T>() {
    private val sourceList = mutableListOf<LiveData<*>>()
    private val isAssembleComplete: Boolean get() = sourceList.size == 0

    fun <S> assemble(source: LiveData<S>, assemble: (result: T, source: S) -> Unit) {
        addSource(source) {
            removeSource(source)
            assemble(data, it)
            if (isAssembleComplete) {
                postValue(data)
            }
        }
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        sourceList.add(source)
        super.addSource(source, onChanged)
    }

    override fun <S : Any?> removeSource(toRemote: LiveData<S>) {
        super.removeSource(toRemote)
        sourceList.remove(toRemote)
    }

    fun removeAllSources() {
        sourceList.forEach { super.removeSource(it) }
        sourceList.clear()
    }
}