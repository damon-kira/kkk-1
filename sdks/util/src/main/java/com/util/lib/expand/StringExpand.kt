package com.util.lib.expand

/**
 * Created by weishl on 2022/11/24
 *
 */

fun isEmpty(str: String?): Boolean {
    return str == null || str.isEmpty()
}

fun isNotEmpty(str: String?): Boolean {
    return str != null && str.isNotEmpty()
}