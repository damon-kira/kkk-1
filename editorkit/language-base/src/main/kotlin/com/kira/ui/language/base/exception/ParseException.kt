

package com.kira.ui.language.base.exception

class ParseException(
    message: String?,
    val lineNumber: Int,
    val columnNumber: Int
) : RuntimeException(message)