package com.colombia.credit.view.baseinfo

import androidx.annotation.IntDef

@IntDef(
    BaseInfoInputType.INPUT_NORMAL,
    BaseInfoInputType.INPUT_NUMBER,
    BaseInfoInputType.INPUT_EMAIL,
    BaseInfoInputType.INPUT_PWD
)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class BaseInfoInputType {
    companion object {
        const val INPUT_NORMAL = -1 // 默认英文
        const val INPUT_NUMBER = 0 //输入数字
        const val INPUT_EMAIL = 1 //输入邮箱地址
        const val INPUT_PWD = 2 // 输入密码
    }
}