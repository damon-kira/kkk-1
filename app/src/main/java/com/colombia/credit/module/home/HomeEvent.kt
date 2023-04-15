package com.colombia.credit.module.home

class HomeEvent(val event: Int) {
    companion object {
        const val EVENT_REFRESH = 10
        const val EVENT_LOGIN = 11
        const val EVENT_LOGOUT = 12
    }
}