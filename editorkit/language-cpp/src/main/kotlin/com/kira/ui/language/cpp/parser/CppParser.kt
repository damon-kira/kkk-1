package com.kira.ui.language.cpp.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class CppParser private constructor() : LanguageParser {

    companion object {

        private var cppParser: CppParser? = null

        fun getInstance(): CppParser {
            return cppParser ?: CppParser().also {
                cppParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}