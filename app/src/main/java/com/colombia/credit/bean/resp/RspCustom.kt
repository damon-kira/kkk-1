package com.colombia.credit.bean.resp

import kotlin.random.Random

class RspCustom {
    var sQI9ElZ: String? = null // app配置id
    var an3XQN: String? = null // 项目编码
    var GCdnsj: String? = null // 邮箱
    var QwtC9lYGT7: String? = null // 邮箱
    var ZJ1b5p3nr: ArrayList<String>? = null // 客服电话

    fun getServiceTel(): String {
        val random = Random(10)
        val fdfdfd = ZJ1b5p3nr
        val size = fdfdfd?.size ?: 0
        if (size == 0) {
            return ""
        }
        val randomNum = random.nextInt(size)
        return fdfdfd!![randomNum]
    }


}