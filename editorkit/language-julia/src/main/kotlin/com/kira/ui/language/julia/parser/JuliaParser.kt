

package com.kira.ui.language.julia.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class JuliaParser private constructor() : LanguageParser {

    companion object {

        private var juliaParser: JuliaParser? = null

        fun getInstance(): JuliaParser {
            return juliaParser ?: JuliaParser().also {
                juliaParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}