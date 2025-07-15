package com.kira.learning.module.quiz

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.common.lib.glide.GlideUtils
import com.common.lib.net.bean.KiraImage
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.FragmentActivitiesImageBinding
import com.kira.learning.module.quiz.vm.QuizViewModel
import com.util.lib.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageProcessingFragment : BaseFunctionFragment() {
    override val moduleId: String by lazy {
        arguments?.getString("moduleId", "-1") ?: "-1"
    }
    override val moduleName: String = "image_processing"

    private var processedImage: Bitmap? = null
    private val mBinding by binding(FragmentActivitiesImageBinding::inflate)
    internal val activityViewModel by lazyActivityViewModel<QuizViewModel>()
    private var mData: KiraImage? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (activityViewModel.mapColumnContents[moduleId] is KiraImage) {
            mData = activityViewModel.mapColumnContents[moduleId] as KiraImage
        }
        if (null == mData) return TextView(context).apply {
            text = "非图片类型Fragment"
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlideUtils.loadCornerImageFromUrl(
            view.context,
            mData?.attrs?.src ?: "",
            mBinding.imageView,
            4.dp(),
            R.drawable.ic_normal_image
        )
        mBinding.processButton.setOnClickListener {
            processImage()
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

    companion object {
        fun newInstance(id: String): ImageProcessingFragment {
            return ImageProcessingFragment().apply {
                arguments = Bundle().apply {
                    putString("moduleId", id)
                }
            }
        }
    }
}