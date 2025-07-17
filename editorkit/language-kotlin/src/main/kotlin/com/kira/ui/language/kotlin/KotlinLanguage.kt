

package com.kira.ui.language.kotlin

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.kotlin.parser.KotlinParser
import com.kira.ui.language.kotlin.provider.KotlinProvider
import com.kira.ui.language.kotlin.styler.KotlinStyler

class KotlinLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "kotlin"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return KotlinParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return KotlinProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return KotlinStyler.getInstance()
    }
}