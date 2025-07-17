

package com.kira.ui.language.plaintext

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.plaintext.parser.PlainTextParser
import com.kira.ui.language.plaintext.provider.PlainTextProvider
import com.kira.ui.language.plaintext.styler.PlainTextStyler

class PlainTextLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "plaintext"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return PlainTextParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return PlainTextProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return PlainTextStyler.getInstance()
    }
}