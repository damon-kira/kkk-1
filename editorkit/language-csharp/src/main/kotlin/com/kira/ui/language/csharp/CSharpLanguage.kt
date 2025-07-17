

package com.kira.ui.language.csharp

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.csharp.parser.CSharpParser
import com.kira.ui.language.csharp.provider.CSharpProvider
import com.kira.ui.language.csharp.styler.CSharpStyler

class CSharpLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "csharp"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return CSharpParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return CSharpProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return CSharpStyler.getInstance()
    }
}