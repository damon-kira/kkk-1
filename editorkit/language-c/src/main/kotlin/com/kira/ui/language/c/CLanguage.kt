package com.kira.ui.language.c

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.c.parser.CParser
import com.kira.ui.language.c.provider.CProvider
import com.kira.ui.language.c.styler.CStyler

class CLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "c"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return CParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return CProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return CStyler.getInstance()
    }
}