

package com.kira.ui.language.xml

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.xml.parser.XmlParser
import com.kira.ui.language.xml.provider.XmlProvider
import com.kira.ui.language.xml.styler.XmlStyler

class XmlLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "xml"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return XmlParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return XmlProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return XmlStyler.getInstance()
    }
}