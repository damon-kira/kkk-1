

package com.kira.ui.language.json.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class JsonParser private constructor() : LanguageParser {

    companion object {

        private var jsonParser: JsonParser? = null

        fun getInstance(): JsonParser {
            return jsonParser ?: JsonParser().also {
                jsonParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}