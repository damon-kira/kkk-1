package com.kira.ui.language.ini.lexer

enum class IniToken {
    EQUALS,
    COMMENT,
    SECTION,
    KEY,
    VALUE,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}