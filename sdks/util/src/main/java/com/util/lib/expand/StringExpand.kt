package com.util.lib.expand

fun isEmpty(str: String?): Boolean {
    return str == null || str.isEmpty()
}

fun isNotEmpty(str: String?): Boolean {
    return str != null && str.isNotEmpty()
}