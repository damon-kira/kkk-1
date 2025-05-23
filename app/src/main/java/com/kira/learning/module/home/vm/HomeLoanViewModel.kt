package com.kira.learning.module.home.vm

import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.bean.resp.RepeatProductInfo
import com.kira.learning.bean.resp.RepeatRepayInfo
import com.kira.learning.bean.resp.RepeatWaitConfirmInfo
import com.kira.learning.bean.resp.RspCertProcessInfo
import com.kira.learning.bean.resp.RspProductInfo
import com.kira.learning.expand.isGpAccount
import com.kira.learning.expand.isRepeat
import com.kira.learning.expand.mUserName
import com.kira.learning.expand.orderStatus
import com.kira.learning.expand.saveMobile
import com.kira.learning.module.home.repo.HomeLoanRepository
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