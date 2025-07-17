

package com.kira.ui.language.base.model

data class Suggestion(
    val type: Type,
    val text: CharSequence,
    val returnType: String
) {

    override fun toString() = text.toString()

    enum class Type(val value: String) {
        FIELD("v"),
        METHOD("m"),
        WORD("w"),
        NONE("none")
    }
}