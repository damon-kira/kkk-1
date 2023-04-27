package com.common.lib.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by weisl on 2019/10/15.
 */
object LiveDataBus {

    private val bus: ConcurrentHashMap<Class<*>, MutableLiveData<*>> by lazy(LazyThreadSafetyMode.NONE) {
        ConcurrentHashMap<Class<*>, MutableLiveData<*>>()
    }

    @JvmStatic
    @MainThread
    inline fun <T> with(clazz: Class<T>): MutableLiveData<T> {
        return getLiveData(clazz)
    }

    @JvmStatic
    inline fun <reified T> post(t: T) {
        getLiveData(T::class.java).postValue(t)
    }

    @JvmStatic
    fun <T> getLiveData(clazz: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(clazz)) {
            bus[clazz] = MutableLiveData<T>()
        }
        return bus[clazz] as MutableLiveData<T>
    }

    @JvmStatic
    fun <T> remove(clazz: Class<T>) {
        if (bus.containsKey(clazz)) {
            bus.remove(clazz)
        }
    }

    fun <T> removeObserve(clazz: Class<T>, observer: Observer<T>?, isRemoveClazz: Boolean = false) {
        observer?.let {
            if (bus.containsKey(clazz)) {
                if (observer is LiveDataBusObserve) {
                    observer.onChange = null
                }
                getLiveData(clazz).removeObserver(it)
                if (isRemoveClazz) {
                    remove(clazz)
                }
            }
        }
    }

    @JvmStatic
    fun getLiveDataVersion(liveData: LiveData<*>): Int {
        val versionField = LiveData::class.java.getDeclaredField("mVersion")
        versionField.isAccessible = true
        return versionField.get(liveData) as Int
    }
}

fun <T> LiveData<T>.observerNonStickyForever(onChange: (t: T) -> Unit): LiveDataBusObserve<T> {
    val observe = LiveDataBusObserve(this, onChange)
    observeForever(observe)
    return observe
}

fun <T> LiveData<T>.observerNonSticky(owner: LifecycleOwner, onChange: (t: T) -> Unit) {
    observe(owner, LiveDataBusObserve(this, onChange))
}

/**
 * 非黏性observer
 */
class LiveDataBusObserve<T>(
    private val liveData: LiveData<T>,
    var onChange: ((t: T) -> Unit)?
) :
    Observer<T> {
    var mLastVersion: Int = LiveDataBus.getLiveDataVersion(liveData)
    private val mIsFirstVersion = AtomicBoolean(true)
    override fun onChanged(t: T) {
        if (mIsFirstVersion.compareAndSet(true, false)) {
            //只走一次之后 后面的就正常了 走正常回调
            val currentLiveDataVersion = LiveDataBus.getLiveDataVersion(liveData)
            if (mLastVersion >= currentLiveDataVersion) {
                return
            }
            mLastVersion = currentLiveDataVersion
            onChange?.invoke(t)
        } else {
            onChange?.invoke(t)
        }
    }
}