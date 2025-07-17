package com.kira.ui.language.go.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class GoParser private constructor() : LanguageParser {

    companion object {

        private var goParser: GoParser? = null

        fun getInstance(): GoParser {
            return goParser ?: GoParser().also {
                goParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}