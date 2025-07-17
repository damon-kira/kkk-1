

package com.kira.ui.filesystem.base.model

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class FileParams(
    val chardet: Boolean = false,
    val charset: Charset = StandardCharsets.UTF_8,
    val linebreak: LineBreak = LineBreak.LF,
)