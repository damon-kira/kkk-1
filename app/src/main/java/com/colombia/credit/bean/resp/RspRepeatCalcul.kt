package com.colombia.credit.bean.resp

class RspRepeatCalcul {

    var BM3HTNDY1b: String? = null // 到账总金额
    var bwAK6N3EuE: String? = null //    借款总金额
    var KcGqvf: String? = null //    到账银行卡
    var FXE6B: String? = null //    总利息
    var jcSrg9: String? = null //    还款时间
    var SNgzk66y: String? = null //    预计到账时间
    var vQDdL: ArrayList<CalculDetail>? = null //    产品列表


    class CalculDetail {
        var kDyJFWE: String? = null //    产品金额
        var Bwh8vVa5wn: String? = null //    产品Code
        var ekrpWqU0: String? = null //    产品id
        var rOVhdGR: String? = null //    产品logo地址
        var um7clL0I: String? = null //    产品名
        var lceAYgef: String? = null //    产品利息
        var UvS8UJEFy9: String? = null //    到账金额

        var isCheck: Int = 0 // 0:未选 1:选择

        fun change() {
            isCheck = if (isCheck == 0) 1 else 0
        }
    }
}