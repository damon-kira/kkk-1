package com.kira.ui.language.css.lexer

enum class CssToken {
    NUMBER,
    DATA_TYPE,
    CLASS,
    REGEX,
    ANNOTATION,
    IMPORTANT,

    PROPERTY,
    VALUE,
    FUNCTION,

    PLUS,
    GT,
    TILDE,
    XOR,
    DOLLAR,
    OR,
    EQ,

    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,
    COLON,
    SEMICOLON,
    COMMA,

    DOUBLE_QUOTED_STRING,
    SINGLE_QUOTED_STRING,

    LINE_COMMENT,
    BLOCK_COMMENT,

    IDENTIFIER,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}