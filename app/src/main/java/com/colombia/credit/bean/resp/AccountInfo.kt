package com.colombia.credit.bean.resp


interface IBaseInfo

class PersonalInfo : IBaseInfo {

}

class WorkInfo : IBaseInfo {

}

class ContactInfo : IBaseInfo {

}

class BankInfo : IBaseInfo {

}

class KycInfo : IBaseInfo {
    var name: String? = null
    var resuname: String? = null
    var nuip: String? = null
    var birthday: String? = null
    var gender: String? = null
}

class ExtensionInfo : IBaseInfo {

}

class FaceInfo : IBaseInfo {
    var path: String? = null
}