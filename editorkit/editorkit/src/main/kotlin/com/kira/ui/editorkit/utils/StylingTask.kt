

package com.kira.ui.editorkit.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.kira.ui.language.base.model.SyntaxHighlightResult
import java.util.concurrent.Executors

class StylingTask(
    private val doAsync: () -> List<SyntaxHighlightResult>,
    private val onSuccess: (List<SyntaxHighlightResult>) -> Unit,
) {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    fun execute() {
        singleThreadExecutor.execute {
            try {
                val syntaxHighlightSpans = doAsync()
                mainThreadHandler.post {
                    if (!singleThreadExecutor.isShutdown) {
                        onSuccess(syntaxHighlightSpans)
                    }
                }
            } catch (e: Throwable) {
                Log.e(TAG, e.message, e)
            }
        }
    }

    fun cancel() {
        singleThreadExecutor.shutdown()
    }

    companion object {
        private const val TAG = "StylingTask"
    }
}