package com.kira.ui.language.base.utils

import com.kira.ui.language.base.model.Suggestion
import com.kira.ui.language.base.model.TextStructure
import java.util.*
import java.util.regex.Pattern

class WordsManager {

    companion object {
        private const val WORDS_REGEX = "\\w((\\w|-)*(\\w))?"
    }

    private val wordsPattern = Pattern.compile(WORDS_REGEX)
    private val lineMap = hashMapOf<Int, LinkedList<Suggestion>>()

    fun getWords(): Set<Suggestion> {
        val wordsSet = hashSetOf<Suggestion>()
        for (line in lineMap.values) {
            for (word in line) {
                wordsSet.add(word)
            }
        }
        return wordsSet
    }

    fun processAllLines(structure: TextStructure) {
        for (line in 0 until structure.lineCount) {
            val text = structure.text.subSequence(
                structure.getIndexForStartOfLine(line),
                structure.getIndexForEndOfLine(line),
            )
            processLine(line, text)
        }
    }

    fun processLine(lineNumber: Int, text: CharSequence) {
        lineMap[lineNumber]?.clear()
        val matcher = wordsPattern.matcher(text)
        while (matcher.find()) {
            val word = Suggestion(
                type = Suggestion.Type.WORD,
                text = text.substring(matcher.start(), matcher.end()),
                returnType = "",
            )
            if (lineMap.containsKey(lineNumber)) {
                lineMap[lineNumber]?.add(word)
            } else {
                lineMap[lineNumber] = LinkedList<Suggestion>()
                    .also { it.add(word) }
            }
        }
    }

    fun deleteLine(lineNumber: Int) {
        lineMap.remove(lineNumber)
    }

    fun clearLines() {
        lineMap.clear()
    }
}