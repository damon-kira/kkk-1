package com.colombia.credit.bean.req


interface IReqBaseInfo

class ReqPersonalInfo : IReqBaseInfo {
    var m8pFeRm: String? = null // email
    var VLQuj: String? = null // 婚姻
    var ECH0: String? = null // 教育
    var nsiCfM: String? = null // 省份
    var JBHSQZXmN: String? = null // 市区
    var bwJaS: String? = null // 详细地址
    var UiSC9wqnS: Long = 0 // 停留时长
    var M6dlmwC: String = "02" // 页面来源
}

class ReqWorkInfo : IReqBaseInfo {
    var Cv10jD: String? = null // 工作时间
    var flb5WwOaa: String? = null // 月收入
    var flb5WwOaaaaa: String? = null // 工资支付频率
    var jQIai: String? = null // 工作类型
    val DxVqsIv = 0L // 停留时长
    val XAhO = "02" // 页面来源
}

class ReqContactInfo : IReqBaseInfo {
//    var vo9dza3yA: String? = null // 联系人id
    var hcPt: String? = null // 第一联系人姓名
    var RHqi: String? = null // 第一联系人电话
    var HCy5Y: String? = null //第一联系人关系

//    var nJBqrhrxl: String? = null // 第二联系人id
    var IKIcWwu6: String? = null // 第二联系人姓名
    var SP2d7: String? = null // 第二联系人电话
    var fOgLF6IBO: String = "4" // 第二联系人关系
    var JifOASaJn = 0L // 停留时长
    var SMJ2sSiDM9 = "02" // 页面来源
}

class ReqBankInfo : IReqBaseInfo {

}

class ReqKycInfo : IReqBaseInfo {
    var name: String? = null
    var resuname: String? = null
    var nuip: String? = null
    var birthday: String? = null
    var gender: String? = null
}

class ReqExtensionInfo : IReqBaseInfo {

}

class ReqFaceInfo : IReqBaseInfo {
    var path: String? = null
}