package com.colombia.credit.bean.resp

import com.colombia.credit.expand.transform

class RspHistoryInfo {

    var jNgnZUXNGq: ArrayList<HistoryOrderInfo>? = null

    class HistoryOrderInfo {

        var KxX0GIRzo: String? = null   // 订单id
        var hlDgN: String? = null       // 订单状态
        var pnFU: String? = null        // 待还款金额
            get() = field?.transform()
        var znxlON0: String? = null     // 还款日期
        var H0WVJP: String? = null      // 拒绝日期
        var eeiu2lKWI: String? = null   // 借款金额
//        var tAnV: String? = null        // 待还期数
//        var mjMt2dTqSd: String? = null  // 订单期数
//        var lK97: String? = null        // 剩余天数
        var npGPjAP: String? = null     // 产品名称
//        var PQw5: String? = null        // 产品logo
        var IIIn: String? = null        // 申请日期
//        var YlWUshbDy: String? = null   // 实际还款金额
    }
}