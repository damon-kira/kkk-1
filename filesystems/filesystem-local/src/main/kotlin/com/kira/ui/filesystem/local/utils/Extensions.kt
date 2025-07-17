

package com.kira.ui.filesystem.local.utils.utils

import java.io.File

internal fun File.size(): Long {
    if (isDirectory) {
        var length = 0L
        for (child in listFiles().orEmpty()) {
            length += child.size()
        }
        return length
    }
    return length()
}