package com.colombia.credit.module.ocr

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.colombia.credit.camera.CaptureActivity
import com.colombia.credit.databinding.ActivityPhotographBinding
import com.common.lib.base.BaseFragmentActivity
import com.common.lib.viewbinding.binding
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PhotographActivity : BaseFragmentActivity() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val mBinding by binding<ActivityPhotographBinding>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setOnClick()

        Thread(object : Runnable {
            override fun run() {
                deepFile("tessdata")
            }
        }).start()
    }

    private fun setOnClick() {
        mBinding.btnCamera.setOnClickListener {
            checkSelfPermission()
            //google分析
//           val bundle = Bundle()
//           bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "main")
//           bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "拍照")
//           bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Action")
//           mFirebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
    }

    /**
     * 将assets中的文件复制出
     *
     * @param path
     */
    fun deepFile(path: String) {
        var path = path
        val newPath = getExternalFilesDir(null).toString() + File.separator
        try {
            val str = assets.list(path)
            if (str!!.size > 0) { //如果是目录
                val file = File(newPath + path)
                file.mkdirs()
                for (string in str) {
                    path = path + File.separator + string
                    deepFile(path)
                    path = path.substring(0, path.lastIndexOf(File.separator)) //回到原来的path
                }
            } else { //如果是文件
                val `is` = assets.open(path)
                val fos = FileOutputStream(File(newPath + path))
                val buffer = ByteArray(1024)
                while (true) {
                    val len = `is`.read(buffer)
                    if (len == -1) {
                        break
                    }
                    fos.write(buffer, 0, len)
                }
                `is`.close()
                fos.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, TakePhotoActivity::class.java)
//                intent.putExtra(
//                    CaptureActivity.KEY_CAPTURE_IMAGE_PATH,
//                    getCacheFile(this@PhotographActivity).absolutePath + File.separator + "11223.jpg"
//                )
                startActivity(intent)
            } else {
                Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 检查权限
     */
    fun checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(
                this, PERMISSION_CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(PERMISSION_CAMERA), PERMISSIONS_REQUEST_CAMERA
            )
        } else {

            val intent = Intent(this, TakePhotoActivity::class.java).apply {
                putExtra(CaptureActivity.KEY_PICTURE_TYPE, "*/*")
            }
//            val intent = Intent(this, CaptureActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getFragmentViewId(): Int {
        return R.id.activity_photograph
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CAMERA = 454

        const val PERMISSION_CAMERA: String = Manifest.permission.CAMERA
    }
}
