

package com.kira.ui.language.shell.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class ShellParser private constructor() : LanguageParser {

    companion object {

        private var shellParser: ShellParser? = null

        fun getInstance(): ShellParser {
            return shellParser ?: ShellParser().also {
                shellParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}