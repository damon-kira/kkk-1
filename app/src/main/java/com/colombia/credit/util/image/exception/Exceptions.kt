package com.colombia.credit.util.image.exception

import java.lang.RuntimeException

open class BaseException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message:String):this(message,null)
}
