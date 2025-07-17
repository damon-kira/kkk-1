

package com.kira.ui.language.smali

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.smali.parser.SmaliParser
import com.kira.ui.language.smali.provider.SmaliProvider
import com.kira.ui.language.smali.styler.SmaliStyler

class SmaliLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "smali"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return SmaliParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return SmaliProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return SmaliStyler.getInstance()
    }
}