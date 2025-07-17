package com.kira.ui.language.c.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class CParser private constructor() : LanguageParser {

    companion object {

        private var cParser: CParser? = null

        fun getInstance(): CParser {
            return cParser ?: CParser().also {
                cParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}