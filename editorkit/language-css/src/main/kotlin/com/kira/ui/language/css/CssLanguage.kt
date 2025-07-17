package com.kira.ui.language.css

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.css.parser.CssParser
import com.kira.ui.language.css.provider.CssProvider
import com.kira.ui.language.css.styler.CssStyler

class CssLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "css"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return CssParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return CssProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return CssStyler.getInstance()
    }
}