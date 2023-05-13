package com.colombia.credit.module.login

import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.colombia.credit.app.getAppContext
import com.colombia.credit.util.SmsContentObserver
import com.colombia.credit.util.registerSmsObserver
import com.colombia.credit.util.unRegisterSmsObserver

// 自动获取验证码
class SmsCodeHelper : LifecycleEventObserver {

    private val mSmsObserver by lazy {
        SmsContentObserver(getAppContext())
    }

    private var mEditText: EditText? = null

    val codeLivedata = MutableLiveData<String>()

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
            codeLivedata.postValue(code)
        }
    }

    fun unRegisterObserver(editText: EditText?) {
        mSmsObserver.setOnCodeCallBack(null)
        editText?.unRegisterSmsObserver(mSmsObserver)
    }
}