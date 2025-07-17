

package com.kira.ui.language.visualbasic.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class VisualBasicParser private constructor() : LanguageParser {

    companion object {

        private var visualBasicParser: VisualBasicParser? = null

        fun getInstance(): VisualBasicParser {
            return visualBasicParser ?: VisualBasicParser().also {
                visualBasicParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}