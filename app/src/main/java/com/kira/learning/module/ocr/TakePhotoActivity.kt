package com.kira.learning.module.ocr

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kira.learning.R
import com.kira.learning.databinding.ActivityTakePhotoBinding
import com.kira.learning.module.ocr.utils.UriUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import java.io.File

/**
 * 拍照界面
 */
class TakePhotoActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var mBinding: ActivityTakePhotoBinding

    /**
     * 旋转文字
     */
    private var isRotated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val decorView = window.decorView
        decorView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                windowSetting()
            }
        })


        super.onCreate(savedInstanceState)
        mBinding = ActivityTakePhotoBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.btnClose.setOnClickListener(onClickListener)
        mBinding.btnShutter.setOnClickListener(onClickListener)
        mBinding.btnAlbum.setOnClickListener(onClickListener)

        startCamera()
    }

    /**
     * 兼容新老版本
     */
    private fun windowSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (isTransverse) {
            if (!isRotated) {
                val tvHint = findViewById<TextView?>(R.id.hint)
                val animator = ObjectAnimator.ofFloat(tvHint, "rotation", 0f, 90f)
                animator.startDelay = 800
                animator.duration = 500
                animator.interpolator = LinearInterpolator()
                animator.start()

                val btnShutter = findViewById<ImageView?>(R.id.btn_shutter)
                val animator1 = ObjectAnimator.ofFloat(btnShutter, "rotation", 0f, 90f)
                animator1.startDelay = 800
                animator1.duration = 500
                animator1.interpolator = LinearInterpolator()
                animator1.start()

                val animator3 = ObjectAnimator.ofFloat(mBinding.btnAlbum, "rotation", 0f, 90f)
                animator3.startDelay = 800
                animator3.duration = 500
                animator3.interpolator = LinearInterpolator()
                animator3.start()
                isRotated = true
            }
        }
    }

    /**
     * 开始预览
     */
    private fun startCamera() {
        // 使用生命周期作用域管理协程，自动匹配Activity生命周期
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // 1. 将ListenableFuture转换为协程挂起函数（需要kotlinx-coroutines-guava依赖）
                val processCameraProvider: ProcessCameraProvider =
                    ProcessCameraProvider.getInstance(this@TakePhotoActivity).await()

                // 2. 创建Preview实例（保持原有配置）
                val preview = Preview.Builder()
                    .setTargetRotation(Surface.ROTATION_90)
                    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                    .build()
                    .apply {
                        setSurfaceProvider(mBinding.preview.getSurfaceProvider())
                    }

                // 3. 选择后置摄像头（与原逻辑一致）
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // 4. 创建ImageCapture实例（保持原有配置）
                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(Surface.ROTATION_90)
                    .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                    .build()

                // 5. 解绑之前的用例（避免重复绑定）
                processCameraProvider.unbindAll()

                // 6. 绑定用例到生命周期（与原逻辑一致）
                processCameraProvider.bindToLifecycle(
                    this@TakePhotoActivity,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                // 保持原异常处理逻辑（可根据需要添加日志）
                e.printStackTrace()
            }
        }
    }

    private fun takePhoto() {
        if (imageCapture != null) {
            // 创建带时间戳的输出文件以保存图片，带时间戳是为了保证文件名唯一
            val photoFile = File(cacheDir, "/" + System.currentTimeMillis() + ".jpg")

            // 创建 output option 对象，用以指定照片的输出方式
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            // 执行takePicture（拍照）方法
            imageCapture!!.takePicture(
                outputFileOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    // 保存照片时的回调
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        launchActivity(savedUri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                    }
                })
        }
    }

    /**
     * 拍照界面
     */
    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.btn_close -> finish()
                R.id.btn_shutter -> takePhoto()
                R.id.btn_album -> {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    /**
     * 获取图片回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val uri = data?.data
            Log.e("uri", uri.toString())
            launchActivity(uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun launchActivity(uri: Uri?) {
        val path: String? = UriUtils.getFileFromUri(this, uri)
        if (path == null) {
            Toast.makeText(this, "文件已损坏", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, CutOutPhotoActivity::class.java)
        intent.putExtra("path", path)
        startActivity(intent)
    }

    companion object {
        var isTransverse: Boolean = true

        private const val TAG = "TakePhotoActivity"
        val IMAGE_URI: Uri? = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
}
