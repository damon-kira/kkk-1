package com.kira.learning.module.home

class MainEvent(val event: String) {
    companion object {
        const val EVENT_SHOW_HOME = "show_home" // 显示首页
        const val EVENT_SHOW_REPAY = "show_repay" // 显示仪表页面
    }
}