

package com.kira.ui.language.toml.lexer

enum class TomlToken {
    NUMBER,
    DATE_TIME,
    BOOLEAN,
    COMMENT,
    KEY,

    BASIC_STRING,
    LITERAL_STRING,
    MULTILINE_BASIC_STRING,
    MULTILINE_LITERAL_STRING,

    EQ,
    COMMA,
    DOT,
    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,

    IDENTIFIER,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}