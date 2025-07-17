package com.kira.ui.language.base.model

class TextStructure(val text: CharSequence) {

    val lineCount: Int
        get() = lines.size

    private val lines = mutableListOf<Line>()

    init {
        lines.add(Line(0))
    }

    fun add(line: Int, index: Int) {
        if (line != 0) {
            lines.add(line, Line(index))
        }
    }

    fun remove(line: Int) {
        if (line != 0) {
            lines.removeAt(line)
        }
    }

    fun clear() {
        lines.clear()
        lines.add(Line(0))
    }

    fun shiftIndexes(fromLine: Int, shiftBy: Int) {
        if (fromLine in 1 until lineCount) {
            var i = fromLine
            while (i < lineCount) {
                val newIndex = getIndexForLine(i) + shiftBy
                if (i <= 0 || newIndex > 0) {
                    lines[i].start = newIndex
                } else {
                    remove(i)
                    i--
                }
                i++
            }
        }
    }

    fun getIndexForLine(line: Int): Int {
        return if (line >= lineCount) {
            -1
        } else {
            lines[line].start
        }
    }

    fun getIndexForStartOfLine(lineNumber: Int): Int {
        return getIndexForLine(lineNumber)
    }

    fun getIndexForEndOfLine(lineNumber: Int): Int {
        if (lineNumber == lineCount - 1) {
            return text.length
        }
        return getIndexForLine(lineNumber + 1) - 1
    }

    fun getLineForIndex(index: Int): Int {
        var first = 0
        var last = lineCount - 1
        while (first < last) {
            val mid = (first + last) / 2
            if (index < getIndexForLine(mid)) {
                last = mid
            } else if (index <= getIndexForLine(mid) || index < getIndexForLine(mid + 1)) {
                return mid
            } else {
                first = mid + 1
            }
        }
        return lineCount - 1
    }

    fun getLine(line: Int): Line {
        if (line > -1 && line < lineCount) {
            return lines[line]
        }
        return Line(0)
    }

    data class Line(var start: Int)
}