

package com.kira.ui.language.ruby

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.ruby.parser.RubyParser
import com.kira.ui.language.ruby.provider.RubyProvider
import com.kira.ui.language.ruby.styler.RubyStyler

class RubyLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "ruby"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return RubyParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return RubyProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return RubyStyler.getInstance()
    }
}