

package com.kira.ui.filesystem.base.utils

fun String.isValidFileName(): Boolean {
    return isNotBlank() && !contains("/") && !equals(".") && !equals("..")
}

fun String.endsWith(suffixes: Array<String>, ignoreCase: Boolean = true): Boolean {
    for (suffix in suffixes) {
        if (endsWith(suffix, ignoreCase)) {
            return true
        }
    }
    return false
}