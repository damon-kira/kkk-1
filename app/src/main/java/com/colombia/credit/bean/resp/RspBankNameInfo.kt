package com.colombia.credit.bean.resp

import com.colombia.credit.bean.SearchInfo
import java.util.*
import kotlin.collections.ArrayList

class RspBankNameInfo {

    val list: ArrayList<BankNameInfo>? = null

    class BankNameInfo: SearchInfo() {
        val Vu3XbBF: String? = null // 银行编码
        val KoGUgumBVm: String? = null  // 银行名称
        val xevtra: String? = null  // 是否常用
        override fun match(constraint: CharSequence?): Boolean {
            var result = false
            KoGUgumBVm?.let { nameStr ->
                constraint?.let { it ->
                    result =
                        nameStr.toLowerCase(Locale.US).startsWith(it.toString().toLowerCase(Locale.US))
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
    }
}