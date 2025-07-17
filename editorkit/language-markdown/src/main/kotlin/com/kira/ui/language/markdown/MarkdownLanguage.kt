

package com.kira.ui.language.markdown

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.markdown.parser.MarkdownParser
import com.kira.ui.language.markdown.provider.MarkdownProvider
import com.kira.ui.language.markdown.styler.MarkdownStyler

class MarkdownLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "markdown"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return MarkdownParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return MarkdownProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return MarkdownStyler.getInstance()
    }
}