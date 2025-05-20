package com.kira.learning.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object FileUtils {

    @JvmStatic
    @SuppressLint("CheckResult")
    fun readAssets(context: Context, fileName: String, result: (result: String) -> Unit) {
        Flowable.fromPublisher<String> { emitter ->
            try {
                val inputStream = context.assets.open(fileName)
                val stringBuilder = StringBuilder()
                BufferedReader(InputStreamReader(inputStream)).use { br ->
                    var line = br.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = br.readLine()
                    }
                    emitter.onNext(stringBuilder.toString())
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ content ->
                result.invoke(content)
            }, { error ->
                // Optional: Add error handling here if needed
                // result.invoke("") or log the error
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