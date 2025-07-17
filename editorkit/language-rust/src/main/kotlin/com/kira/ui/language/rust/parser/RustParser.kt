

package com.kira.ui.language.rust.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class RustParser private constructor() : LanguageParser {

    companion object {

        private var rustParser: RustParser? = null

        fun getInstance(): RustParser {
            return rustParser ?: RustParser().also {
                rustParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}