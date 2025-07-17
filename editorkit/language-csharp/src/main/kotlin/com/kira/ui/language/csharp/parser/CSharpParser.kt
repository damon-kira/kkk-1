package com.kira.ui.language.csharp.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class CSharpParser private constructor() : LanguageParser {

    companion object {

        private var csharpParser: CSharpParser? = null

        fun getInstance(): CSharpParser {
            return csharpParser ?: CSharpParser().also {
                csharpParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}