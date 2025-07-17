

package com.kira.ui.language.fortran.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class FortranParser private constructor() : LanguageParser {

    companion object {

        private var fortranParser: FortranParser? = null

        fun getInstance(): FortranParser {
            return fortranParser ?: FortranParser().also {
                fortranParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}