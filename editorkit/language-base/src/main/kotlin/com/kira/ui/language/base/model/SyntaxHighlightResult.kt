package com.kira.ui.language.base.model

data class SyntaxHighlightResult(
    val tokenType: TokenType,
    var start: Int,
    var end: Int
)