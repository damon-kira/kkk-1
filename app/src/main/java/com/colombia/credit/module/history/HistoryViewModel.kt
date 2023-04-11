package com.colombia.credit.module.history

import com.colombia.credit.bean.resp.RspHistoryInfo
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import com.project.util.AESNormalUtil
import com.util.lib.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: HistoryRepository) :
    BaseViewModel() {

    val mInfoLiveData = generatorLiveData<BaseResponse<RspHistoryInfo>>()

    fun getHistoryList() {
        showloading()
        mInfoLiveData.addSourceLiveData(repository.getHistoryList()) {
            hideLoading()
//            val list = createData()
//            val json = GsonUtil.toJson(list)
//            val encrypt = AESNormalUtil.mexicoEncrypt(json.orEmpty(), true)
//            mInfoLiveData.postValue(BaseResponse<RspHistoryInfo>(200, json, ""))
            mInfoLiveData.postValue(it)
        }
    }

    private val STATUS_REVIEW = "02" //审核中
    private val STATUS_VERIFI = "03" //审核通过
    private val STATUS_REFUSED = "04" //拒单
    private val STATUS_REPAY = "07" //未逾期
    private val STATUS_OVERDUE = "08" //已逾期
    private val STATUS_FAILURE = "09" //异常关闭，失效状态
    private val STATUS_SETTLE = "10" //已结清

    private fun createData(): RspHistoryInfo {
        var hlDgN: String? = null       // 订单状态
        var pnFU: String? = null        // 待还款金额
        var znxlON0: String? = null     // 还款日期
        var H0WVJP: String? = null      // 拒绝日期
        val list = arrayListOf<RspHistoryInfo.HistoryOrderInfo>()

        var info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_REVIEW
        info.pnFU = "360000"
        info.znxlON0 = "12/05/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)

        info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_VERIFI
        info.pnFU = "460000"
        info.znxlON0 = "12/06/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)

        info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_REFUSED
        info.pnFU = "560000"
        info.znxlON0 = "30/05/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)

        info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_REPAY
        info.pnFU = "660000"
        info.znxlON0 = "26/05/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)

        info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_OVERDUE
        info.pnFU = "760000"
        info.znxlON0 = "02/05/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)

        info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_FAILURE
        info.pnFU = "860000"
        info.znxlON0 = "22/05/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)

        info = RspHistoryInfo.HistoryOrderInfo()
        info.hlDgN = STATUS_SETTLE
        info.pnFU = "960000"
        info.znxlON0 = "12/06/2023"
        info.H0WVJP = "03/04/2023"
        list.add(info)
        return RspHistoryInfo().also {
            it.list = list
        }
    }
}