package com.kira.ui.language.css.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.model.TextStructure

class CssParser private constructor() : LanguageParser {

    companion object {

        private var cssParser: CssParser? = null

        fun getInstance(): CssParser {
            return cssParser ?: CssParser().also {
                cssParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}