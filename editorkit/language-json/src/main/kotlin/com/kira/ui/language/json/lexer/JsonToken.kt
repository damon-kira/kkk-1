

package com.kira.ui.language.json.lexer

enum class JsonToken {
    NUMBER,

    TRUE,
    FALSE,
    NULL,

    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,
    COMMA,
    COLON,

    DOUBLE_QUOTED_STRING,
    SINGLE_QUOTED_STRING,
    LINE_COMMENT,
    BLOCK_COMMENT,

    IDENTIFIER,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}