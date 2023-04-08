package com.colombia.credit.module.home

import com.colombia.credit.bean.resp.*
import com.colombia.credit.expand.inValidToken
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeLoanViewModel @Inject constructor(private val repository: HomeLoanRepository) :
    BaseViewModel() {

    private val _homeLiveData = generatorLiveData<BaseResponse<RspProductInfo>>()
    val mHomeLiveData = _homeLiveData

    val firstProductLiveData = generatorLiveData<ArrayList<FirstConfirmInfo>>() // 首贷
    val repeatProductLiveData = generatorLiveData<ArrayList<RepeatProductInfo>?>() // 复贷产品列表
    val repeatRepayLiveData = generatorLiveData<ArrayList<RepeatRepayInfo>>() // 复贷还款列表
    val repeatConfirmLiveData = generatorLiveData<ArrayList<RepeatWaitConfirmInfo>>() // 复贷待确认产品列表

    val mRspInfoLiveData = generatorLiveData<RspProductInfo>()

    var mRspProductInfo: RspProductInfo? = null

    init {
        _homeLiveData.observerNonStickyForever {response ->
            if (response.isSuccess()) {
                response.getData()?.let { info ->
                    info.fyEV?.let { list ->
                        firstProductLiveData.postValue(info.fyEV)
                    }
                    repeatProductLiveData.postValue(info.jBRR)

                    info.gQ1J?.let {
                        repeatRepayLiveData.postValue(it)
                    }
                    info.Jg4g2?.let {
                        repeatConfirmLiveData.postValue(it)
                    }
                    mRspInfoLiveData.postValue(info)
                }
            }
        }
    }

    fun getHomeInfo() {
        if (inValidToken()) return
        mHomeLiveData.addSourceLiveData(repository.getHomeInfo()) { response ->
            _homeLiveData.postValue(response)
        }
    }
}