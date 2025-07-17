

package com.kira.ui.language.lua

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.lua.parser.LuaParser
import com.kira.ui.language.lua.provider.LuaProvider
import com.kira.ui.language.lua.styler.LuaStyler

class LuaLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "lua"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return LuaParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return LuaProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return LuaStyler.getInstance()
    }
}