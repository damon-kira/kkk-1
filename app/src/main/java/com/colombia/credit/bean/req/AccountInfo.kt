package com.colombia.credit.bean.req


interface IReqBaseInfo

class ReqPersonalInfo : IReqBaseInfo {
    var unH4I2vHXG: String? = null // email
    var m7pyaSk: String? = null // 婚姻
    var zgGtVHl9N2: String? = null // 教育
    var QlCvCLnNx: String? = null // 省份
    var woTVOe: String? = null // 市区
    var lh3bJ: String? = null // 详细地址
    var qoTp6AOTAZ: Long = 0 // 停留时长
    var XVQOOWFrF: String = "02" // 页面来源
}

class ReqWorkInfo : IReqBaseInfo {
    var x6yR: String? = null // 工作时间
    var xgJ5: String? = null // 月收入
    var u0pn: String? = null // 工资支付频率
    var AD8Jznx: String? = null // 工作类型
    val HeWTdLp3 = 0L // 停留时长
    val GdGN6viJS = "02" // 页面来源
}

class ReqContactInfo : IReqBaseInfo {
    //    var vo9dza3yA: String? = null // 联系人id
    var zAqGvHgHls: String? = null // 第一联系人姓名
    var ifunMf6ZLx: String? = null // 第一联系人电话
    var gQdRCJKOEJ: String? = null //第一联系人关系

    //    var nJBqrhrxl: String? = null // 第二联系人id
    var VWHN: String? = null // 第二联系人姓名
    var fHdl: String? = null // 第二联系人电话
    var PqQz: String = "4" // 第二联系人关系
    var yB5L8A8mo = 0L // 停留时长
    var HkdIn = "02" // 页面来源
}

class ReqBankInfo : IReqBaseInfo {
    var thXggvo: String? = null // 银行名称
    var SElc4: String? = null // 银行类型
    var GiQ40BKKr: String? = null // 银行编码
    var Bkmaj97: String? = null // 银行卡号
    var kQWn = 0 // 停留时长
    var cOzMNSKThS = "02" // 页面来源
}

class ReqKycInfo : IReqBaseInfo {
    var W8mqV: String? = null // 性别
    var ALKxGTZ4FQ: String? = null // nuip
    var y6hQBtv: String? = null // 姓名
    var GJmhwzsK5: String? = null // 生日

    var Yqm8Lv = 0L // 停留时长
    var oOxsFrckdv = "02" // 页面来源
}

// 展期
class ReqExtensionInfo : IReqBaseInfo {

}

// 活体
class ReqFaceInfo : IReqBaseInfo {
    var path: String? = null
}