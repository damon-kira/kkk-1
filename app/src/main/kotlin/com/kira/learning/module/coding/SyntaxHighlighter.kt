package com.kira.learning.module.coding

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * 语法高亮处理器
 * 为不同编程语言提供语法高亮功能
 */
object SyntaxHighlighter {

    @Composable
    fun highlightCode(code: String, language: ProgrammingLanguage): AnnotatedString {
        return when (language) {
            ProgrammingLanguage.PYTHON -> highlightPython(code)
            ProgrammingLanguage.JAVA -> highlightJava(code)
            ProgrammingLanguage.KOTLIN -> highlightKotlin(code)
            ProgrammingLanguage.JAVASCRIPT -> highlightJavaScript(code)
            ProgrammingLanguage.CPP -> highlightCpp(code)
            ProgrammingLanguage.C -> highlightC(code)
        }
    }

    @Composable
    private fun highlightPython(code: String): AnnotatedString {
        val keywords = setOf(
            "def", "class", "if", "elif", "else", "for", "while", "try", "except", "finally",
            "import", "from", "as", "return", "yield", "lambda", "with", "pass", "break",
            "continue", "and", "or", "not", "in", "is", "None", "True", "False", "global",
            "nonlocal", "assert", "del", "async", "await", "print", "input", "len", "range"
        )

        val builtins = setOf(
            "int", "float", "str", "list", "dict", "tuple", "set", "bool", "type", "object",
            "super", "property", "staticmethod", "classmethod", "enumerate", "zip", "map",
            "filter", "sorted", "reversed", "sum", "min", "max", "abs", "round"
        )

        return buildAnnotatedString {
            highlightCodeGeneric(
                code = code,
                keywords = keywords,
                builtins = builtins,
                singleLineComment = "#",
                multiLineCommentStart = "\"\"\"",
                multiLineCommentEnd = "\"\"\"",
                stringDelimiters = setOf("\"", "'")
            )
        }
    }

    @Composable
    private fun highlightJava(code: String): AnnotatedString {
        val keywords = setOf(
            "public", "private", "protected", "static", "final", "abstract", "synchronized",
            "volatile", "transient", "native", "strictfp", "class", "interface", "enum",
            "extends", "implements", "package", "import", "if", "else", "for", "while",
            "do", "switch", "case", "default", "break", "continue", "return", "try",
            "catch", "finally", "throw", "throws", "new", "this", "super", "instanceof",
            "void", "boolean", "byte", "char", "short", "int", "long", "float", "double"
        )

        val builtins = setOf(
            "String", "Object", "System", "Math", "Integer", "Double", "Boolean", "Character",
            "ArrayList", "HashMap", "Scanner", "Exception", "Thread", "Runnable"
        )

        return buildAnnotatedString {
            highlightCodeGeneric(
                code = code,
                keywords = keywords,
                builtins = builtins,
                singleLineComment = "//",
                multiLineCommentStart = "/*",
                multiLineCommentEnd = "*/",
                stringDelimiters = setOf("\"")
            )
        }
    }

    @Composable
    private fun highlightKotlin(code: String): AnnotatedString {
        val keywords = setOf(
            "fun", "val", "var", "class", "object", "interface", "enum", "data", "sealed",
            "abstract", "open", "final", "override", "private", "public", "protected",
            "internal", "if", "else", "when", "for", "while", "do", "try", "catch",
            "finally", "throw", "return", "break", "continue", "in", "!in", "is", "!is",
            "as", "as?", "this", "super", "null", "true", "false", "lazy", "lateinit",
            "suspend", "inline", "noinline", "crossinline", "reified", "operator",
            "infix", "tailrec", "vararg", "companion", "init", "constructor"
        )

        val builtins = setOf(
            "String", "Int", "Long", "Float", "Double", "Boolean", "Char", "Byte", "Short",
            "Array", "List", "MutableList", "Set", "MutableSet", "Map", "MutableMap",
            "Any", "Unit", "Nothing", "Pair", "Triple", "println", "print", "readLine"
        )

        return buildAnnotatedString {
            highlightCodeGeneric(
                code = code,
                keywords = keywords,
                builtins = builtins,
                singleLineComment = "//",
                multiLineCommentStart = "/*",
                multiLineCommentEnd = "*/",
                stringDelimiters = setOf("\"", "'")
            )
        }
    }

