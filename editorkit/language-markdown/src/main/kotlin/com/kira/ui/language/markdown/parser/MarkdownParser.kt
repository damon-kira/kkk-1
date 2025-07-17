

package com.kira.ui.language.markdown.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class MarkdownParser private constructor() : LanguageParser {

    companion object {

        private var markdownParser: MarkdownParser? = null

        fun getInstance(): MarkdownParser {
            return markdownParser ?: MarkdownParser().also {
                markdownParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}