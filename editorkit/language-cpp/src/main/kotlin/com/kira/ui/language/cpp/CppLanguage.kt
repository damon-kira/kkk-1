package com.kira.ui.language.cpp

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.cpp.parser.CppParser
import com.kira.ui.language.cpp.provider.CppProvider
import com.kira.ui.language.cpp.styler.CppStyler

class CppLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "cplusplus"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return CppParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return CppProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return CppStyler.getInstance()
    }
}