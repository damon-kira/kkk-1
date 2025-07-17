

package com.kira.ui.language.sql

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.sql.parser.SqlParser
import com.kira.ui.language.sql.provider.SqlProvider
import com.kira.ui.language.sql.styler.SqlStyler

class SqlLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "sql"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        return SqlParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return SqlProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return SqlStyler.getInstance()
    }
}