

package com.kira.ui.language.latex.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.latex.lexer.LatexLexer
import com.kira.ui.language.latex.lexer.LatexToken
import java.io.StringReader

class LatexStyler private constructor() : LanguageStyler {

    companion object {

        private var latexStyler: LatexStyler? = null

        fun getInstance(): LatexStyler {
            return latexStyler ?: LatexStyler().also {
                latexStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = LatexLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    LatexToken.RESERVED_WORD -> {
                        val tokenType = TokenType.KEYWORD
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    LatexToken.FUNCTION -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    LatexToken.LENGTH,
                    LatexToken.NUMBER -> {
                        val tokenType = TokenType.NUMBER
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    LatexToken.BEGIN_BLOCK -> {
                        for (token in lexer.beginTokens) {
                            val tokenType = when (token.token) {
                                LatexToken.RESERVED_WORD -> TokenType.KEYWORD
                                LatexToken.SEPARATOR -> TokenType.TAG_NAME
                                else -> throw IllegalArgumentException()
                            }
                            val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                            syntaxHighlightResults.add(syntaxHighlightResult)
                        }
                    }
                    LatexToken.END_BLOCK -> {
                        for (token in lexer.endTokens) {
                            val tokenType = when (token.token) {
                                LatexToken.RESERVED_WORD -> TokenType.KEYWORD
                                LatexToken.SEPARATOR -> TokenType.TAG_NAME
                                else -> throw IllegalArgumentException()
                            }
                            val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                            syntaxHighlightResults.add(syntaxHighlightResult)
                        }
                    }
                    LatexToken.LPAREN,
                    LatexToken.RPAREN,
                    LatexToken.LBRACE,
                    LatexToken.RBRACE,
                    LatexToken.LBRACK,
                    LatexToken.RBRACK,
                    LatexToken.SEPARATOR -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    LatexToken.LINE_COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    LatexToken.IDENTIFIER,
                    LatexToken.WHITESPACE,
                    LatexToken.BAD_CHARACTER -> {
                        continue
                    }
                    LatexToken.EOF -> {
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