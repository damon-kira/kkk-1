

package com.kira.ui.language.json

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.json.parser.JsonParser
import com.kira.ui.language.json.provider.JsonProvider
import com.kira.ui.language.json.styler.JsonStyler

class JsonLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "json"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return JsonParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return JsonProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return JsonStyler.getInstance()
    }
}