package com.kira.learning.utils.image.exception

open class BaseException(message: String, cause: Throwable?) : RuntimeException(message, cause) {
    constructor(message:String):this(message,null)
}
