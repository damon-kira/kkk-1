package com.colombia.credit.bean.resp

import com.colombia.credit.bean.SearchInfo
import java.util.*

class BankInfoSearch : SearchInfo() {
    var BName: String? = null
    val BCode: String = ""
    var isCommonlyUsed: Int = 0 // 常用是1，默认是0

    fun getBankName() = BName

    fun getBankCode() = BCode

    override fun match(constraint: CharSequence?): Boolean {
        var result = false
        BName?.let { nameStr ->
            constraint?.let { it ->
                result =
                    nameStr.toLowerCase(Locale.US).startsWith(it.toString().toLowerCase(Locale.US))
            }
        }
        return result
    }

    override fun getTag(): String {
        val name = BName
        if (name.isNullOrEmpty()) return ""
        return name.substring(0, 1).uppercase()
    }

    override fun isHot(): Boolean = isCommonlyUsed == 1

}