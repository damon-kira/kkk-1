package com.colombia.credit.module.repay

import com.colombia.credit.bean.resp.RspRepayOrders
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RepayTabViewModel @Inject constructor(private val repository: RepayTabRepository) :
    BaseViewModel() {

    private val _ordersLivedata = generatorLiveData<BaseResponse<RspRepayOrders>>()
    val ordersLivedata = _ordersLivedata

    fun getRepayOrders() {
        _ordersLivedata.addSourceLiveData(repository.getRepayOrders()) {
//            val createData = createData()
//            _ordersLivedata.postValue(BaseResponse(ResponseCode.SUCCESS_CODE, GsonUtil.toJson(createData), ""))
            _ordersLivedata.postValue(it)
        }
    }

    fun createData(): RspRepayOrders {
        val list = ArrayList<RspRepayOrders.RepayOrderDetail>()
        var C2O8E6jjzd: String? = null //产品名字
        var Eff0nA: Int = 0     //待还金额
        var zbRV6Lg8jO: String? = null //还款日期
        var gzBTFx: String? = null     //是否逾期
        var QiZorG: String? = null     //1代表勾选中
        var repayOrderDetail = RspRepayOrders.RepayOrderDetail()
        val random = Random(100000)
        for (index in 0 .. 10) {
            repayOrderDetail.C2O8E6jjzd = "dddfjdl$index"
            repayOrderDetail.Eff0nA = random.nextInt(1000000)
            repayOrderDetail.zbRV6Lg8jO = "0$index/05/2023"
            repayOrderDetail.QiZorG = if (index % 3 == 0) "1" else "0"
            repayOrderDetail.gzBTFx = if (index % 2 == 0) "1" else "0"
            list.add(repayOrderDetail)
            repayOrderDetail = RspRepayOrders.RepayOrderDetail()
        }
        return RspRepayOrders().also {
            it.list = list
        }
    }
}