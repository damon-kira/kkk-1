package com.colombia.credit.bean.resp


interface IRspBaseInfo

class RspPersonalInfo : IRspBaseInfo {

    val ovabhwbahsSBHs: PersonalInfo? = null

    class PersonalInfo {
        var BVbhbhaBHDas: String? = null // 教育
        var twavgVGDEWE2HBS: String? = null // 婚姻
        var mbchaBHDE2DSsj: String? = null // 省份
        var oiawasVSV: String? = null // 市区
        var vVGVAgxvsa: String? = null // 月收入
        var tavwgVGSVnsdj: String? = null // 工作类型

        fun isEmpty(): Boolean {
            return vVGVAgxvsa.isNullOrEmpty() && BVbhbhaBHDas.isNullOrEmpty() && this.twavgVGDEWE2HBS.isNullOrEmpty()
                    && mbchaBHDE2DSsj.isNullOrEmpty() && oiawasVSV.isNullOrEmpty() && tavwgVGSVnsdj.isNullOrEmpty()
        }
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

        fun isEmpty(): Boolean {
            return yYVUx.isNullOrEmpty() && MGwL.isNullOrEmpty() && fTvY4N5.isNullOrEmpty() && dZgCz3.isNullOrEmpty() && fWvRFuMb.isNullOrEmpty()
        }
    }
}

class RspBankInfo : IRspBaseInfo {

    val hQYeCtjtJh: BankInfo? = null

    class BankInfo {
        var N61kI40HaH: String? = null // 银行名称
        var `87hVygkzSb`: String? = null // 银行类型
        var TA2B58tdUU: String? = null // 银行编码
        var owuNUS9vAj: String? = null // 银行卡号

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
            return !(fefFSZ.isNullOrEmpty() && YZ7Mlc8yf.isNullOrEmpty() && DrD60 == "-1" && Wa7f.isNullOrEmpty()
                    && FStwV6Fge7.isNullOrEmpty() && YiWtoa1.isNullOrEmpty())
        }
    }
}

class RspFace : IRspBaseInfo