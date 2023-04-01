package com.colombia.credit.util.image.operator.exception

import java.lang.RuntimeException

open class OptException(msg: String) : RuntimeException(msg) {

    override fun fillInStackTrace(): Throwable {
        return this
    }
}

class NoDataException : OptException("No data")


class QuantityOverflowException : OptException("Too many files selected")

class OversizeException : OptException("The selected file is too large")