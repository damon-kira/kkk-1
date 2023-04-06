package com.colombia.credit.module.process.kyc

import androidx.annotation.IntDef

@IntDef(ModeType.MODE_ALBUM, ModeType.MODE_CAMERA, ModeType.TYPE_NULL)
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModeType {
    companion object {
        const val TYPE_NULL = 10
        const val MODE_CAMERA = 11
        const val MODE_ALBUM = 12
    }
}