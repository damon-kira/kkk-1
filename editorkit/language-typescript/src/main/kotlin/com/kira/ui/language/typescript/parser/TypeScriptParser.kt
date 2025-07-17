

package com.kira.ui.language.typescript.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class TypeScriptParser private constructor() : LanguageParser {

    companion object {

        private var typeScriptParser: TypeScriptParser? = null

        fun getInstance(): TypeScriptParser {
            return typeScriptParser ?: TypeScriptParser().also {
                typeScriptParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}