package com.colombia.credit.module.repay

import com.colombia.credit.bean.resp.RspRepayOrders
import com.colombia.credit.bean.resp.RspRepayOrders.RepayOrderDetail
import com.common.lib.base.BaseViewModel
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class RepayTabViewModel @Inject constructor(private val repository: RepayTabRepository) :
    BaseViewModel() {

    private val _ordersLivedata = generatorLiveData<BaseResponse<RspRepayOrders>>()
    val ordersLivedata = _ordersLivedata

    val listLivedata = generatorLiveData<ArrayList<RspRepayOrders.RepayOrderDetail>?>()

    fun getRepayOrders() {
        _ordersLivedata.addSourceLiveData(repository.getRepayOrders()) {
//            if (it.isSuccess()) {
//                val list = it.getData()?.list
//                if (list != listLivedata.value) {
//                    listLivedata.postValue(list)
//                }
//            }
            var list: ArrayList<RepayOrderDetail>? = arrayListOf(RepayOrderDetail(),RepayOrderDetail(),RepayOrderDetail(),)
            listLivedata.postValue(list)
            _ordersLivedata.postValue(it)
        }
    }

    fun clearData(){
        listLivedata.value = null
        _ordersLivedata.value = BaseResponse(ResponseCode.OTHER_ERROR_CODE, RspRepayOrders(), null)
    }

}
