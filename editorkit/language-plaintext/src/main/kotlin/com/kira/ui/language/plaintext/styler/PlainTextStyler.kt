

package com.kira.ui.language.plaintext.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.styler.LanguageStyler

class PlainTextStyler private constructor() : LanguageStyler {

    companion object {

        private var plainTextStyler: PlainTextStyler? = null

        fun getInstance(): PlainTextStyler {
            return plainTextStyler ?: PlainTextStyler().also {
                plainTextStyler = it
            }
        }
    }

    override fun execute(structure: TextStructure): List<SyntaxHighlightResult> {
        return emptyList()
    }
}