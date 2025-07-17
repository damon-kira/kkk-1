

package com.kira.ui.language.python

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.python.parser.PythonParser
import com.kira.ui.language.python.provider.PythonProvider
import com.kira.ui.language.python.styler.PythonStyler

class PythonLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "python"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return PythonParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return PythonProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return PythonStyler.getInstance()
    }
}