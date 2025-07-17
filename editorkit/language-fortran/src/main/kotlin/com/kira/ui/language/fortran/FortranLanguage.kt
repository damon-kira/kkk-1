

package com.kira.ui.language.fortran

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.fortran.parser.FortranParser
import com.kira.ui.language.fortran.provider.FortranProvider
import com.kira.ui.language.fortran.styler.FortranStyler

class FortranLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "fortran"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return FortranParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return FortranProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return FortranStyler.getInstance()
    }
}