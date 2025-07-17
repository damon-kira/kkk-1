

package com.kira.ui.feature.settings.data.utils

import android.content.Context
import android.content.pm.PackageManager

val Context.applicationName: String
    get() = try {
        applicationInfo.loadLabel(packageManager).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

val Context.versionName: String
    get() = try {
        packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

val Context.versionCode: Int
    get() = try {
        packageManager.getPackageInfo(packageName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        -1
    }