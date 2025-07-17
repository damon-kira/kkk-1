

package com.kira.ui.language.actionscript.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.model.TextStructure

class ActionScriptParser private constructor() : LanguageParser {

    companion object {

        private var actionScriptParser: ActionScriptParser? = null

        fun getInstance(): ActionScriptParser {
            return actionScriptParser ?: ActionScriptParser().also {
                actionScriptParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}