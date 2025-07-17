package com.kira.ui.language.json.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.json.lexer.JsonLexer
import com.kira.ui.language.json.lexer.JsonToken
import java.io.StringReader

class JsonStyler private constructor() : LanguageStyler {

    companion object {

        private var jsonStyler: JsonStyler? = null

        fun getInstance(): JsonStyler {
            return jsonStyler ?: JsonStyler().also {
                jsonStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = JsonLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    JsonToken.NUMBER -> {
                        val tokenType = TokenType.NUMBER
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    JsonToken.LBRACE,
                    JsonToken.RBRACE,
                    JsonToken.LBRACK,
                    JsonToken.RBRACK,
                    JsonToken.COMMA,
                    JsonToken.COLON -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    JsonToken.TRUE,
                    JsonToken.FALSE,
                    JsonToken.NULL -> {
                        val tokenType = TokenType.LANG_CONST
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    JsonToken.DOUBLE_QUOTED_STRING,
                    JsonToken.SINGLE_QUOTED_STRING -> {
                        val tokenType = TokenType.STRING
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    JsonToken.BLOCK_COMMENT,
                    JsonToken.LINE_COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    JsonToken.IDENTIFIER,
                    JsonToken.WHITESPACE,
                    JsonToken.BAD_CHARACTER -> {
                        continue
                    }

                    JsonToken.EOF -> {
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