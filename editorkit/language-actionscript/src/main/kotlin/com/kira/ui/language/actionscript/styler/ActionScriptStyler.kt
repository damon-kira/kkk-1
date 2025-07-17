

package com.kira.ui.language.actionscript.styler

import com.kira.ui.language.actionscript.lexer.ActionScriptLexer
import com.kira.ui.language.actionscript.lexer.ActionScriptToken
import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.base.model.TokenType
import java.io.StringReader
import java.util.regex.Pattern

class ActionScriptStyler private constructor() : LanguageStyler {

    companion object {

        private val METHOD = Pattern.compile("(?<=(function)) (\\w+)")

        private var actionScriptStyler: ActionScriptStyler? = null

        fun getInstance(): ActionScriptStyler {
            return actionScriptStyler ?: ActionScriptStyler().also {
                actionScriptStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = ActionScriptLexer(sourceReader)

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
                    ActionScriptToken.LONG_LITERAL,
                    ActionScriptToken.INTEGER_LITERAL,
                    ActionScriptToken.FLOAT_LITERAL,
                    ActionScriptToken.DOUBLE_LITERAL -> {
                        val tokenType = TokenType.NUMBER
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.PLUS,
                    ActionScriptToken.MINUSMINUS,
                    ActionScriptToken.DIV,
                    ActionScriptToken.PLUSPLUS,
                    ActionScriptToken.MOD,
                    ActionScriptToken.MULT,
                    ActionScriptToken.MINUS,
                    ActionScriptToken.PLUSEQ,
                    ActionScriptToken.DIVEQ,
                    ActionScriptToken.MODEQ,
                    ActionScriptToken.MULTEQ,
                    ActionScriptToken.MINUSEQ,
                    ActionScriptToken.EQ,
                    ActionScriptToken.AND,
                    ActionScriptToken.LTLT,
                    ActionScriptToken.TILDE,
                    ActionScriptToken.OR,
                    ActionScriptToken.GTGT,
                    ActionScriptToken.GTGTGT,
                    ActionScriptToken.XOR,
                    ActionScriptToken.ANDEQ,
                    ActionScriptToken.LTLTEQ,
                    ActionScriptToken.OREQ,
                    ActionScriptToken.GTGTEQ,
                    ActionScriptToken.GTGTGTEQ,
                    ActionScriptToken.XOREQ,
                    ActionScriptToken.EQEQ,
                    ActionScriptToken.GT,
                    ActionScriptToken.GTEQ,
                    ActionScriptToken.NOTEQ,
                    ActionScriptToken.LT,
                    ActionScriptToken.LTEQ,
                    ActionScriptToken.EQEQEQ,
                    ActionScriptToken.NOTEQEQ,
                    ActionScriptToken.ANDAND,
                    ActionScriptToken.ANDANDEQ,
                    ActionScriptToken.NOT,
                    ActionScriptToken.OROR,
                    ActionScriptToken.OROREQ,
                    ActionScriptToken.LPAREN,
                    ActionScriptToken.RPAREN,
                    ActionScriptToken.LBRACE,
                    ActionScriptToken.RBRACE,
                    ActionScriptToken.LBRACK,
                    ActionScriptToken.RBRACK,
                    ActionScriptToken.QUEST,
                    ActionScriptToken.COLON -> {
                        val tokenType = TokenType.OPERATOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.SEMICOLON,
                    ActionScriptToken.COMMA,
                    ActionScriptToken.DOT -> {
                        continue // skip
                    }
                    ActionScriptToken.BREAK,
                    ActionScriptToken.CASE,
                    ActionScriptToken.CONTINUE,
                    ActionScriptToken.DEFAULT,
                    ActionScriptToken.DO,
                    ActionScriptToken.WHILE,
                    ActionScriptToken.ELSE,
                    ActionScriptToken.FOR,
                    ActionScriptToken.IN,
                    ActionScriptToken.EACH,
                    ActionScriptToken.IF,
                    ActionScriptToken.LABEL,
                    ActionScriptToken.RETURN,
                    ActionScriptToken.SUPER,
                    ActionScriptToken.SWITCH,
                    ActionScriptToken.THROW,
                    ActionScriptToken.TRY,
                    ActionScriptToken.CATCH,
                    ActionScriptToken.FINALLY,
                    ActionScriptToken.WITH,
                    ActionScriptToken.DYNAMIC,
                    ActionScriptToken.FINAL,
                    ActionScriptToken.INTERNAL,
                    ActionScriptToken.NATIVE,
                    ActionScriptToken.OVERRIDE,
                    ActionScriptToken.PRIVATE,
                    ActionScriptToken.PROTECTED,
                    ActionScriptToken.PUBLIC,
                    ActionScriptToken.STATIC,
                    ActionScriptToken.PARAMETER,
                    ActionScriptToken.CLASS,
                    ActionScriptToken.CONST,
                    ActionScriptToken.EXTENDS,
                    ActionScriptToken.FUNCTION,
                    ActionScriptToken.GET,
                    ActionScriptToken.IMPLEMENTS,
                    ActionScriptToken.INTERFACE,
                    ActionScriptToken.NAMESPACE,
                    ActionScriptToken.PACKAGE,
                    ActionScriptToken.TYPEOF,
                    ActionScriptToken.SET,
                    ActionScriptToken.THIS,
                    ActionScriptToken.INCLUDE,
                    ActionScriptToken.INSTANCEOF,
                    ActionScriptToken.IMPORT,
                    ActionScriptToken.USE,
                    ActionScriptToken.AS,
                    ActionScriptToken.NEW,
                    ActionScriptToken.VAR -> {
                        val tokenType = TokenType.KEYWORD
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.ARRAY,
                    ActionScriptToken.OBJECT,
                    ActionScriptToken.BOOLEAN,
                    ActionScriptToken.NUMBER,
                    ActionScriptToken.STRING,
                    ActionScriptToken.VOID,
                    ActionScriptToken.VECTOR,
                    ActionScriptToken.INT,
                    ActionScriptToken.UINT -> {
                        val tokenType = TokenType.TYPE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.TRUE,
                    ActionScriptToken.FALSE,
                    ActionScriptToken.NULL,
                    ActionScriptToken.UNDEFINED,
                    ActionScriptToken.NAN -> {
                        val tokenType = TokenType.LANG_CONST
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.PREPROCESSOR -> {
                        val tokenType = TokenType.PREPROCESSOR
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.DOUBLE_QUOTED_STRING,
                    ActionScriptToken.SINGLE_QUOTED_STRING -> {
                        val tokenType = TokenType.STRING
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.LINE_COMMENT,
                    ActionScriptToken.BLOCK_COMMENT -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    ActionScriptToken.IDENTIFIER,
                    ActionScriptToken.WHITESPACE,
                    ActionScriptToken.BAD_CHARACTER -> {
                        continue
                    }
                    ActionScriptToken.EOF -> {
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