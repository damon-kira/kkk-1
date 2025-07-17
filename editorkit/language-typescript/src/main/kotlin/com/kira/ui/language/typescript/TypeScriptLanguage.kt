

package com.kira.ui.language.typescript

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.typescript.parser.TypeScriptParser
import com.kira.ui.language.typescript.provider.TypeScriptProvider
import com.kira.ui.language.typescript.styler.TypeScriptStyler

class TypeScriptLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "typescript"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return TypeScriptParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return TypeScriptProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return TypeScriptStyler.getInstance()
    }
}