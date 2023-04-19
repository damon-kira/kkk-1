package com.colombia.credit.bean.resp

import android.os.Parcel
import android.os.Parcelable
import com.colombia.credit.expand.transform
import java.math.BigDecimal

class RspRepayOrders {

    var V0qlC: String? = null // 最大限制金额
    var list: ArrayList<RepayOrderDetail>? = null

    class RepayOrderDetail() : Parcelable {
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

        constructor(parcel: Parcel) : this() {
            bS6qpg4E = parcel.readString()
            W5KW6 = parcel.readString()
            RA9GEePdNs = parcel.readString()
            C2O8E6jjzd = parcel.readString()
            Eff0nA = parcel.readString()
            zbRV6Lg8jO = parcel.readString()
            gzBTFx = parcel.readString()
            QiZorG = parcel.readString()
            q48Wml8N = parcel.readString()
            X32HrYq4u = parcel.readString()
            prr9Ie61 = parcel.readString()
            GHMXDjtsUn = parcel.readString()
            rCC18KSG = parcel.readString()
            BPKD = parcel.readString()
            JKEAEEnOUZ = parcel.readString()
            LvgWnBEX = parcel.readString()
        }

        fun isCheck() = QiZorG == "1"

        fun isOverdue() = gzBTFx == "1"

        fun changeSelect() {
            QiZorG = if (isCheck()) "0" else "1"
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(bS6qpg4E)
            parcel.writeString(W5KW6)
            parcel.writeString(RA9GEePdNs)
            parcel.writeString(C2O8E6jjzd)
            parcel.writeString(Eff0nA)
            parcel.writeString(zbRV6Lg8jO)
            parcel.writeString(gzBTFx)
            parcel.writeString(QiZorG)
            parcel.writeString(q48Wml8N)
            parcel.writeString(X32HrYq4u)
            parcel.writeString(prr9Ie61)
            parcel.writeString(GHMXDjtsUn)
            parcel.writeString(rCC18KSG)
            parcel.writeString(BPKD)
            parcel.writeString(JKEAEEnOUZ)
            parcel.writeString(LvgWnBEX)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<RepayOrderDetail> {
            override fun createFromParcel(parcel: Parcel): RepayOrderDetail {
                return RepayOrderDetail(parcel)
            }

            override fun newArray(size: Int): Array<RepayOrderDetail?> {
                return arrayOfNulls(size)
            }
        }
    }
}