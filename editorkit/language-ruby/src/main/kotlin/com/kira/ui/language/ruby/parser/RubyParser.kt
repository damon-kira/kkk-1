

package com.kira.ui.language.ruby.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class RubyParser private constructor() : LanguageParser {

    companion object {

        private var rubyParser: RubyParser? = null

        fun getInstance(): RubyParser {
            return rubyParser ?: RubyParser().also {
                rubyParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}