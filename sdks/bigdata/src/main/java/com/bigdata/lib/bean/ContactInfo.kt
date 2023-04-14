package com.bigdata.lib.bean

class ContactInfo {
    var firstName: String? = null
    var lastName: String? = null
    var name:String? = null // 名字
    var tel:String? = null // 电话
    var lastUpdateTime:String? = null // 最后更新时间
    var address:String? = null // 归属地
    var timesContacted:Long = 0 // 联系时间
    var lastTimeContacted: Long = 0 // 最后联系时间
    var is_sim:Int = 0 // 保存位置 1：sim 0:首届
    var group: String? = null //分组

}