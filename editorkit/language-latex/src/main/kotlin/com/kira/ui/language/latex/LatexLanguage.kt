

package com.kira.ui.language.latex

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.latex.parser.LatexParser
import com.kira.ui.language.latex.provider.LatexProvider
import com.kira.ui.language.latex.styler.LatexStyler

class LatexLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "latex"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return LatexParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return LatexProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return LatexStyler.getInstance()
    }
}