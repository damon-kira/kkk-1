package com.colombia.credit.bean.resp

import com.colombia.credit.expand.transform

class RspRepayOrders {

    var V0qlC: String? = null // 最大限制金额
    var list: ArrayList<RepayOrderDetail>? = null

    class RepayOrderDetail {
        val KER10faeq9: String? = "123" // 订单id
        val bS6qpg4E: String? = "null"   //详情传这个
        val W5KW6: String? = "null"      //计划id
        val RA9GEePdNs: String? = "null" //产品log
        val C2O8E6jjzd: String? = "null" //产品名字
        val Eff0nA: String? = "null"     //待还金额
            get() = field?.transform()
        val zbRV6Lg8jO: String? = "null" //还款日期
        val gzBTFx: Int = 0     //是否逾期
        var QiZorG: String? = "null"     //1代表勾选中
        val q48Wml8N: String? = "null"   //1代表可以展期
        val X32HrYq4u: String? = "null"  //展期金额
            get() = field?.transform()
        val prr9Ie61: String? = "null"    //展期后时间
        val GHMXDjtsUn: String? = "null"  //展期天数
        val rCC18KSG: String? = "null"    //最初应还金额
            get() = field?.transform()

        //新增
        val BPKD: String? = "null" // 产品id
        val JKEAEEnOUZ: String? = "null" // 订单状态
        val LvgWnBEX: String? = "null" //订单状态描述

        fun isCheck() = QiZorG == "1"

        fun isOverdue() = gzBTFx < 0

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
            result = 31 * result + (gzBTFx.hashCode())
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