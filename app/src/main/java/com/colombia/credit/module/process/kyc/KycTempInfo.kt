package com.colombia.credit.module.process.kyc

import android.os.SystemClock
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.R
import com.util.lib.expand.deleteFiles
import com.util.lib.expand.getCameraCache
import com.util.lib.expand.getTempPhotoSavePath
import java.io.File
import java.lang.StringBuilder


@Keep
class KycTempInfo {
    val pathFront: String
        get() = generatorPath("front.png")
    val pathBack: String
        get() = generatorPath("back.png")

    var resFrontDrawable: Int = R.drawable.image_front_hint
    var resBackDrawable: Int = R.drawable.image_back_hint


    fun setNormalDrawableRes(@DrawableRes idRes: Int, @DrawableRes withRes: Int) {
        resFrontDrawable = idRes
        resBackDrawable = withRes
    }

    fun frontPathCheck(): Boolean {
        val file = File(pathFront)
        return file.exists() && file.length() > 0
    }

    fun backPathCheck(): Boolean {
        val file = File(pathBack)
        return file.exists() && file.length() > 0
    }

    fun getTempPath(): String {
        return getTempPhotoSavePath(getAppContext(), "${SystemClock.currentThreadTimeMillis()}.jpg")
    }

    private fun generatorPath(fileName: String): String {
        return StringBuilder(getCameraCache(getAppContext()).absolutePath).append(File.separator)
            .append(fileName).append(".jpg").toString()
    }


    private fun getLeftImagePath(): String {
        return pathFront
    }

    private fun getRightImagePath(): String {
        return pathBack
    }

    fun deleteImage() {
        val leftPath = getLeftImagePath()
        val rightPath = getRightImagePath()
        deleteFiles(leftPath)
        deleteFiles(rightPath)
    }

    override fun toString(): String {
        return "IdentityInfo(pathId='$pathFront', pathWith='$pathBack', resIdDrawable=${resFrontDrawable}Drawable, resWithDrawable=$resBackDrawable')"
    }


}