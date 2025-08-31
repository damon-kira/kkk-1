package com.kira.learning.xml.modules.firstconfirm

class ConfirmEvent(val event: Int,val value: String? = null) {

    companion object {
        const val EVENT_BANK_NO = 10 // 回传数据
    }
}