

package com.kira.ui.language.yaml.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.yaml.lexer.YamlLexer
import com.kira.ui.language.yaml.lexer.YamlToken
import java.io.StringReader

class YamlStyler : LanguageStyler {

    companion object {

        private var yamlStyler: YamlStyler? = null

        fun getInstance(): YamlStyler {
            return yamlStyler ?: YamlStyler().also {
                yamlStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = YamlLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    YamlToken.LBRACE,
                    YamlToken.RBRACE,
                    YamlToken.LBRACKET,
                    YamlToken.RBRACKET,
                    YamlToken.COMMA,
                    YamlToken.COLON,
                    YamlToken.AMPERSAND,
                    YamlToken.STAR,
                    YamlToken.QUESTION -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    YamlToken.SCALAR_KEY -> {
                        val tokenType = TokenType.KEYWORD
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    YamlToken.SCALAR_STRING,
                    YamlToken.SCALAR_DSTRING -> {
                        val tokenType = TokenType.STRING
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    YamlToken.COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    YamlToken.SEQUENCE_MARKER,
                    YamlToken.DOCUMENT_MARKER,
                    YamlToken.DOCUMENT_END -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    YamlToken.SCALAR_LIST,
                    YamlToken.SCALAR_TEXT,
                    YamlToken.SCALAR_EOL,
                    YamlToken.TAG,
                    YamlToken.TEXT,
                    YamlToken.ANCHOR,
                    YamlToken.ALIAS,
                    YamlToken.INDENT,
                    YamlToken.WHITESPACE,
                    YamlToken.EOL -> {
                        continue
                    }
                    YamlToken.EOF -> {
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