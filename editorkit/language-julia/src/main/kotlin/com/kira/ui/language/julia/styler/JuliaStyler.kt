package com.kira.ui.language.julia.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.julia.lexer.JuliaLexer
import com.kira.ui.language.julia.lexer.JuliaToken
import java.io.StringReader
import java.util.regex.Pattern

class JuliaStyler private constructor() : LanguageStyler {

    companion object {

        private val METHOD = Pattern.compile("(?<=(function)) (\\w+)")

        private var juliaStyler: JuliaStyler? = null

        fun getInstance(): JuliaStyler {
            return juliaStyler ?: JuliaStyler().also {
                juliaStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = JuliaLexer(sourceReader)

        // FIXME flex doesn't support positive lookbehind
        val matcher = METHOD.matcher(source)
        matcher.region(0, source.length)
        while (matcher.find()) {
            val tokenType = TokenType.METHOD
            val syntaxHighlightResult = SyntaxHighlightResult(tokenType, matcher.start(), matcher.end())
            syntaxHighlightResults.add(syntaxHighlightResult)
        }

        while (true) {
            try {
                when (lexer.advance()) {
                    JuliaToken.INTEGER_LITERAL,
                    JuliaToken.FLOAT_LITERAL,
                    JuliaToken.DOUBLE_LITERAL -> {
                        val tokenType = TokenType.NUMBER
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    JuliaToken.OPERATOR -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    /*JuliaToken.BASE_MODULE_FUNCS,
                    JuliaToken.BASE_MACROS,
                    JuliaToken.BASE_MODULES,
                    JuliaToken.BASE_FUNCS -> {
                        continue // skip
                    }*/
                    JuliaToken.BASE_TYPES -> {
                        val tokenType = TokenType.TYPE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    JuliaToken.KEYWORD_OTHER,
                    JuliaToken.KEYWORD_CONTROL -> {
                        val tokenType = TokenType.KEYWORD
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    JuliaToken.CONSTANTS -> {
                        val tokenType = TokenType.LANG_CONST
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    JuliaToken.DOUBLE_QUOTED_STRING,
                    JuliaToken.SINGLE_QUOTED_STRING,
                    JuliaToken.LONG_DOUBLE_QUOTED_STRING,
                    JuliaToken.SINGLE_BACKTICK_STRING -> {
                        val tokenType = TokenType.STRING
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    JuliaToken.LINE_COMMENT,
                    JuliaToken.BLOCK_COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    JuliaToken.IDENTIFIER,
                    JuliaToken.WHITESPACE,
                    JuliaToken.BAD_CHARACTER -> {
                        continue
                    }
                    JuliaToken.EOF -> {
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