

package com.kira.ui.language.groovy.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class GroovyParser private constructor() : LanguageParser {

    companion object {

        private var groovyParser: GroovyParser? = null

        fun getInstance(): GroovyParser {
            return groovyParser ?: GroovyParser().also {
                groovyParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}