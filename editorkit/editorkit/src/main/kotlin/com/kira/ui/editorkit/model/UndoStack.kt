

package com.kira.ui.editorkit.model

class UndoStack() {

    companion object {
        const val MAX_SIZE = Integer.MAX_VALUE
    }

    val size: Int
        get() = stack.size

    private var stack = mutableListOf<TextChange>()
    private var currentSize = 0

    private constructor(stack: List<TextChange>) : this() {
        this.stack = stack.toMutableList()
    }

    operator fun get(index: Int): TextChange {
        return stack[index]
    }

    fun pop(): TextChange {
        val item = stack[size - 1]
        stack.removeAt(size - 1)
        currentSize -= item.newText.length + item.oldText.length
        return item
    }

    fun push(textChange: TextChange) {
        val delta = textChange.newText.length + textChange.oldText.length
        if (delta < MAX_SIZE) {
            if (size > 0) {
                val previous = stack[size - 1]
                val toCharArray: CharArray
                val length: Int
                var allWhitespace: Boolean
                var allLettersDigits: Boolean
                var i = 0
                if (textChange.oldText.isEmpty() &&
                    textChange.newText.length == 1 &&
                    previous.oldText.isEmpty()
                ) {
                    if (previous.start + previous.newText.length != textChange.start) {
                        stack.add(textChange)
                    } else if (textChange.newText[0].isWhitespace()) {
                        allWhitespace = true
                        toCharArray = previous.newText.toCharArray()
                        length = toCharArray.size
                        while (i < length) {
                            if (!toCharArray[i].isWhitespace()) {
                                allWhitespace = false
                            }
                            i++
                        }
                        if (allWhitespace) {
                            previous.newText += textChange.newText
                        } else {
                            stack.add(textChange)
                        }
                    } else if (textChange.newText[0].isLetterOrDigit()) {
                        allLettersDigits = true
                        toCharArray = previous.newText.toCharArray()
                        length = toCharArray.size
                        while (i < length) {
                            if (!toCharArray[i].isLetterOrDigit()) {
                                allLettersDigits = false
                            }
                            i++
                        }
                        if (allLettersDigits) {
                            previous.newText += textChange.newText
                        } else {
                            stack.add(textChange)
                        }
                    } else {
                        stack.add(textChange)
                    }
                } else if (textChange.oldText.length != 1 ||
                    textChange.newText.isNotEmpty() ||
                    previous.newText.isNotEmpty()
                ) {
                    stack.add(textChange)
                } else if (previous.start - 1 != textChange.start) {
                    stack.add(textChange)
                } else if (textChange.oldText[0].isWhitespace()) {
                    allWhitespace = true
                    toCharArray = previous.oldText.toCharArray()
                    length = toCharArray.size
                    while (i < length) {
                        if (!toCharArray[i].isWhitespace()) {
                            allWhitespace = false
                        }
                        i++
                    }
                    if (allWhitespace) {
                        previous.oldText = textChange.oldText + previous.oldText
                        previous.start -= textChange.oldText.length
                    } else {
                        stack.add(textChange)
                    }
                } else if (textChange.oldText[0].isLetterOrDigit()) {
                    allLettersDigits = true
                    toCharArray = previous.oldText.toCharArray()
                    length = toCharArray.size
                    while (i < length) {
                        if (!toCharArray[i].isLetterOrDigit()) {
                            allLettersDigits = false
                        }
                        i++
                    }
                    if (allLettersDigits) {
                        previous.oldText = textChange.oldText + previous.oldText
                        previous.start -= textChange.oldText.length
                    } else {
                        stack.add(textChange)
                    }
                } else {
                    stack.add(textChange)
                }
            } else {
                stack.add(textChange)
            }
            currentSize += delta
            while (currentSize > MAX_SIZE) {
                if (!removeLast()) {
                    return
                }
            }
            return
        }
        removeAll()
    }

    fun removeAll() {
        currentSize = 0
        stack.clear()
    }

    fun canUndo(): Boolean {
        return size > 0
    }

    fun clone(): UndoStack {
        return UndoStack(stack)
    }

    private fun removeLast(): Boolean {
        if (size <= 0) {
            return false
        }
        val item = stack[0]
        stack.removeAt(0)
        currentSize -= item.newText.length + item.oldText.length
        return true
    }
}