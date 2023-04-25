package com.colombia.credit.module.banklist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colombia.credit.bean.resp.RspBankAccount
import com.colombia.credit.bean.resp.RspBankNameInfo
import com.colombia.credit.bean.resp.RspResult
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonSticky
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class BankCardViewModel @Inject constructor(private val repository: BankCardRepository) :
    BaseViewModel(), LifecycleEventObserver {

    val mBankNameLiveData = generatorLiveData<ArrayList<RspBankNameInfo.BankNameInfo>>()

    val mBankAccountLiveData = generatorLiveData<BaseResponse<RspBankAccount>>()

    val mUpdateLiveData = generatorLiveData<BaseResponse<RspResult>>()

    private var _rspBankNameList: ArrayList<RspBankNameInfo.BankNameInfo>? = null
        set(value) {
            value ?: return
            val hotList = value.filter { it.isHot() }
            val sortList = value.filter { !it.isHot() }.sortedBy { it.getTag() }.toMutableList()
            sortList.addAll(0, hotList)
            field = sortList as ArrayList<RspBankNameInfo.BankNameInfo>
        }

    val mRspBankNameList: ArrayList<RspBankNameInfo.BankNameInfo>?
        get() {
            if (_rspBankNameList == null) {
                getBankName(true)
            }
            return _rspBankNameList
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
                it.getData()?.nefV2g8cf0?.apply {
                    _rspBankNameList = this
                }
            }
        }
    }

    fun updateBank(bankNo: String, productId: String?) {
        showloading()
        mUpdateLiveData.addSourceLiveData(repository.updateBank(bankNo, productId)) {
            hideLoading()
            mUpdateLiveData.postValue(it)
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