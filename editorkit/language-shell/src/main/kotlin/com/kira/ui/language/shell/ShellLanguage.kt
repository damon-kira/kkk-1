

package com.kira.ui.language.shell

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.shell.parser.ShellParser
import com.kira.ui.language.shell.provider.ShellProvider
import com.kira.ui.language.shell.styler.ShellStyler

class ShellLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "shell"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return ShellParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return ShellProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return ShellStyler.getInstance()
    }
}