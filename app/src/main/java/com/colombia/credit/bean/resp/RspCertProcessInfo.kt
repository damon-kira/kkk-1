package com.colombia.credit.bean.resp

import com.colombia.credit.expand.*

class RspCertProcessInfo {
    //        "userInfoCredit":"hj15t",//私信认证
//        "workInfoCredit":"M82WVQF",//工作认证
//        "contactInfoCredit":"pfimInrSb",//联系人认证
//        "idCardCredit":"nTVc",//身份证认证
//        "faceInfoCredit":"BzXT",//人脸认证
//        "bankCardCredit":"peFgT"//添加银行卡认证
    val hj15t: Int = 0 // 基本信息
    val M82WVQF: Int = 0 // 工作信息
    val pfimInrSb: Int = 0 //联系人认证
    val nTVc: Int = 0//身份证认证
    val BzXT: Int = 0//人脸认证
    val peFgT: Int = 0//添加银行卡认证

    // return 返回true 未认证
    fun isCertInfo(process: Int) = process == 0

    fun getProcessType(): Int {
        return if (isCertInfo(hj15t)) {
            TYPE_PERSONAL
        } else if (isCertInfo(M82WVQF)) {
            TYPE_WORK
        } else if (isCertInfo(pfimInrSb)) {
            TYPE_CONTACT
        } else if (isCertInfo(peFgT)) {
            TYPE_BANK
        } else if (isCertInfo(nTVc)) {
            TYPE_IDENTITY
        } else if (isCertInfo(BzXT)) {
            TYPE_FACE
        } else TYPE_SUCCESS
    }

    fun isAllSuccess():Boolean {
        return getProcessType() == TYPE_SUCCESS
    }
}