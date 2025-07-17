package com.kira.ui.language.javascript

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.javascript.parser.JavaScriptParser
import com.kira.ui.language.javascript.provider.JavaScriptProvider
import com.kira.ui.language.javascript.styler.JavaScriptStyler

class JavaScriptLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "javascript"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return JavaScriptParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return JavaScriptProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return JavaScriptStyler.getInstance()
    }
}