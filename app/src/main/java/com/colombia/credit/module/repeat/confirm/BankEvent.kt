package com.colombia.credit.module.repeat.confirm

class BankEvent(var evnet: Int, var params: String? = null) {
    companion object {
        const val EVENT_BANK = 0x10
    }
}