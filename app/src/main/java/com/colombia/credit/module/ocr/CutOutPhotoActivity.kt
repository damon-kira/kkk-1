package com.colombia.credit.module.ocr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.colombia.credit.databinding.ActivityCutoutPhoteBinding
import com.common.lib.base.BaseFragmentActivity
import com.common.lib.viewbinding.binding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * 裁剪
 */
class CutOutPhotoActivity : BaseFragmentActivity() {

        private val mBinding by binding<ActivityCutoutPhoteBinding>()
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        mBinding
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cutout_phote);
        val path = getIntent().getStringExtra("path")
        val bitmap = BitmapFactory.decodeFile(path)

        mBinding.cropImageView.setImageBitmap(bitmap)

        mBinding.btnClosecropper.setOnClickListener({ view -> finish() })

        mBinding.btnStartcropper.setOnClickListener({ view ->
            val cropperBitmap: Bitmap = mBinding.cropImageView.getCroppedImage()
            // 图像名称
            val path1 = getCacheDir().getPath()
            val resultPath = saveImage(path1, cropperBitmap)

            val intent = Intent(this@CutOutPhotoActivity, ShowCropperedActivity::class.java)
            intent.putExtra("path", resultPath)
            intent.putExtra("width", cropperBitmap.getWidth())
            intent.putExtra("height", cropperBitmap.getHeight())
            startActivity(intent)
            cropperBitmap.recycle()
            finish()
        })
    }

    /**
     * 存储图像
     */
    private fun saveImage(path: String, source: Bitmap?): String {
        var outputStream: OutputStream? = null
        var file: File
        try {
            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            file = File(path, System.currentTimeMillis().toString() + ".jpg")
            if (file.createNewFile()) {
                outputStream = FileOutputStream(file)
                source?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        } catch (e: IOException) {
            return ""
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (t: Throwable) {
                }
            }
        }

        return file.path
    }

    public override fun getFragmentViewId(): Int {
        return 0
    }
}
