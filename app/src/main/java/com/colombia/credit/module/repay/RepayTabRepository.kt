package com.colombia.credit.module.repay

import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspRepayOrders
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class RepayTabRepository @Inject constructor(): BaseRepository() {

    fun getRepayOrders() = ApiServiceLiveDataProxy.request(RspRepayOrders::class.java) {
        apiService.getRepayOrders()
    }
}