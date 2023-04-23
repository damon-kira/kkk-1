package com.colombia.credit.module.home

import com.colombia.credit.bean.resp.*
import com.colombia.credit.expand.inValidToken
import com.colombia.credit.expand.isGp
import com.colombia.credit.expand.mUserName
import com.colombia.credit.expand.saveMobile
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeLoanViewModel @Inject constructor(private val repository: HomeLoanRepository) :
    BaseViewModel() {

    private val _homeLiveData = generatorLiveData<BaseResponse<RspProductInfo>>()
    val mHomeLiveData = _homeLiveData

    val firstConfirmLiveData = generatorLiveData<ArrayList<FirstConfirmInfo>>() // 首贷确认额度
    val repeatProductLiveData = generatorLiveData<ArrayList<RepeatProductInfo>>() // 复贷产品列表
    val repeatRepayLiveData = generatorLiveData<RepeatRepayInfo>() // 复贷还款列表
//    val repeatConfirmLiveData = generatorLiveData<ArrayList<RepeatWaitConfirmInfo>>() // 复贷待确认产品列表

    val mRspInfoLiveData = generatorLiveData<RspProductInfo>()

    val mCertProcessLiveData = generatorLiveData<BaseResponse<RspCertProcessInfo>>()

    init {
        _homeLiveData.observerNonStickyForever {response ->
            if (response.isSuccess()) {
                response.getData()?.let { info ->
                    isGp = info.Wg5u.equals("G", true)
                    mUserName = info.HyulExS1ei.orEmpty()
                    saveMobile(info.cusTell.orEmpty())
                    info.fyEV?.let {
                        firstConfirmLiveData.postValue(info.fyEV)
                    }

                    if (repeatProductLiveData.value != info.jBRR) {
                        repeatProductLiveData.postValue(info.jBRR)
                    }

                    info.gQ1J?.let {
                        repeatRepayLiveData.postValue(it)
                    }
//                    info.Jg4g2?.let {
//                        repeatConfirmLiveData.postValue(it)
//                    }
                    mRspInfoLiveData.postValue(info)
                }
            }
        }
    }

    fun getHomeInfo() {
        if (inValidToken()) return
        showloading()
        mHomeLiveData.addSourceLiveData(repository.getHomeInfo()) { response ->
            hideLoading()
            _homeLiveData.postValue(response)
        }
    }

    fun getCertProcess() {
        showloading()
        mCertProcessLiveData.addSourceLiveData(repository.getCertProcess()) {
            hideLoading()
            if (isGp) {
                mCertProcessLiveData.postValue(BaseResponse(ResponseCode.SUCCESS_CODE, RspCertProcessInfo(),null))
            } else {
                mCertProcessLiveData.postValue(it)
            }
        }
    }
}