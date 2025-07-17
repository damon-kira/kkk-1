

package com.kira.ui.language.base

import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler

interface Language {

    val languageName: String

    fun getParser(): LanguageParser
    fun getProvider(): SuggestionProvider
    fun getStyler(): LanguageStyler
}