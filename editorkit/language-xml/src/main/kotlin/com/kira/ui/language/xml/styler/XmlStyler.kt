package com.kira.ui.language.xml.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.xml.lexer.XmlLexer
import com.kira.ui.language.xml.lexer.XmlToken
import java.io.StringReader

class XmlStyler private constructor() : LanguageStyler {

    companion object {

        private var xmlStyler: XmlStyler? = null

        fun getInstance(): XmlStyler {
            return xmlStyler ?: XmlStyler().also {
                xmlStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        val source = structure.text.toString()
        val syntaxHighlightResults = mutableListOf<SyntaxHighlightResult>()
        val sourceReader = StringReader(source)
        val lexer = XmlLexer(sourceReader)

        while (true) {
            try {
                when (lexer.advance()) {
                    XmlToken.XML_CHAR_ENTITY_REF,
                    XmlToken.XML_ENTITY_REF_TOKEN -> {
                        val tokenType = TokenType.ENTITY_REF
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    XmlToken.XML_TAG_NAME -> {
                        val tokenType = TokenType.TAG_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    XmlToken.XML_ATTR_NAME -> {
                        val tokenType = TokenType.ATTR_NAME
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    XmlToken.XML_DOCTYPE_PUBLIC,
                    XmlToken.XML_DOCTYPE_SYSTEM,
                    XmlToken.XML_DOCTYPE_START,
                    XmlToken.XML_DOCTYPE_END,
                    XmlToken.XML_PI_START,
                    XmlToken.XML_PI_END,
                    XmlToken.XML_PI_TARGET,
                    XmlToken.XML_EMPTY_ELEMENT_END,
                    XmlToken.XML_TAG_END,
                    XmlToken.XML_CDATA_START,
                    XmlToken.XML_CDATA_END,
                    XmlToken.XML_START_TAG_START,
                    XmlToken.XML_END_TAG_START -> {
                        val tokenType = TokenType.TAG
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    XmlToken.XML_ATTRIBUTE_VALUE -> {
                        val tokenType = TokenType.ATTR_VALUE
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    XmlToken.XML_COMMENT_CHARACTERS -> {
                        val tokenType = TokenType.COMMENT
                        val syntaxHighlightResult = SyntaxHighlightResult(tokenType, lexer.tokenStart, lexer.tokenEnd)
                        syntaxHighlightResults.add(syntaxHighlightResult)
                    }
                    XmlToken.XML_DATA_CHARACTERS,
                    XmlToken.XML_TAG_CHARACTERS,
                    XmlToken.WHITESPACE,
                    XmlToken.BAD_CHARACTER -> {
                        continue
                    }
                    XmlToken.EOF -> {
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