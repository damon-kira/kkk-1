

package com.kira.ui.language.visualbasic.lexer

enum class VisualBasicToken {
    LONG_LITERAL,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    DOUBLE_LITERAL,

    KEYWORD,

    BOOLEAN,
    BYTE,
    CHAR,
    DATE,
    DECIMAL,
    DOUBLE,
    INTEGER,
    LONG,
    OBJECT,
    SBYTE,
    SHORT,
    SINGLE,
    STRING,
    UINTEGER,
    ULONG,
    USHORT,

    TRUE,
    FALSE,

    AND,
    ANDEQ,
    MULT,
    MULTEQ,
    PLUS,
    PLUSEQ,
    EQ,
    MINUS,
    MINUSEQ,
    LT,
    LTLT,
    LTLTEQ,
    GT,
    GTGT,
    GTGTEQ,
    DIV,
    DIVEQ,
    BACKSLASH,
    XOR,
    XOREQ,

    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,
    SEMICOLON,
    COMMA,
    DOT,

    DOUBLE_QUOTED_STRING,

    LINE_COMMENT,

    IDENTIFIER,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}