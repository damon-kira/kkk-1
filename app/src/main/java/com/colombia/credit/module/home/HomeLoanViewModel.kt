package com.colombia.credit.module.home

import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.bean.resp.RepeatRepayInfo
import com.colombia.credit.bean.resp.RspCertProcessInfo
import com.colombia.credit.bean.resp.RspProductInfo
import com.colombia.credit.expand.isGpAccount
import com.colombia.credit.expand.isRepeat
import com.colombia.credit.expand.mUserName
import com.colombia.credit.expand.saveMobile
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class HomeLoanViewModel @Inject constructor(private val repository: HomeLoanRepository) :
    BaseViewModel() {

    private val _homeLiveData = generatorLiveData<BaseResponse<RspProductInfo>>()
    val mHomeLiveData = _homeLiveData

    val repeatProductLiveData = generatorLiveData<ArrayList<RepeatProductInfo>>() // 复贷产品列表
    val repeatRepayLiveData = generatorLiveData<RepeatRepayInfo>() // 复贷还款列表
//    val repeatConfirmLiveData = generatorLiveData<ArrayList<RepeatWaitConfirmInfo>>() // 复贷待确认产品列表

    val mRspInfoLiveData = generatorLiveData<RspProductInfo>()

    val mCertProcessLiveData = generatorLiveData<BaseResponse<RspCertProcessInfo>>()

    init {
        _homeLiveData.observerNonStickyForever { response ->
            if (response.isSuccess()) {
                response.getData()?.let { info ->
                    isRepeat = info.EqyO == "0"
                    mUserName = info.HyulExS1ei.orEmpty()
                    saveMobile(info.cusTell.orEmpty())

                    if (repeatProductLiveData.value != info.jBRR) {
                        repeatProductLiveData.postValue(info.jBRR)
                    }

                    repeatRepayLiveData.postValue(info.gQ1J)
//                    info.Jg4g2?.let {
//                        repeatConfirmLiveData.postValue(it)
//                    }
                    mRspInfoLiveData.postValue(info)
                }
            }
        }
    }

    fun getHomeInfo() {
        mHomeLiveData.addSourceLiveData(repository.getHomeInfo()) { response ->
            _homeLiveData.postValue(response)
        }
    }

    fun getCertProcess() {
        showloading()
        mCertProcessLiveData.addSourceLiveData(repository.getCertProcess()) {
            hideLoading()
            if (isGpAccount()) {
                mCertProcessLiveData.postValue(
                    BaseResponse(
                        ResponseCode.SUCCESS_CODE,
                        RspCertProcessInfo(),
                        null
                    )
                )
            } else {
                mCertProcessLiveData.postValue(it)
            }
        }
    }

    fun clearData(){
        _homeLiveData.value = BaseResponse(ResponseCode.OTHER_ERROR_CODE, RspProductInfo(), null)
    }
}