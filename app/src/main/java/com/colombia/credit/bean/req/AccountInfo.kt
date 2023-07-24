package com.colombia.credit.bean.req


interface IReqBaseInfo {
    fun isEmpty(): Boolean
}

class ReqPersonalInfo : IReqBaseInfo {
    var kBHCAbhdwbh: String? = null // 工作类型
    var TEAVvgsvgqs: String? = null // 教育
    var oiwnusx: String? = null // 婚姻
    var dGCAVvsgw23ds: String? = null // 月收入
    var uwahBHDbws: String? = null // 省份
    var pwonBHSWASA: String? = null // 市区
    var reasVGGSagyd: Long = 0 // 停留时长
    var teagVGSWGVSs: String = "02" // 页面来源

    override fun isEmpty(): Boolean {
        return kBHCAbhdwbh.isNullOrEmpty() && oiwnusx.isNullOrEmpty() && TEAVvgsvgqs.isNullOrEmpty()
                && uwahBHDbws.isNullOrEmpty() && pwonBHSWASA.isNullOrEmpty() || dGCAVvsgw23ds.isNullOrEmpty()
    }
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

    override fun isEmpty(): Boolean {
        return zAqGvHgHls.isNullOrEmpty() && ifunMf6ZLx.isNullOrEmpty() && gQdRCJKOEJ.isNullOrEmpty()
                && VWHN.isNullOrEmpty() && fHdl.isNullOrEmpty()
    }
}

class ReqBankInfo : IReqBaseInfo {
    var thXggvo: String? = null // 银行名称
    var SElc4: String? = null // 银行类型
    var GiQ40BKKr: String? = null // 银行编码
    var Bkmaj97: String? = null // 银行卡号
    var kQWn = 0 // 停留时长
    var cOzMNSKThS = "02" // 页面来源

    override fun isEmpty(): Boolean {
        return thXggvo.isNullOrEmpty() && SElc4.isNullOrEmpty() && GiQ40BKKr.isNullOrEmpty() && Bkmaj97.isNullOrEmpty()
    }
}

class ReqKycInfo : IReqBaseInfo {
    var W8mqV: String? = null // 性别
    var ALKxGTZ4FQ: String? = null // nuip
    var y6hQBtv: String? = null // 姓名
    var GJmhwzsK5: String? = null // 生日

    var Yqm8Lv = 0L // 停留时长
    var oOxsFrckdv = "02" // 页面来源

    override fun isEmpty(): Boolean {
        return (W8mqV.isNullOrEmpty() && ALKxGTZ4FQ.isNullOrEmpty() && y6hQBtv.isNullOrEmpty() && GJmhwzsK5.isNullOrEmpty())
    }

    override fun toString(): String {
        return "ReqKycInfo(W8mqV=$W8mqV, ALKxGTZ4FQ=$ALKxGTZ4FQ, y6hQBtv=$y6hQBtv, GJmhwzsK5=$GJmhwzsK5, Yqm8Lv=$Yqm8Lv, oOxsFrckdv='$oOxsFrckdv')"
    }
}

// 展期
class ReqExtensionInfo : IReqBaseInfo {
    override fun isEmpty(): Boolean {
        return false
    }
}

// 活体
class ReqFaceInfo : IReqBaseInfo {
    var path: String? = null
    override fun isEmpty(): Boolean {
        return false
    }
}