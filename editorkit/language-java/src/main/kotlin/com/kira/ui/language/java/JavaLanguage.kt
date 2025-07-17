

package com.kira.ui.language.java

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.java.parser.JavaParser
import com.kira.ui.language.java.provider.JavaProvider
import com.kira.ui.language.java.styler.JavaStyler

class JavaLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "java"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return JavaParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return JavaProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return JavaStyler.getInstance()
    }
}