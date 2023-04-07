package com.colombia.credit.expand

import android.os.Build


/**
 * 是否是小米手机
 */
inline fun isXiaomi(): Boolean {
    return Build.BRAND.toUpperCase() == "XIAOMI"
}
