

package com.kira.ui.language.latex.lexer

enum class LatexToken {
    RESERVED_WORD,
    FUNCTION,
    NUMBER,
    LENGTH,

    BEGIN_BLOCK,
    END_BLOCK,

    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,
    SEPARATOR,

    LINE_COMMENT,

    IDENTIFIER,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}