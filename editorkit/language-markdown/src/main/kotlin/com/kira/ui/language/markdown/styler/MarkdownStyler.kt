

package com.kira.ui.language.markdown.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.markdown.lexer.MarkdownLexer
import com.kira.ui.language.markdown.lexer.MarkdownToken
import java.io.StringReader

class MarkdownStyler private constructor() : LanguageStyler {

    companion object {

        private var markdownStyler: MarkdownStyler? = null

        fun getInstance(): MarkdownStyler {
            return markdownStyler ?: MarkdownStyler().also {
                markdownStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = MarkdownLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    MarkdownToken.HEADER -> {
                        val tokenType = TokenType.TAG_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.UNORDERED_LIST_ITEM,
                    MarkdownToken.ORDERED_LIST_ITEM -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.BOLDITALIC1,
                    MarkdownToken.BOLDITALIC2 -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.BOLD1,
                    MarkdownToken.BOLD2 -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.ITALIC1,
                    MarkdownToken.ITALIC2 -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.STRIKETHROUGH -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.CODE,
                    MarkdownToken.CODE_BLOCK -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.LT,
                    MarkdownToken.GT,
                    MarkdownToken.EQ,
                    MarkdownToken.NOT,
                    MarkdownToken.DIV,
                    MarkdownToken.MINUS,
                    MarkdownToken.LPAREN,
                    MarkdownToken.RPAREN,
                    MarkdownToken.LBRACE,
                    MarkdownToken.RBRACE,
                    MarkdownToken.LBRACK,
                    MarkdownToken.RBRACK -> {
                        val tokenType = TokenType.TAG
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.URL -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    MarkdownToken.IDENTIFIER,
                    MarkdownToken.WHITESPACE,
                    MarkdownToken.BAD_CHARACTER -> {
                        continue
                    }
                    MarkdownToken.EOF -> {
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