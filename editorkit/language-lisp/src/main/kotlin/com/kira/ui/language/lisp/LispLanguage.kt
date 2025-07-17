

package com.kira.ui.language.lisp

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.lisp.parser.LispParser
import com.kira.ui.language.lisp.provider.LispProvider
import com.kira.ui.language.lisp.styler.LispStyler

class LispLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "lisp"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return LispParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return LispProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return LispStyler.getInstance()
    }
}