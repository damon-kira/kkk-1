package com.bigdata.lib.bean

class SmsInfo {
    var tel: String? = null
    var body: String? = null
    var time: Long = 0
    var read: String? = null // 0未读 1已读
    var status: String? = null//-1 接收，0 complete,64 pending128 failed
    var type: String? = null // 1接受 2发出

}