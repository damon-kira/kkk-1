package com.colombia.credit.bean.resp

import com.colombia.credit.expand.transform

class RspRepayOrders {

    var V0qlC: String? = null // 最大限制金额
    var list: ArrayList<RepayOrderDetail>? = null

    class RepayOrderDetail {
        var bS6qpg4E: String? = null   //详情传这个
        var W5KW6: String? = null      //计划id
        var RA9GEePdNs: String? = null //产品log
        var C2O8E6jjzd: String? = null //产品名字
        var Eff0nA: String? = null            //待还金额
            get() = field?.transform()
        var zbRV6Lg8jO: String? = null //还款日期
        var gzBTFx: String? = null     //是否逾期
        var QiZorG: String? = null     //1代表勾选中
        var q48Wml8N: String? = null   //1代表可以展期
        var X32HrYq4u: String? = null  //展期金额
            get() = field?.transform()
        var prr9Ie61: String? = null    //展期后时间
        var GHMXDjtsUn: String? = null  //展期天数
        var rCC18KSG: String? = null    //最初应还金额
            get() = field?.transform()

        //新增
        var BPKD: String? = null // 产品id
        var JKEAEEnOUZ: String? = null // 订单状态
        var LvgWnBEX: String? = null //订单状态描述

        fun isCheck() = QiZorG == "1"

        fun isOverdue() = gzBTFx == "1"

        fun changeSelect() {
            QiZorG = if (isCheck()) "0" else "1"
        }

        override fun hashCode(): Int {
            var result = bS6qpg4E?.hashCode() ?: 0
            result = 31 * result + (W5KW6?.hashCode() ?: 0)
            result = 31 * result + (RA9GEePdNs?.hashCode() ?: 0)
            result = 31 * result + (C2O8E6jjzd?.hashCode() ?: 0)
            result = 31 * result + (Eff0nA?.hashCode() ?: 0)
            result = 31 * result + (zbRV6Lg8jO?.hashCode() ?: 0)
            result = 31 * result + (gzBTFx?.hashCode() ?: 0)
            result = 31 * result + (QiZorG?.hashCode() ?: 0)
            result = 31 * result + (q48Wml8N.hashCode())
            result = 31 * result + (X32HrYq4u?.hashCode() ?: 0)
            result = 31 * result + (prr9Ie61?.hashCode() ?: 0)
            result = 31 * result + (GHMXDjtsUn?.hashCode() ?: 0)
            result = 31 * result + (rCC18KSG?.hashCode() ?: 0)
            result = 31 * result + (BPKD?.hashCode() ?: 0)
            result = 31 * result + (JKEAEEnOUZ?.hashCode() ?: 0)
            result = 31 * result + (LvgWnBEX?.hashCode() ?: 0)
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (javaClass != other?.javaClass) return false

            other as RepayOrderDetail
            if (W5KW6 != other.W5KW6) return false
            if (RA9GEePdNs != other.RA9GEePdNs) return false
            if (C2O8E6jjzd != other.C2O8E6jjzd) return false
            if (Eff0nA != other.Eff0nA) return false
            if (zbRV6Lg8jO != other.zbRV6Lg8jO) return false
            if (gzBTFx != other.gzBTFx) return false
//            if (QiZorG != other.QiZorG) return false
            if (q48Wml8N != other.q48Wml8N) return false
            if (X32HrYq4u != other.X32HrYq4u) return false
            if (prr9Ie61 != other.prr9Ie61) return false
            if (GHMXDjtsUn != other.GHMXDjtsUn) return false
            if (rCC18KSG != other.rCC18KSG) return false
            if (BPKD != other.BPKD) return false
            if (JKEAEEnOUZ != other.JKEAEEnOUZ) return false
            if (LvgWnBEX != other.LvgWnBEX) return false
            return true
        }
    }
}