package com.kira.learning.module.ocr

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import androidx.lifecycle.lifecycleScope
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.databinding.ActivityShowCropperedBinding
import com.kira.learning.module.ocr.ImageProcessingUtils.MAX_IMAGE_DIMENSION
import com.kira.learning.module.ocr.vm.OcrViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 显示截图结果并进行OCR识别
 */
@AndroidEntryPoint
class ShowCropperedActivity : BaseActivity() {

    // 使用自定义工厂创建ViewModel
    private val viewModel by lazyViewModel<OcrViewModel>()

    // 视图绑定
    private val binding by binding<ActivityShowCropperedBinding>()

    // 当前加载的位图
    private var currentBitmap: Bitmap? = null

    // 进度对话框
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化视图绑定
        setContentView(binding.root)

        // 获取传递的参数
        val width = intent.getIntExtra("width", 0)
        val height = intent.getIntExtra("height", 0)
        val imagePath = intent.getStringExtra("path") ?: run {
            showError("图片路径无效")
            finish()
            return
        }

        // 设置UI
        setupUi(width, height, imagePath)

        // 观察识别状态
        viewModel.recognitionState.observe(this) { state ->
            when (state) {
                is OcrViewModel.RecognitionState.Loading -> showLoading()
                is OcrViewModel.RecognitionState.Success -> handleSuccess(state)
                is OcrViewModel.RecognitionState.Error -> handleError(state)
                OcrViewModel.RecognitionState.Idle -> Unit
            }
        }

        // 开始OCR识别
        startOcrRecognition()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消后台任务
        viewModel.cancelRecognition()

        // 关闭对话框
        progressDialog?.dismiss()

        // 回收位图资源
        currentBitmap?.recycle()
        currentBitmap = null
    }

    private fun setupUi(width: Int, height: Int, imagePath: String) {
        // 计算尺寸并显示图片
        if (width > 0 && height > 0) {
            val screenWidth = resources.displayMetrics.widthPixels
            val scale = screenWidth.toFloat() / width
            val scaledHeight = (height * scale).toInt()

            binding.image.apply {
                layoutParams.height = scaledHeight
                requestLayout()
            }
        }

        // 加载并显示图片
        currentBitmap = loadOptimizedBitmap(imagePath)
        currentBitmap?.let {
            binding.image.setImageBitmap(it)
        } ?: run {
            showError("图片加载失败")
        }
    }

    private fun loadOptimizedBitmap(path: String): Bitmap? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(path, options)

            val maxDim = maxOf(options.outWidth, options.outHeight)
            val sampleSize = if (maxDim > MAX_IMAGE_DIMENSION) {
                (maxDim / MAX_IMAGE_DIMENSION).coerceAtLeast(1)
            } else {
                1
            }

            BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inPreferredConfig = Bitmap.Config.RGB_565
            }.let { opts ->
                BitmapFactory.decodeFile(path, opts)
            }
        } catch (e: Exception) {
            Log.e("ShowCroppered", "位图加载错误", e)
            null
        }
    }

    private fun startOcrRecognition() {
        currentBitmap?.let { bitmap ->
            lifecycleScope.launch {
                // 获取语言包路径
                val languagePath = "${getExternalFilesDir(null)?.absolutePath ?: filesDir.absolutePath}/"
                viewModel.recognizeText(bitmap, languagePath)
            }
        }
    }

    private fun showLoading() {
        progressDialog?.dismiss()
        progressDialog = AlertDialog.Builder(this)
            .setMessage("正在识别...")
            .setCancelable(false)
            .create()
        progressDialog?.show()
    }

    private fun handleSuccess(state: OcrViewModel.RecognitionState.Success) {
        progressDialog?.dismiss()
        state.processedBitmap?.let { processed ->
            binding.image2.setImageBitmap(processed)
            // 更新当前位图
            currentBitmap?.recycle()
            currentBitmap = processed
        }
        binding.text.text = state.recognizedText
    }

    private fun handleError(state: OcrViewModel.RecognitionState.Error) {
        progressDialog?.dismiss()
        showError(state.message)
        binding.text.text = "识别失败"
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e("ShowCroppered", message)
    }
}

/**
 * 图像处理工具类
 */
object ImageProcessingUtils {

    // 最大图像尺寸
    const val MAX_IMAGE_DIMENSION = 3000

    // 二值化阈值
    private const val BINARIZATION_THRESHOLD = 128

    /**
     * OCR图像预处理
     */
    fun preprocessForOcr(original: Bitmap): Bitmap {
        // 1. 调整大小
        val resized = resizeIfNeeded(original)

        // 2. 灰度化
        val gray = convertToGray(resized)

        return gray
        // 3. 二值化
        return binarize(gray, BINARIZATION_THRESHOLD)
    }

    /**
     * 调整图像大小
     */
    fun resizeIfNeeded(bitmap: Bitmap, maxDim: Int = MAX_IMAGE_DIMENSION): Bitmap {
        if (bitmap.width <= maxDim && bitmap.height <= maxDim) {
            return bitmap
        }

        val scale = maxDim / maxOf(bitmap.width, bitmap.height).toFloat()
        val newWidth = (bitmap.width * scale).toInt()
        val newHeight = (bitmap.height * scale).toInt()

        return bitmap.scale(newWidth, newHeight)
    }

    /**
     * 转换为灰度图
     */
    fun convertToGray(bitmap: Bitmap): Bitmap {
        val result = createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(
                ColorMatrix().apply { setSaturation(0f) }
            )
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
//        var colorMatrix: ColorMatrix? = null
//        colorMatrix = ColorMatrix()
//        colorMatrix.setSaturation(0f)
//        val filter = ColorMatrixColorFilter(colorMatrix)
//
//        val paint = Paint()
//        paint.setColorFilter(filter)
//        val result =
//            createBitmap(bitmap.getWidth(), bitmap.getHeight())
//        val canvas = Canvas(result)
//
//        canvas.drawBitmap(bitmap, 0f, 0f, paint)
//        return result
    }

    /**
     * 二值化处理
     */
    /**
     * 二值化处理（完全修正版）
     * @param bitmap 输入位图
     * @param threshold 二值化阈值(0-255)
     * @return 处理后的位图
     */
    fun binarize(bitmap: Bitmap, threshold: Int = 128): Bitmap {
        // 1. 创建目标位图（使用RGB_565节省内存）
        val output = createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        // 2. 创建传统Canvas（替代applyCanvas）
        val canvas = Canvas(output)

        // 3. 获取源位图像素
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        // 4. 处理每个像素
        for (i in pixels.indices) {
            val alpha = pixels[i] ushr 24
            val r = (pixels[i] shr 16) and 0xFF
            val g = (pixels[i] shr 8) and 0xFF
            val b = pixels[i] and 0xFF

            // 计算灰度值（NTSC标准加权）
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()

            // 二值化（保留透明度）
            pixels[i] = if (gray > threshold) {
                (alpha shl 24) or 0x00FFFFFF // 白
            } else {
                (alpha shl 24) or 0x00000000 // 黑
            }
        }

        // 5. 创建临时位图并绘制
        val tempBitmap = createBitmap(bitmap.width, bitmap.height).apply {
            setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        }

        // 6. 绘制到输出（使用单参数lambda）
        canvas.drawBitmap(tempBitmap, 0f, 0f, null)

        // 7. 回收资源
        tempBitmap.recycle()

        return output
    }
}