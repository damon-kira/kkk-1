package com.colombia.credit.module.home

import com.colombia.credit.bean.resp.*
import com.colombia.credit.expand.*
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

    val repeatProductLiveData = generatorLiveData<ArrayList<RepeatProductInfo>>() // 复贷产品列表
    val repeatRepayLiveData = generatorLiveData<RepeatRepayInfo>() // 复贷还款列表
//    val repeatConfirmLiveData = generatorLiveData<ArrayList<RepeatWaitConfirmInfo>>() // 复贷待确认产品列表

    val mRspInfoLiveData = generatorLiveData<RspProductInfo>()

    val mCertProcessLiveData = generatorLiveData<BaseResponse<RspCertProcessInfo>>()

    init {
        _homeLiveData.observerNonStickyForever { response ->
            if (response.isSuccess()) {
                response.getData()?.let { info ->
                    isGp = info.Wg5u.equals("G", true)
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
            if (isGp) {
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