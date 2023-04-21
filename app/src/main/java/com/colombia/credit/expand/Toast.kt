package com.colombia.credit.expand

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.colombia.credit.app.AppEnv
import com.colombia.credit.app.getAppContext
import com.colombia.credit.databinding.LayoutToastViewBinding
import com.common.lib.toast.ToastCompat
import com.util.lib.log.logger_e

private fun toast(
    message: CharSequence, @DrawableRes drawableLeft: Int = 0, @DrawableRes drawableTop: Int = 0,
    @DrawableRes drawableRight: Int = 0, @DrawableRes drawableBottom: Int = 0,
    duration: Int = Toast.LENGTH_SHORT
) {
    // try-catch解决在8.0版本以下NotificationManager可能取消Token,
    // 因此尝试添加View到Window的时候可能会出问题，只能通过Try-Catch解决(8.0以及以上版本已经解决和这个问题)
    try {
        val toast = ToastCompat.makeText(getAppContext(), message, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = duration
        val ctx = getAppContext()
        val inflate = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = LayoutToastViewBinding.inflate(inflate, null, false)
//        val view = LayoutInflater.from(getAppContext())
//            .inflate(R.layout.layout_toast_view, null, false)
        binding.tvToastText.text = message
        binding.tvToastText.setCompoundDrawablesWithIntrinsicBounds(
            drawableLeft,
            drawableTop,
            drawableRight,
            drawableBottom
        )
        toast.view = binding.root
        toast.show()
    } catch (e: Exception) {
        // Nothing to do
        if (AppEnv.DEBUG) {
            logger_e("debug_toast", "exception = $e")
        }
    }
}

fun toast(
    @StringRes stringId: Int,
    @DrawableRes icon: Int = 0,
    duration: Int = Toast.LENGTH_SHORT
) {
    // try-catch解决在8.0版本以下NotificationManager可能取消Token,
    // 因此尝试添加View到Window的时候可能会出问题，只能通过Try-Catch解决(8.0以及以上版本已经解决和这个问题)
    try {
        toast(getAppContext().getString(stringId), icon, duration)
    } catch (e: Exception) {
        // Nothing to do
    }
}

fun toast(str: String, @DrawableRes icon: Int = 0) {
    // try-catch解决在8.0版本以下NotificationManager可能取消Token,
    // 因此尝试添加View到Window的时候可能会出问题，只能通过Try-Catch解决(8.0以及以上版本已经解决和这个问题)
    try {
        toast(str, icon, Toast.LENGTH_SHORT)
    } catch (e: Exception) {
        // Nothing to do
    }
}

fun String.toast(){
    toast(this)
}