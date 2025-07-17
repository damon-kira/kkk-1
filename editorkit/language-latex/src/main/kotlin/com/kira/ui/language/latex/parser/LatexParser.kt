

package com.kira.ui.language.latex.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class LatexParser private constructor() : LanguageParser {

    companion object {

        private var latexParser: LatexParser? = null

        fun getInstance(): LatexParser {
            return latexParser ?: LatexParser().also {
                latexParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}