package com.common.kira.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics

fun Modifier.clearSemantics(): Modifier {
    return this.clearAndSetSemantics { /* no-op */ }
}

fun Modifier.mergeSemantics(): Modifier {
    return this.semantics(mergeDescendants = true) { /* no-op */ }
}