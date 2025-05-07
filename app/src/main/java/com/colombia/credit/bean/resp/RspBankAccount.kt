package com.colombia.credit.bean.resp

class RspBankAccount {
    val oaeFxoW: ArrayList<BankAccountInfo>? = null

    class BankAccountInfo {
        val RPZ7: String? = null       
        val JJ41sQr: String? = null    //  卡号
        val oH3Jv: Long = 0            // 银行卡id
        val cAPjPKhf9u: String? = null // 持有人OCR名称
        val k3icKyM: String? = null    // 项目编码
        var EQk6U51brP: String = "0"   // (新增) 是否默认选择 0:否 1:是

        fun isSelector() = EQk6U51brP == "1"

        fun changeSelector() {
            if(EQk6U51brP == "0") {
                EQk6U51brP = "1"
            } else {
                EQk6U51brP = "0"
            }
        }
    }
}