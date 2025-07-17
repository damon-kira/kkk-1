

package com.kira.ui.language.kotlin.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class KotlinParser private constructor() : LanguageParser {

    companion object {

        private var kotlinParser: KotlinParser? = null

        fun getInstance(): KotlinParser {
            return kotlinParser ?: KotlinParser().also {
                kotlinParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}