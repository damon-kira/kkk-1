package com.kira.learning.module.quiz

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.kira.learning.R
import java.util.UUID

class ImageProcessingFragment : BaseFunctionFragment() {
    override val moduleId: String = UUID.randomUUID().toString()
    override val moduleName: String = "image_processing"

    private lateinit var imageView: ImageView
    private var processedImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_activities_image, container, false).apply {
            imageView = findViewById(R.id.image_view)
            findViewById<Button>(R.id.process_button).setOnClickListener {
                processImage()
            }
        }
    }

    private fun processImage() {
        // 图像处理逻辑
//        processedImage = processImageInternal()

        // 提交部分结果
        val result = Bundle().apply {
            putString("status", "processed")
            putInt("width", processedImage?.width ?: 0)
            putInt("height", processedImage?.height ?: 0)
        }
        resultListener?.onPartialResult(moduleId, result)
    }

    override fun collectResult(): Bundle {
        return Bundle().apply {
            putParcelable("processed_image", processedImage)
            putLong("timestamp", System.currentTimeMillis())
        }
    }

    override fun onMessageReceived(senderId: String, message: Bundle) {
        when (message.getString("action")) {
            "update_image" -> {
                val imageUri = message.getString("image_uri")
//                loadImage(imageUri)
            }
        }
    }
}