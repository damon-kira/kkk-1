

package com.kira.ui.language.php

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.php.parser.PhpParser
import com.kira.ui.language.php.provider.PhpProvider
import com.kira.ui.language.php.styler.PhpStyler

class PhpLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "php"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return PhpParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return PhpProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return PhpStyler.getInstance()
    }
}