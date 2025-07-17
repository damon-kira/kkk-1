package com.kira.ui.language.php.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class PhpParser private constructor() : LanguageParser {

    companion object {

        private var phpParser: PhpParser? = null

        fun getInstance(): PhpParser {
            return phpParser ?: PhpParser().also {
                phpParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}