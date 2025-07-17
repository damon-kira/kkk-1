package com.kira.ui.language.ini.styler

import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.ini.lexer.IniLexer
import com.kira.ui.language.ini.lexer.IniToken
import java.io.StringReader

class IniStyler : LanguageStyler {

    companion object {

        private var iniStyler: IniStyler? = null

        fun getInstance(): IniStyler {
            return iniStyler ?: IniStyler().also {
                iniStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = IniLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    IniToken.SECTION -> {
                        val tokenType = TokenType.TAG_NAME
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    IniToken.COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    IniToken.EQUALS -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    IniToken.KEY -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    IniToken.VALUE -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult =
                            SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }

                    IniToken.WHITESPACE,
                    IniToken.BAD_CHARACTER -> {
                        continue
                    }

                    IniToken.EOF -> {
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