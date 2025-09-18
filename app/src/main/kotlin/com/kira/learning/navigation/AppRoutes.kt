package com.kira.learning.navigation

/**
 * 全局路由集中管理。
 * 后续新增页面请在此添加常量，避免各处硬编码字符串。
 */
object AppRoutes {
    // 主要功能路由
    const val MAIN = "main"
    const val CHAT = "chat"
    const val OCR = "ocr"
    const val IMAGE_PLAYER = "image_player"
    const val VIDEO_PLAYER = "video_player"
    const val CODE_EDITOR = "code_editor"
    const val NAV_DEMO = "nav_demo"
    const val UI_DEMO = "ui_demo"
    const val ANSWER = "answer"

    // 用户相关路由
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

    // 其他功能路由
    const val ASSISTANT = "assistant"
    const val DEMO = "demo"
    const val SAMPLE = "sample"

}
