package com.kira.ui.language.xml.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class XmlParser private constructor() : LanguageParser {

    companion object {

        private var xmlParser: XmlParser? = null

        fun getInstance(): XmlParser {
            return xmlParser ?: XmlParser().also {
                xmlParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}