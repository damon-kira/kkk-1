package com.colombia.credit.module.repay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspRepayOrders
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.LiveDataCall
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

class RepayTabRepository @Inject constructor(): BaseRepository() {

    fun getRepayOrders() = ApiServiceLiveDataProxy.request(RspRepayOrders::class.java) {
        apiService.getRepayOrders()
    }
//    fun getRepayOrders(): LiveData<BaseResponse<RspRepayOrders>> {
//        // 硬编码的成功响应数据
//        val mockResponse = BaseResponse<RspRepayOrders>().apply {
//            code = ResponseCode.SUCCESS_CODE
//            data = RspRepayOrders().apply {
//                V0qlC = "50000.00" // 最大限制金额
//                list = arrayListOf(
//                    RspRepayOrders.RepayOrderDetail().apply {
//                    },
//                    RspRepayOrders.RepayOrderDetail().apply {
//                    }
//                )
//            }
//        }
//
//        // 使用MutableLiveData返回固定数据
//        return MutableLiveData<BaseResponse<RspRepayOrders>>().apply {
//            value = mockResponse
//        }
//    }
}