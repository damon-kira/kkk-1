package com.kira.learning.module.login

import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kira.learning.di.getAppContext
import com.kira.learning.utils.SmsContentObserver
import com.kira.learning.utils.registerSmsObserver
import com.kira.learning.utils.unRegisterSmsObserver

// 自动获取验证码
class SmsCodeHelper : LifecycleEventObserver {

    private val mSmsObserver by lazy {
        SmsContentObserver(getAppContext())
    }

    private var mEditText: EditText? = null

    val codeLivedata = MutableLiveData<String>()

    var isAutoInsert = false

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            unRegisterObserver(mEditText)
            mEditText = null
        }
    }

    fun updateReceiverTime() {
        mSmsObserver.updateReceiverTime()
    }

    fun registerObserver(editText: EditText) {
        this.mEditText = editText
        editText.registerSmsObserver(mSmsObserver) { code ->
            isAutoInsert = true
            codeLivedata.postValue(code)
        }
    }

    fun unRegisterObserver(editText: EditText?) {
        editText?.unRegisterSmsObserver(mSmsObserver)
    }
}