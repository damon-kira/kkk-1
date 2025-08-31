package com.kira.learning.xml.modules.ocr.vm

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.common.lib.base.BaseViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kira.learning.xml.modules.ocr.ImageProcessingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

/**
 * OCR识别视图模型
 */
class OcrViewModel @Inject constructor(
) : BaseViewModel(), LifecycleEventObserver {

    sealed class RecognitionState {
        object Idle : RecognitionState()
        object Loading : RecognitionState()
        data class Success(val recognizedText: String, val processedBitmap: Bitmap?) :
            RecognitionState()

        data class Error(val message: String) : RecognitionState()
    }

    // 识别状态
    val recognitionState =
        MutableLiveData<RecognitionState>(RecognitionState.Idle)

    // Tesseract实例
//    private var mTessApi: TessBaseAPI? = null

    // 是否取消任务
    private var isCancelled = false

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // 开始识别文本
    suspend fun recognizeText(bitmap: Bitmap, languagePath: String) {
        // 重置取消状态
        isCancelled = false

        // 更新为加载状态
        recognitionState.postValue(RecognitionState.Loading)

        try {
            val result = withContext(Dispatchers.Default) {
                if (isCancelled) return@withContext null

                // 1. 确保语言包存在
                val language = "chi_sim" // 英文：eng

                // 2. 初始化Tesseract
//                val tessApi = TessBaseAPI().apply {
//                    if (!init(languagePath, language)) {
//                        throw IllegalStateException("Tesseract初始化失败")
//                    }
                    // 性能参数
//                    setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO)
//                    setVariable(
//                        TessBaseAPI.VAR_CHAR_WHITELIST,
//                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,!?':;\"-()[]{}"
//                    )
//
//                    // 优化文本布局分析
//                    setVariable("textord_tabfind_force_vertical_text", "F")
//                    setVariable("textord_noise_rejwords", "F")
//                    setVariable("tessedit_enable_doc_dict", "0") // 禁用文档字典（防止错误修正）
//
//                    // 提升数字和小写字母识别
//                    setVariable("tessedit_char_blacklist", "@#$%^&*_+=|<>~`")
//                    setVariable("classify_bln_numeric_mode", "1") // 数字模式
//
//                    // 配置训练文件参数
//                    setVariable("load_system_dawg", "0") // 关闭系统词典
//                    setVariable("load_freq_dawg", "1")  // 开启常用词词典
//                }


                // 保存实例
//                mTessApi = tessApi

                // 3. 预处理图像
                val processed = ImageProcessingUtils.preprocessForOcr(bitmap)

                // 4. 设置图像并识别
//                if (isCancelled) return@withContext null
//                tessApi.setImage(processed)
//                val text = tessApi.utF8Text?.trim() ?: ""

                // Google mlkit处理
                var text = processImageWithCoroutine(processed)


                return@withContext Pair(text, processed)
            }

            // 处理识别结果
            if (result != null) {
                recognitionState.postValue(RecognitionState.Success(result.first, result.second))
            } else {
                recognitionState.postValue(RecognitionState.Error("识别任务已取消"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "OCR识别失败: ${e.message}")
            recognitionState.postValue(RecognitionState.Error("识别失败: ${e.message}"))
        }
    }

    private suspend fun processImageWithCoroutine(photoBitmap: Bitmap): String =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                try {
                    val image = InputImage.fromBitmap(photoBitmap, 0)

                    val recognizer =
                        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    recognizer.process(image)
                        .addOnSuccessListener { scannedText ->
                            continuation.resume(scannedText.text) { cause -> }
                        }
                        .addOnFailureListener { e ->
                            continuation.resumeWithException(e)
                        }
                        .addOnCanceledListener {
                            continuation.cancel(CancellationException("Text recognition cancelled"))
                        }

                    // 处理协程取消
                    continuation.invokeOnCancellation {
                        recognizer.close()
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        }

    // 取消识别
    fun cancelRecognition() {
        isCancelled = true
//        mTessApi?.recycle()
//        mTessApi = null
    }

    override fun onCleared() {
        super.onCleared()
        cancelRecognition()
    }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)

        }
    }
}