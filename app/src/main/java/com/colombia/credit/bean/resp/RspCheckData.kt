package com.colombia.credit.bean.resp

class RspCheckData {
    val MqvW: Int = 0 // app list是否是最新数据
    val tHBuj: Int = 0// 联系人是否是最新数据
    val w2WYkZs8: Int = 0// 短信是否是最新数据
    val gZmPw: Int = 0// 是否是最新埋点的数据
    val KJjsu723sd: Int = 0// 设备信息是否是最新数据
    val Kluwbxta7fwe: Int = 0// 相册信息是否是最新数据

    fun isNew(): Boolean {
        return MqvW == 1 && tHBuj == 1 && w2WYkZs8 == 1 && gZmPw == 1 && KJjsu723sd == 1 && Kluwbxta7fwe == 1
    }
}