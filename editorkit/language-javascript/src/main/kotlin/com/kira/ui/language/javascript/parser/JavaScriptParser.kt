

package com.kira.ui.language.javascript.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.model.TextStructure

class JavaScriptParser private constructor() : LanguageParser {

    companion object {

        private var javaScriptParser: JavaScriptParser? = null

        fun getInstance(): JavaScriptParser {
            return javaScriptParser ?: JavaScriptParser().also {
                javaScriptParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}