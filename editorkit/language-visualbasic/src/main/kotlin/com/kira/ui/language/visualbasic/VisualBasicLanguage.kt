

package com.kira.ui.language.visualbasic

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.visualbasic.parser.VisualBasicParser
import com.kira.ui.language.visualbasic.provider.VisualBasicProvider
import com.kira.ui.language.visualbasic.styler.VisualBasicStyler

class VisualBasicLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "visualbasic"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return VisualBasicParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return VisualBasicProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return VisualBasicStyler.getInstance()
    }
}