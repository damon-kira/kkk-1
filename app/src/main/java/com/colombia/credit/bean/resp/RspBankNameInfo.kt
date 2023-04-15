package com.colombia.credit.bean.resp

import com.colombia.credit.bean.SearchInfo
import java.util.*
import kotlin.collections.ArrayList

class RspBankNameInfo {

    val nefV2g8cf0: ArrayList<BankNameInfo>? = null

//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as RspBankNameInfo
//        return nefV2g8cf0 === other.nefV2g8cf0
//    }

    class BankNameInfo : SearchInfo() {
        val Vu3XbBF: String? = null // 银行编码
        val KoGUgumBVm: String? = null  // 银行名称
        val xevtra: String? = null  // 是否常用
        override fun match(constraint: CharSequence?): Boolean {
            var result = false
            KoGUgumBVm?.let { nameStr ->
                constraint?.let { it ->
                    result =
                        nameStr.toLowerCase(Locale.US)
                            .startsWith(it.toString().toLowerCase(Locale.US))
                }
            }
            return result
        }

        override fun getTag(): String {
            val name = KoGUgumBVm
            if (name.isNullOrEmpty()) return ""
            return name.substring(0, 1).uppercase()
        }

        override fun isHot(): Boolean = xevtra == "1"

//        override fun hashCode(): Int {
//            var result = Vu3XbBF?.hashCode() ?: 0
//             result += KoGUgumBVm?.hashCode() ?: 0
//             result += xevtra?.hashCode() ?: 0
//            return result
//        }

//        override fun equals(other: Any?): Boolean {
//            if (other === this) return true
//            if (javaClass != other?.javaClass) return false
//
//            other as BankNameInfo
//
//            if (Vu3XbBF != other.Vu3XbBF) return false
//            if (KoGUgumBVm != other.KoGUgumBVm) return false
//            if (xevtra != other.xevtra) return false
//            return true
//        }
    }
}