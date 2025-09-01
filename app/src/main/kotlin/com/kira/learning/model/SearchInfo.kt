package com.kira.learning.model

abstract class SearchInfo {

    abstract fun match(constraint: CharSequence?): Boolean

    abstract fun getTag(): String

    abstract fun isHot(): Boolean
}