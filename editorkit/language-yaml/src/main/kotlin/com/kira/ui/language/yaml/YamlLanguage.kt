

package com.kira.ui.language.yaml

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.yaml.parser.YamlParser
import com.kira.ui.language.yaml.provider.YamlProvider
import com.kira.ui.language.yaml.styler.YamlStyler

class YamlLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "yaml"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return YamlParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return YamlProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return YamlStyler.getInstance()
    }
}