    @Composable
    private fun highlightJavaScript(code: String): AnnotatedString {
        val keywords = setOf(
            "function", "var", "let", "const", "if", "else", "for", "while", "do", "switch",
            "case", "default", "break", "continue", "return", "try", "catch", "finally",
            "throw", "new", "this", "typeof", "instanceof", "in", "of", "class", "extends",
            "import", "export", "from", "default", "async", "await", "yield", "true",
            "false", "null", "undefined", "void", "delete", "with", "debugger"
        )

        val builtins = setOf(
            "console", "window", "document", "Array", "Object", "String", "Number", "Boolean",
            "Date", "Math", "JSON", "Promise", "setTimeout", "setInterval", "parseInt",
            "parseFloat", "isNaN", "isFinite", "encodeURIComponent", "decodeURIComponent"
        )

        return buildAnnotatedString {
            highlightCodeGeneric(
                code = code,
                keywords = keywords,
                builtins = builtins,
                singleLineComment = "//",
                multiLineCommentStart = "/*",
                multiLineCommentEnd = "*/",
                stringDelimiters = setOf("\"", "'", "`")
            )
        }
    }

    @Composable
    private fun highlightCpp(code: String): AnnotatedString {
        val keywords = setOf(
            "auto", "break", "case", "char", "const", "continue", "default", "do", "double",
            "else", "enum", "extern", "float", "for", "goto", "if", "inline", "int", "long",
            "register", "restrict", "return", "short", "signed", "sizeof", "static", "struct",
            "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "class",
            "namespace", "using", "public", "private", "protected", "virtual", "override",
            "final", "template", "typename", "try", "catch", "throw", "new", "delete",
            "this", "nullptr", "true", "false", "bool", "const_cast", "dynamic_cast",
            "reinterpret_cast", "static_cast"
        )

        val builtins = setOf(
            "std", "cout", "cin", "endl", "string", "vector", "map", "set", "list", "queue",
            "stack", "pair", "make_pair", "sort", "find", "begin", "end", "size", "empty",
            "push_back", "pop_back", "insert", "erase", "clear", "iostream", "algorithm",
            "memory", "utility", "functional"
        )

        return buildAnnotatedString {
            highlightCodeGeneric(
                code = code,
                keywords = keywords,
                builtins = builtins,
                singleLineComment = "//",
                multiLineCommentStart = "/*",
                multiLineCommentEnd = "*/",
                stringDelimiters = setOf("\"", "'")
            )
        }
    }

    @Composable
    private fun highlightC(code: String): AnnotatedString {
        val keywords = setOf(
            "auto", "break", "case", "char", "const", "continue", "default", "do", "double",
            "else", "enum", "extern", "float", "for", "goto", "if", "inline", "int", "long",
            "register", "restrict", "return", "short", "signed", "sizeof", "static", "struct",
            "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "_Bool",
            "_Complex", "_Imaginary", "_Alignas", "_Alignof", "_Atomic", "_Static_assert",
            "_Noreturn", "_Thread_local", "_Generic"
        )

        val builtins = setOf(
            "printf", "scanf", "malloc", "free", "calloc", "realloc", "strlen", "strcpy",
            "strcat", "strcmp", "memcpy", "memset", "fopen", "fclose", "fread", "fwrite",
            "fprintf", "fscanf", "getchar", "putchar", "gets", "puts", "atoi", "atof",
            "exit", "rand", "srand", "time", "clock", "sizeof", "NULL", "FILE", "size_t"
        )

        return buildAnnotatedString {
            highlightCodeGeneric(
                code = code,
                keywords = keywords,
                builtins = builtins,
                singleLineComment = "//",
                multiLineCommentStart = "/*",
                multiLineCommentEnd = "*/",
                stringDelimiters = setOf("\"", "'")
            )
        }
    }

