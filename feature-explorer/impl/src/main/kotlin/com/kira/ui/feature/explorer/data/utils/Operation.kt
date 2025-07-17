

package com.kira.ui.feature.explorer.data.utils

enum class Operation(val value: Int) {
    CREATE(0),
    RENAME(1),
    DELETE(2),
    CUT(3),
    COPY(4),
    COMPRESS(5),
    EXTRACT(6);

    companion object {

        fun of(value: Int): Operation {
            return values().find { it.value == value } ?: CREATE
        }
    }
}