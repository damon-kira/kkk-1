

package com.kira.ui.filesystem.base.model

enum class AuthMethod(val value: Int) {
    PASSWORD(0),
    KEY(1);

    companion object {

        fun of(value: Int): AuthMethod {
            return checkNotNull(values().find { it.value == value })
        }
    }
}