package com.kira.ui.language.lua.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class LuaParser private constructor() : LanguageParser {

    companion object {

        private var luaParser: LuaParser? = null

        fun getInstance(): LuaParser {
            return luaParser ?: LuaParser().also {
                luaParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}