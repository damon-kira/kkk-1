package com.colombia.credit.bean.resp


interface IRspBaseInfo

class RspPersonalInfo : IRspBaseInfo {

    var OloW: String? = null // email
    var rtA8s2HSB: String? = null // 教育
    var wXlWnOHPzK: String? = null // 婚姻
    var tKgYzqB7yP: String? = null // 省份
    var ZzBVPho: String? = null // 市区
    var fomX9KPzpi: String? = null // 详细地址

    var Vi4Jtj: String = "0" // 是否允许编辑 1：允许 1：不允许
}

class RspWorkInfo : IRspBaseInfo {
    var iBwnjiNbTX: String? = null // 工作时间
    var P2i72V: String? = null // 月收入
    var RbNJgGj: String? = null // 工资支付频率
    var V33vxNjkQf: String? = null // 工作类型
}

class RspContactInfo : IRspBaseInfo {
    //    var vo9dza3yA: String? = null // 联系人id
    var yYVUx: String? = null //第一联系人关系
    var MGwL: String? = null // 第一联系人姓名
    var fTvY4N5: String? = null // 第一联系人电话

    //    var nJBqrhrxl: String? = null // 第二联系人id
    var dZgCz3: String? = null // 第二联系人姓名
    var fWvRFuMb: String? = null // 第二联系人电话
    var Is8p43A: String = "4" // 第二联系人关系
}

class RspBankInfo : IRspBaseInfo {
    var thXggvo: String? = null // 银行名称
    var SElc4: String? = null // 银行类型
    var GiQ40BKKr: String? = null // 银行编码
    var Bkmaj97: String? = null // 银行卡号
}

class RspKycInfo : IRspBaseInfo {
    var DrD60: String? = null // 性别
    var Wa7f: String? = null // nuip
    var W3YOu8: String? = null // 姓名
    var YiWtoa1: String? = null // 生日
    var FStwV6Fge7:String? = null //姓
    var JSusdh7YE:String? = null //名

    var fefFSZ: String? = null // 反面url
    var YZ7Mlc8yf: String? = null // 正面url
}

class RspFace: IRspBaseInfo

// 展期
class RspExtensionInfo : IRspBaseInfo {

}
