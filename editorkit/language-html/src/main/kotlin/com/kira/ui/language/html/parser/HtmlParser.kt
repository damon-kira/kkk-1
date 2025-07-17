package com.kira.ui.language.html.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.model.TextStructure

class HtmlParser private constructor() : LanguageParser {

    companion object {

        private var htmlParser: HtmlParser? = null

        fun getInstance(): HtmlParser {
            return htmlParser ?: HtmlParser().also {
                htmlParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}