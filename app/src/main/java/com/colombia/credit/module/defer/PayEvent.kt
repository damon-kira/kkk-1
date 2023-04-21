package com.colombia.credit.module.defer

class PayEvent(val event: Int) {

    companion object {
        const val EVENT_REFRESH = 100
        const val EVENT_EXIT = 101
    }
}