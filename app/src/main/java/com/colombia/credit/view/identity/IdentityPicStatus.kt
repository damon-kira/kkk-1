package com.colombia.credit.view.identity

import androidx.annotation.IntDef
import com.colombia.credit.view.identity.IdentityPicStatus.Companion.STATUS_ERROR
import com.colombia.credit.view.identity.IdentityPicStatus.Companion.STATUS_NORMAL
import com.colombia.credit.view.identity.IdentityPicStatus.Companion.STATUS_SUCCESS

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@IntDef(STATUS_NORMAL, STATUS_ERROR, STATUS_SUCCESS)
annotation class IdentityPicStatus {
    companion object {
        const val STATUS_NORMAL = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
    }
}