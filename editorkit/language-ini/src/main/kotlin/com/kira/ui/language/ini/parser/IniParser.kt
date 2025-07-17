package com.kira.ui.language.ini.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.model.TextStructure

class IniParser private constructor() : LanguageParser {

    companion object {

        private var iniParser: IniParser? = null

        fun getInstance(): IniParser {
            return iniParser ?: IniParser().also {
                iniParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}