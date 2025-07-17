

package com.kira.ui.language.smali.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class SmaliParser private constructor() : LanguageParser {

    companion object {

        private var smaliParser: SmaliParser? = null

        fun getInstance(): SmaliParser {
            return smaliParser ?: SmaliParser().also {
                smaliParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}