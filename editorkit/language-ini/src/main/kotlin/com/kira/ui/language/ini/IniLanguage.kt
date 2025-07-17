package com.kira.ui.language.ini

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.ini.provider.IniProvider
import com.kira.ui.language.ini.styler.IniStyler

class IniLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "ini"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return com.kira.ui.language.ini.parser.IniParser.Companion.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return IniProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return IniStyler.getInstance()
    }
}