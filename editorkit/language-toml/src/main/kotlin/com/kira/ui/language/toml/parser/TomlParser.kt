

package com.kira.ui.language.toml.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class TomlParser private constructor() : LanguageParser {

    companion object {

        private var tomlParser: TomlParser? = null

        fun getInstance(): TomlParser {
            return tomlParser ?: TomlParser().also {
                tomlParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}