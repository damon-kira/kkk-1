

package com.kira.ui.language.toml.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.toml.lexer.TomlLexer
import com.kira.ui.language.toml.lexer.TomlToken
import java.io.StringReader

class TomlStyler : LanguageStyler {

    companion object {

        private var tomlStyler: TomlStyler? = null

        fun getInstance(): TomlStyler {
            return tomlStyler ?: TomlStyler().also {
                tomlStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = TomlLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    TomlToken.KEY -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    TomlToken.DATE_TIME,
                    TomlToken.NUMBER -> {
                        val tokenType = TokenType.NUMBER
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    TomlToken.COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    TomlToken.BOOLEAN -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    TomlToken.BASIC_STRING,
                    TomlToken.LITERAL_STRING,
                    TomlToken.MULTILINE_BASIC_STRING,
                    TomlToken.MULTILINE_LITERAL_STRING -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    TomlToken.EQ,
                    TomlToken.LBRACE,
                    TomlToken.RBRACE,
                    TomlToken.LBRACK,
                    TomlToken.RBRACK -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    TomlToken.COMMA,
                    TomlToken.DOT,
                    TomlToken.IDENTIFIER,
                    TomlToken.WHITESPACE,
                    TomlToken.BAD_CHARACTER -> {
                        continue
                    }
                    TomlToken.EOF -> {
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