    @Composable
    private fun AnnotatedString.Builder.highlightCodeGeneric(
        code: String,
        keywords: Set<String>,
        builtins: Set<String>,
        singleLineComment: String,
        multiLineCommentStart: String,
        multiLineCommentEnd: String,
        stringDelimiters: Set<String>
    ) {
        val keywordColor = MaterialTheme.colorScheme.primary
        val builtinColor = MaterialTheme.colorScheme.secondary
        val commentColor = MaterialTheme.colorScheme.outline
        val stringColor = MaterialTheme.colorScheme.tertiary
        val numberColor = MaterialTheme.colorScheme.error
        val operatorColor = MaterialTheme.colorScheme.onSurfaceVariant

        var i = 0
        while (i < code.length) {
            when {
                // 处理多行注释
                code.startsWith(multiLineCommentStart, i) -> {
                    val endIndex = code.indexOf(multiLineCommentEnd, i + multiLineCommentStart.length)
                    val commentEnd = if (endIndex != -1) endIndex + multiLineCommentEnd.length else code.length
                    withStyle(SpanStyle(color = commentColor, fontStyle = FontStyle.Italic)) {
                        append(code.substring(i, commentEnd))
                    }
                    i = commentEnd
                }

                // 处理单行注释
                code.startsWith(singleLineComment, i) -> {
                    val lineEnd = code.indexOf('\n', i).let { if (it == -1) code.length else it }
                    withStyle(SpanStyle(color = commentColor, fontStyle = FontStyle.Italic)) {
                        append(code.substring(i, lineEnd))
                    }
                    i = lineEnd
                }

                // 处理字符串
                stringDelimiters.any { code.startsWith(it, i) } -> {
                    val delimiter = stringDelimiters.first { code.startsWith(it, i) }
                    val start = i
                    i += delimiter.length
                    while (i < code.length && !code.startsWith(delimiter, i)) {
                        if (code[i] == '\\' && i + 1 < code.length) i += 2 else i++
                    }
                    if (i < code.length) i += delimiter.length
                    withStyle(SpanStyle(color = stringColor)) {
                        append(code.substring(start, i))
                    }
                }

                // 处理数字
                code[i].isDigit() -> {
                    val start = i
                    while (i < code.length && (code[i].isDigit() || code[i] == '.' || code[i] == 'f' || code[i] == 'L')) {
                        i++
                    }
                    withStyle(SpanStyle(color = numberColor, fontWeight = FontWeight.Bold)) {
                        append(code.substring(start, i))
                    }
                }

                // 处理标识符（关键字、内置函数等）
                code[i].isLetter() || code[i] == '_' -> {
                    val start = i
                    while (i < code.length && (code[i].isLetterOrDigit() || code[i] == '_')) {
                        i++
                    }
                    val word = code.substring(start, i)
                    when {
                        keywords.contains(word) -> {
                            withStyle(SpanStyle(color = keywordColor, fontWeight = FontWeight.Bold)) {
                                append(word)
                            }
                        }
                        builtins.contains(word) -> {
                            withStyle(SpanStyle(color = builtinColor, fontWeight = FontWeight.Medium)) {
                                append(word)
                            }
                        }
                        else -> append(word)
                    }
                }

                // 处理操作符
                "+-*/%=<>!&|^~".contains(code[i]) -> {
                    withStyle(SpanStyle(color = operatorColor, fontWeight = FontWeight.Bold)) {
                        append(code[i])
                    }
                    i++
                }

                // 处理括号
                "(){}[]".contains(code[i]) -> {
                    withStyle(SpanStyle(color = operatorColor, fontWeight = FontWeight.Bold)) {
                        append(code[i])
                    }
                    i++
                }

                // 其他字符
                else -> {
                    append(code[i])
                    i++
                }
            }
        }
    }
}
