package com.colombia.credit.bean


abstract class SearchInfo {

    var tag: String? = ""

    abstract fun match(constraint: CharSequence?):Boolean
}