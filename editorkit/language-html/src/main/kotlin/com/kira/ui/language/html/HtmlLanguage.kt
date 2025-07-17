

package com.kira.ui.language.html

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.html.parser.HtmlParser
import com.kira.ui.language.html.provider.HtmlProvider
import com.kira.ui.language.html.styler.HtmlStyler

class HtmlLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "html"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return HtmlParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return HtmlProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return HtmlStyler.getInstance()
    }
}