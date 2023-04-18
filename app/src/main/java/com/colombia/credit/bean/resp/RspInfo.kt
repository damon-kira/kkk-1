package com.colombia.credit.bean.resp


interface IRspBaseInfo

class RspPersonalInfo : IRspBaseInfo {

    val MFL57Df: PersonalInfo? = null

    class PersonalInfo {
        var OloW: String? = null // email
        var rtA8s2HSB: String? = null // 教育
        var wXlWnOHPzK: String? = null // 婚姻
        var tKgYzqB7yP: String? = null // 省份
        var ZzBVPho: String? = null // 市区
        var fomX9KPzpi: String? = null // 详细地址
    }

    var Vi4Jtj: String = "0" // 是否允许编辑 1：允许 1：不允许
}

class RspWorkInfo : IRspBaseInfo {

    val dbxhWe4XWA: WorkInfo? = null

    class WorkInfo {
        var iBwnjiNbTX: String? = null // 工作时间
        var P2i72V: String? = null // 月收入
        var RbNJgGj: String? = null // 工资支付频率
        var V33vxNjkQf: String? = null // 工作类型
    }
}

class RspContactInfo : IRspBaseInfo {
    //    var vo9dza3yA: String? = null // 联系人id

    val Rwfbhdu1: ContactInfo? = null

    class ContactInfo {

        var yYVUx: String? = null //第一联系人关系
        var MGwL: String? = null // 第一联系人姓名
        var fTvY4N5: String? = null // 第一联系人电话

        //    var nJBqrhrxl: String? = null // 第二联系人id
        var dZgCz3: String? = null // 第二联系人姓名
        var fWvRFuMb: String? = null // 第二联系人电话
        var Is8p43A: String = "4" // 第二联系人关系
    }
}

class RspBankInfo : IRspBaseInfo {

    val hQYeCtjtJh: BankInfo? = null

    class BankInfo {
        var N61kI40HaH: String? = null // 银行名称
        var `87hVygkzSb`: String? = null // 银行类型
        var TA2B58tdUU: String? = null // 银行编码
        var owuNUS9vAj: String? = null // 银行卡号
    }
}

class RspKycInfo : IRspBaseInfo {

    val jmWujylO6j: KycInfo? = null
    get() {
        if (field?.isUpload() == true) return field
        else return null
    }

    class KycInfo {
        var DrD60: String? = null // 性别
        var Wa7f: String? = null // nuip
        var W3YOu8: String? = null // 姓名
        var YiWtoa1: String? = null // 生日
        var FStwV6Fge7: String? = null //姓
        var JSusdh7YE: String? = null //名

        var fefFSZ: String? = null // 反面url
            get() = field?.replace(" ", "")
        var YZ7Mlc8yf: String? = null // 正面url
            get() = field?.replace(" ", "")

        fun isUpload(): Boolean {
            return !(fefFSZ.isNullOrEmpty() || YZ7Mlc8yf.isNullOrEmpty() || DrD60 == "-1" || Wa7f.isNullOrEmpty() || FStwV6Fge7.isNullOrEmpty() || YiWtoa1.isNullOrEmpty())
        }
    }
}

class RspFace : IRspBaseInfo

// 展期
class RspExtensionInfo : IRspBaseInfo {

}
