

package com.kira.ui.language.toml

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.toml.parser.TomlParser
import com.kira.ui.language.toml.provider.TomlProvider
import com.kira.ui.language.toml.styler.TomlStyler

class TomlLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "toml"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return TomlParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return TomlProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return TomlStyler.getInstance()
    }
}