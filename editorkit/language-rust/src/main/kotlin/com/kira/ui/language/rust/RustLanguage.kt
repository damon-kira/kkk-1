

package com.kira.ui.language.rust

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.rust.parser.RustParser
import com.kira.ui.language.rust.provider.RustProvider
import com.kira.ui.language.rust.styler.RustStyler

class RustLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "rust"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return RustParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return RustProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return RustStyler.getInstance()
    }
}