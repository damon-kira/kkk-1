

package com.kira.ui.language.actionscript

import com.kira.ui.language.actionscript.parser.ActionScriptParser
import com.kira.ui.language.actionscript.provider.ActionScriptProvider
import com.kira.ui.language.actionscript.styler.ActionScriptStyler
import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler

class ActionScriptLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "actionscript"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return ActionScriptParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return ActionScriptProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return ActionScriptStyler.getInstance()
    }
}