

package com.kira.ui.language.yaml.lexer

enum class YamlToken {
    SEQUENCE_MARKER,
    DOCUMENT_MARKER,
    DOCUMENT_END,

    COMMA,
    COLON,
    AMPERSAND,
    STAR,
    QUESTION,

    LBRACKET,
    RBRACKET,
    LBRACE,
    RBRACE,

    SCALAR_KEY,
    SCALAR_TEXT,
    SCALAR_LIST,
    SCALAR_STRING,
    SCALAR_DSTRING,
    SCALAR_EOL,

    COMMENT,
    TEXT,
    TAG,
    ANCHOR,
    ALIAS,
    INDENT,
    WHITESPACE,
    EOL,
    EOF,
}