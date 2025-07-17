package com.kira.ui.language.base.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure

interface LanguageParser {
    fun execute(structure: TextStructure): ParseResult
}