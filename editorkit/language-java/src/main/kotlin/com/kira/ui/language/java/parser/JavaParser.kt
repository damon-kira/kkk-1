package com.kira.ui.language.java.parser

import com.kira.ui.language.base.model.ParseResult
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.parser.LanguageParser

class JavaParser private constructor() : LanguageParser {

    companion object {

        private var javaParser: JavaParser? = null

        fun getInstance(): JavaParser {
            return javaParser ?: JavaParser().also {
                javaParser = it
            }
        }
    }

    override fun execute(structure: TextStructure): ParseResult {
        TODO("Not yet implemented")
    }
}