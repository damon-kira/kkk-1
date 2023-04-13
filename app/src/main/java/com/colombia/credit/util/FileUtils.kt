package com.colombia.credit.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object FileUtils {

    @JvmStatic
    @SuppressLint("CheckResult")
    fun readAssets(context: Context, fileName: String, result: (result: String) -> Unit) {
        Flowable.fromPublisher<String> {
            try {
                val inputStream = context.assets.open(fileName)
                val result = StringBuilder()
                BufferedReader(InputStreamReader(inputStream)).use { br ->
                    var line = br.readLine()
                    while (line != null) {
                        result.append(line)
                        line = br.readLine()
                    }
                    it.onNext(result.toString())
                    it.onComplete()
                }
            } catch (e: Exception) {
                it.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                result.invoke(it)
            }, {
            })
    }

    @JvmStatic
    fun createUriFromFile(context: Context, file: File): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.applicationInfo.packageName}.provider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }
}