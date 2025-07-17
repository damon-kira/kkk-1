

package com.kira.ui.language.groovy

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.groovy.parser.GroovyParser
import com.kira.ui.language.groovy.provider.GroovyProvider
import com.kira.ui.language.groovy.styler.GroovyStyler

class GroovyLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "groovy"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return GroovyParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return GroovyProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return GroovyStyler.getInstance()
    }
}