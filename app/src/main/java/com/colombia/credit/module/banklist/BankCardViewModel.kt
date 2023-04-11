package com.colombia.credit.module.banklist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colombia.credit.bean.resp.RspBankAccount
import com.colombia.credit.bean.resp.RspBankNameInfo
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonSticky
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankCardViewModel @Inject constructor(private val repository: BankCardRepository) :
    BaseViewModel(), LifecycleEventObserver {

    val mBankNameLiveData = generatorLiveData<ArrayList<RspBankNameInfo.BankNameInfo>>()

    val mBankAccountLiveData = generatorLiveData<BaseResponse<RspBankAccount>>()

    var mRspBankNameList: ArrayList<RspBankNameInfo.BankNameInfo>? = null
        get() {
            if (field == null) {
                getBankName(true)
            }
            return field
        }

    fun getBankAccountList() {
        mBankAccountLiveData.addSourceLiveData(repository.getBankAccountList()) {
            mBankAccountLiveData.postValue(it)
        }
    }

    fun getBankName(showLoading: Boolean = false) {
        if (showLoading)
            showloading()
        mBankNameLiveData.addSourceLiveData(repository.getBankName()) {
            hideLoading()
            if (it.isSuccess()) {
                it.getData()?.list?.apply {
                    mRspBankNameList = this
                }
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            mBankNameLiveData.observerNonSticky(source) {}
            getBankName()
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
        }
    }
}