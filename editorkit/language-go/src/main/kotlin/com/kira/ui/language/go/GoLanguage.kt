

package com.kira.ui.language.go

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.go.parser.GoParser
import com.kira.ui.language.go.provider.GoProvider
import com.kira.ui.language.go.styler.GoStyler

class GoLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "go"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return GoParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return GoProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return GoStyler.getInstance()
    }
}