package com.colombia.credit.view.email

import androidx.annotation.Keep


@Keep
class EmailInputBean {

    constructor()

    constructor(inputText: String?, email: String?) {
        this.inputText = inputText
        this.email = email
    }

    var inputText: String? = null
    var email: String? = null
}