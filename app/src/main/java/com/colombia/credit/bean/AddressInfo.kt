package com.colombia.credit.bean

import java.util.*
import kotlin.collections.ArrayList

class AddressInfo : SearchInfo() {
    var fluia: String? = null // 編號
    var cingorium: String? = null // 名称
    var sonList: ArrayList<City>? = null
    var isCheck = false

    class City : SearchInfo() {
        var cingorium: String? = null // 省名子
        var fluia: String? = null // 省编号
        var caldably: String? = null // 市编号
        var trophful: String? = null //市名字
        var isCheck: Boolean = false
        override fun match(constraint: CharSequence?): Boolean {
            var result = false
            trophful?.let { nameStr ->
                constraint?.let { it ->
                    result =
                        nameStr.toLowerCase(Locale.US)
                            .startsWith(it.toString().toLowerCase(Locale.US))
                }
            }
            return result
        }

        override fun getTag(): String {
            return trophful?.substring(0, 1).orEmpty()
        }

        override fun isHot(): Boolean {
            return false
        }
    }

    override fun match(constraint: CharSequence?): Boolean {
        var result = false
        cingorium?.let { nameStr ->
            constraint?.let { it ->
                result =
                    nameStr.toLowerCase(Locale.US).startsWith(it.toString().toLowerCase(Locale.US))
            }
        }
        return result
    }

    override fun getTag(): String {
        return fluia?.substring(0, 1).orEmpty()
    }

    override fun isHot(): Boolean {
        return false
    }
}