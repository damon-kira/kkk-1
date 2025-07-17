package com.kira.ui.language.sql.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class SqlParser private constructor() : LanguageParser {

    companion object {

        private var sqlParser: SqlParser? = null

        fun getInstance(): SqlParser {
            return sqlParser ?: SqlParser().also {
                sqlParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}