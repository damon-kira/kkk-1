package com.colombia.credit.bean.resp

import com.colombia.credit.expand.transform

class RspRepayDetail {

    val productName: String? = null
    val Ab85: String? = null // 产品URL
    val TxksJTU8C: String? = null
        //总金额
        get() = field?.transform()
    val oTDoZR: String? = null//总期数
    val Rskv6D: String? = null//是否优惠
    val qN38x: String? = null//优惠比例 0%
    val rGCC180IN: String? = null //优惠金额
    val VBRUBZO: String? = null//总金额

    var KUgC: ArrayList<RepayDetail>? = null

    class RepayDetail {
        val KER10faeq9: String? = null // 订单id
        val PJpH0: String? = null//生成还款码传这个id
        val RKGqbRy: String? = null // 产品id
        val qh1yC1M: String? = null
        val cx2Te: String? = null //总期数
        val vR7HkDeZP: String? = null //当前期数
        val CI6jcXNI: String? = null
        val PmuEMR5ESD: String? = null //RPS
        val VJJxo2: String? = null// 2018000//还款金额
        val RPKfna: String? = null //760000
        val uALonXNeY: String? = null //0
        val ch4x: String? = null //11-02-2023//还款时间
        val rm7fqlDid: String? = null //- 2//剩余时间
        val vzcAyk: String? = null //0
        val DlYbHlY: String? = null //1 是否可以展期 1可以
        val zlftJgf: String? = null //展期天数
        val EA7nMOa: String? = null //展期金额
            get() = field?.transform()
        val vlSH: String? = null //1
        val DaNhMLH: String? = null //放款银行卡
        val cHum8: String? = null //罚息
        val YXtMfL6nAm: String? = null //22-02-2023//展期后应还时间
        val pHSUCa43: String? = null
            // 990000//到手金额
            get() = field?.transform()
        val Dmj7UQm: String? = null //830000//利息
        val dbhmAOVp56: String? = null
            // 830000 // 最初应还金额
            get() = field?.transform()

        //新增字段
        val danMcSTojF: String? = null //订单状态
        val wIWdNgC: String? = null// 订单状态描述
    }
}