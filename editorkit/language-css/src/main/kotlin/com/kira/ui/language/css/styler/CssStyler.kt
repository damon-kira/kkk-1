package com.kira.ui.language.css.styler

import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.css.lexer.CssLexer
import com.kira.ui.language.css.lexer.CssToken
import java.io.StringReader

class CssStyler private constructor() : LanguageStyler {

    companion object {

        private var cssStyler: CssStyler? = null

        fun getInstance(): CssStyler {
            return cssStyler ?: CssStyler().also {
                cssStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = CssLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    CssToken.NUMBER -> {
                        val tokenType = TokenType.NUMBER
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.PLUS,
                    CssToken.GT,
                    CssToken.TILDE,
                    CssToken.XOR,
                    CssToken.DOLLAR,
                    CssToken.OR,
                    CssToken.EQ,
                    CssToken.LPAREN,
                    CssToken.RPAREN,
                    CssToken.LBRACE,
                    CssToken.RBRACE,
                    CssToken.LBRACK,
                    CssToken.RBRACK,
                    CssToken.COLON -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.SEMICOLON,
                    CssToken.COMMA -> {
                        continue // skip
                    }
                    CssToken.DATA_TYPE,
                    CssToken.CLASS -> {
                        val tokenType = TokenType.TAG_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.REGEX,
                    CssToken.ANNOTATION,
                    CssToken.IMPORTANT -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.PROPERTY -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.VALUE -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.FUNCTION -> {
                        val tokenType = TokenType.KEYWORD
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd - 1)
                        syntaxHighlightResults.add(syntaxHighlightResult)

                        val braceType = TokenType.OPERATOR
                        val braceResult = SyntaxHighlightResult(braceType, lexer.tokenEnd - 1, lexer.tokenEnd)
                        syntaxHighlightResults.add(braceResult)
                    }
                    CssToken.DOUBLE_QUOTED_STRING,
                    CssToken.SINGLE_QUOTED_STRING -> {
                        val tokenType = TokenType.STRING
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.LINE_COMMENT,
                    CssToken.BLOCK_COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    CssToken.IDENTIFIER,
                    CssToken.WHITESPACE,
                    CssToken.BAD_CHARACTER -> {
                        continue
                    }
                    CssToken.EOF -> {
                        break
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                break
            }
        }
        return syntaxHighlightResults
    }
}