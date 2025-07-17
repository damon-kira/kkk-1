

package com.kira.ui.language.python.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class PythonParser private constructor() : LanguageParser {

    companion object {

        private var pythonParser: PythonParser? = null

        fun getInstance(): PythonParser {
            return pythonParser ?: PythonParser().also {
                pythonParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}