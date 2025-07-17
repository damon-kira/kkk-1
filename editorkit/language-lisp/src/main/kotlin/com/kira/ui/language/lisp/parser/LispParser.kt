

package com.kira.ui.language.lisp.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class LispParser private constructor() : LanguageParser {

    companion object {

        private var lispParser: LispParser? = null

        fun getInstance(): LispParser {
            return lispParser ?: LispParser().also {
                lispParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}