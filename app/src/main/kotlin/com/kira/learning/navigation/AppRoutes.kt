package com.kira.learning.navigation

/**
 * 全局路由集中管理。
 * 后续新增页面请在此添加常量，避免各处硬编码字符串。
 */
object AppRoutes {
    // 底部导航 / 主要模块
    const val CHAT = "chat"
    const val SAMPLE = "sample"
    const val ANSWER = "answer"
    const val PROFILE = "profile"

    // Demo / 功能示例
    const val NAV_DEMO = "nav_demo"
    const val UI_DEMO = "ui_demo"

    // AI & 识别
    const val OCR = "ocr"          // 图片识别
    const val ASSISTANT = "assistant" // AI 助手
}

