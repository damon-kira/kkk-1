package com.colombia.credit.bean.req


interface IReqBaseInfo {
    fun isEmpty(): Boolean
}

class ReqPersonalInfo : IReqBaseInfo {
    var kBHCAbhdwbh: String? = null 
    var TEAVvgsvgqs: String? = null 
    var oiwnusx: String? = null 
    var dGCAVvsgw23ds: String? = null 
    var uwahBHDbws: String? = null
    var pwonBHSWASA: String? = null 
    var reasVGGSagyd: Long = 0 
    var teagVGSWGVSs: String = "02" 

    override fun isEmpty(): Boolean {
        return kBHCAbhdwbh.isNullOrEmpty() && oiwnusx.isNullOrEmpty() && TEAVvgsvgqs.isNullOrEmpty()
                && uwahBHDbws.isNullOrEmpty() && pwonBHSWASA.isNullOrEmpty() || dGCAVvsgw23ds.isNullOrEmpty()
    }
}


class ReqContactInfo : IReqBaseInfo {
    //    var vo9dza3yA: String? = null 
    var zAqGvHgHls: String? = null 
    var ifunMf6ZLx: String? = null 
    var gQdRCJKOEJ: String? = null 

    //    var nJBqrhrxl: String? = null 
    var VWHN: String? = null 
    var fHdl: String? = null 
    var PqQz: String = "4" 
    var yB5L8A8mo = 0L 
    var HkdIn = "02" 

    override fun isEmpty(): Boolean {
        return zAqGvHgHls.isNullOrEmpty() && ifunMf6ZLx.isNullOrEmpty() && gQdRCJKOEJ.isNullOrEmpty()
                && VWHN.isNullOrEmpty() && fHdl.isNullOrEmpty()
    }
}

class ReqBankInfo : IReqBaseInfo {
    var thXggvo: String? = null 
    var SElc4: String? = null 
    var GiQ40BKKr: String? = null 
    var Bkmaj97: String? = null 
    var kQWn = 0 
    var cOzMNSKThS = "02" 

    override fun isEmpty(): Boolean {
        return thXggvo.isNullOrEmpty() && SElc4.isNullOrEmpty() && GiQ40BKKr.isNullOrEmpty() && Bkmaj97.isNullOrEmpty()
    }
}

class ReqKycInfo : IReqBaseInfo {
    var W8mqV: String? = null 
    var ALKxGTZ4FQ: String? = null 
    var y6hQBtv: String? = null 
    var GJmhwzsK5: String? = null 

    var Yqm8Lv = 0L 
    var oOxsFrckdv = "02" 

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