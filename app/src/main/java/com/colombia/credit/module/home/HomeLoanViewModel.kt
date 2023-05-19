package com.colombia.credit.module.home

import com.colombia.credit.bean.resp.*
import com.colombia.credit.expand.*
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
    val waitConfirmLiveData = generatorLiveData<ArrayList<RepeatWaitConfirmInfo>?>() // 复贷待确认产品列表

    val mRspInfoLiveData = generatorLiveData<RspProductInfo>()

    val mCertProcessLiveData = generatorLiveData<BaseResponse<RspCertProcessInfo>>()

    init {
        _homeLiveData.observerNonStickyForever { response ->
            if (response.isSuccess()) {
                response.getData()?.let { info ->
                    isRepeat = info.EqyO == "0"
                    orderStatus = info.xXkO
                    mUserName = info.HyulExS1ei.orEmpty()
                    saveMobile(info.cusTell.orEmpty())

                    mRspInfoLiveData.postValue(info)

                    if (repeatProductLiveData.value != info.jBRR || repeatProductLiveData.value == null) {
                        repeatProductLiveData.postValue(info.jBRR)
                    }
                    // 复贷还款信息
                    repeatRepayLiveData.postValue(info.gQ1J)
                    // 复贷待确认订单
                    waitConfirmLiveData.postValue(info.Jg4g2)

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

    fun clearData() {
        _homeLiveData.value = BaseResponse(ResponseCode.OTHER_ERROR_CODE, RspProductInfo(), null)
    }
}