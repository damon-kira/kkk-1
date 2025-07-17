

package com.kira.ui.language.markdown.lexer

enum class MarkdownToken {
    HEADER,

    UNORDERED_LIST_ITEM,
    ORDERED_LIST_ITEM,

    BOLDITALIC1,
    BOLDITALIC2,
    BOLD1,
    BOLD2,
    ITALIC1,
    ITALIC2,
    STRIKETHROUGH,

    CODE,
    CODE_BLOCK,

    LT,
    GT,
    EQ,
    NOT,
    DIV,
    MINUS,

    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,

    URL,
    IDENTIFIER,
    WHITESPACE,
    BAD_CHARACTER,
    EOF
}