package com.colombia.credit.popwindow

import androidx.annotation.Keep


@Keep
class PopData {

    constructor()

    constructor(value: String, key: Int) {
        this.selectKey = key
        this.selectValues = value
    }

    constructor(value: String, key: String) {
        this.keyStr = key
        this.selectValues = value
    }

    var selectKey: Int = 0
    var keyStr: String? = null
    var selectValues: String = ""
    var isSelected: Boolean = false
}