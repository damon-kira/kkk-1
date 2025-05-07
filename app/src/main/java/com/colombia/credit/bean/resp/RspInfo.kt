package com.colombia.credit.bean.resp


interface IRspBaseInfo

class RspPersonalInfo : IRspBaseInfo {

    val ovabhwbahsSBHs: PersonalInfo? = null

    class PersonalInfo {
        var BVbhbhaBHDas: String? = null 
        var twavgVGDEWE2HBS: String? = null 
        var mbchaBHDE2DSsj: String? = null
        var oiawasVSV: String? = null 
        var vVGVAgxvsa: String? = null 
        var tavwgVGSVnsdj: String? = null 

        fun isEmpty(): Boolean {
            return vVGVAgxvsa.isNullOrEmpty() && BVbhbhaBHDas.isNullOrEmpty() && this.twavgVGDEWE2HBS.isNullOrEmpty()
                    && mbchaBHDE2DSsj.isNullOrEmpty() && oiawasVSV.isNullOrEmpty() && tavwgVGSVnsdj.isNullOrEmpty()
        }
    }
}


class RspContactInfo : IRspBaseInfo {
    //    var vo9dza3yA: String? = null 

    val Rwfbhdu1: ContactInfo? = null

    class ContactInfo {

        var yYVUx: String? = null 
        var MGwL: String? = null 
        var fTvY4N5: String? = null 

        //    var nJBqrhrxl: String? = null 
        var dZgCz3: String? = null 
        var fWvRFuMb: String? = null 
        var Is8p43A: String = "4" 

        fun isEmpty(): Boolean {
            return yYVUx.isNullOrEmpty() && MGwL.isNullOrEmpty() && fTvY4N5.isNullOrEmpty() && dZgCz3.isNullOrEmpty() && fWvRFuMb.isNullOrEmpty()
        }
    }
}

class RspBankInfo : IRspBaseInfo {

    val hQYeCtjtJh: BankInfo? = null

    class BankInfo {
        var N61kI40HaH: String? = null 
        var `87hVygkzSb`: String? = null 
        var TA2B58tdUU: String? = null 
        var owuNUS9vAj: String? = null 

        fun isEmpty(): Boolean {
            return N61kI40HaH.isNullOrEmpty() && TA2B58tdUU.isNullOrEmpty() && owuNUS9vAj.isNullOrEmpty() && `87hVygkzSb`.isNullOrEmpty()
        }
    }
}

class RspKycInfo : IRspBaseInfo {

    val jmWujylO6j: KycInfo? = null
        get() {
            if (field?.isUpload() == true) return field
            else return null
        }

    class KycInfo {
        var DrD60: String? = null 
        var Wa7f: String? = null 
        var W3YOu8: String? = null 
        var YiWtoa1: String? = null 
        var FStwV6Fge7: String? = null //姓
        var JSusdh7YE: String? = null //名

        var fefFSZ: String? = null // 反面url
            get() = field?.replace(" ", "")
        var YZ7Mlc8yf: String? = null // 正面url
            get() = field?.replace(" ", "")

        fun isUpload(): Boolean {
            return !(fefFSZ.isNullOrEmpty() && YZ7Mlc8yf.isNullOrEmpty() && DrD60 == "-1" && Wa7f.isNullOrEmpty()
                    && FStwV6Fge7.isNullOrEmpty() && YiWtoa1.isNullOrEmpty())
        }
    }
}

class RspFace : IRspBaseInfo