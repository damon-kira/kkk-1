package com.colombia.credit.bean.req


interface IReqBaseInfo

class ReqPersonalInfo : IReqBaseInfo {
    var m8pFeRm: String? = null // email
    var VLQuj: String? = null // 婚姻
    var ECH0: String? = null // 教育
    var nsiCfM: String? = null // 省份
    var JBHSQZXmN: String? = null // 市区
    var bwJaS: String? = null // 详细地址
//    var UiSC9wqnS: Long = 0 // 停留时长
//    var M6dlmwC: String? = null // 页面来源
}

class ReqWorkInfo : IReqBaseInfo {

}

class ReqContactInfo : IReqBaseInfo {

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