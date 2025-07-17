

package com.kira.ui.language.plaintext.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class PlainTextParser private constructor() : LanguageParser {

    companion object {

        private var plainTextParser: PlainTextParser? = null

        fun getInstance(): PlainTextParser {
            return plainTextParser ?: PlainTextParser().also {
                plainTextParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        return ParseResult(null)
    }
}