package com.kira.learning.util.image.exception

open class BaseException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message:String):this(message,null)
}
