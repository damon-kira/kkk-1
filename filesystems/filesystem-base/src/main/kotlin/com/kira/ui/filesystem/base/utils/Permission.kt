

package com.kira.ui.filesystem.base.utils

infix fun Int.hasFlag(flag: Int): Boolean = this and flag != 0
infix fun Int.plusFlag(flag: Int): Int = this or flag
infix fun Int.minusFlag(flag: Int): Int = this and flag.inv()