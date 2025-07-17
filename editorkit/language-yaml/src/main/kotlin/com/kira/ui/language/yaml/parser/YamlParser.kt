

package com.kira.ui.language.yaml.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class YamlParser private constructor() : LanguageParser {

    companion object {

        private var yamlParser: YamlParser? = null

        fun getInstance(): YamlParser {
            return yamlParser ?: YamlParser().also {
                yamlParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}