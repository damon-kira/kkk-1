

package com.kira.ui.language.latex.lexer

data class TokenData(
    val start: Int,
    val end: Int,
    val token: LatexToken,
)