package com.colombia.credit.module.ocr

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.common.lib.base.BaseFragmentActivity
import com.googlecode.tesseract.android.TessBaseAPI
import com.colombia.credit.R
import com.colombia.credit.module.ocr.utils.Utils
import com.common.lib.base.BaseActivity

/**
 * 显示截图结果
 * 并识别
 */
class ShowCropperedActivity : BaseActivity() {
    private var context: Context? = null
    private var imageView: ImageView? = null
    private var imageView2: ImageView? = null
    private var textView: TextView? = null

    private var width = 0
    private var height = 0
    private var path: String? = null
    private var result: String? = null

    private val baseApi: TessBaseAPI = TessBaseAPI()
    private val handler = Handler()
    private var dialog: ProgressDialog? = null

    private var colorMatrix: ColorMatrix? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_croppered)
        context = this
        LANGUAGE_PATH = getExternalFilesDir("").toString() + "/"
        Log.e("---------", ShowCropperedActivity.Companion.LANGUAGE_PATH)

        width = getIntent().getIntExtra("width", 0)
        height = getIntent().getIntExtra("height", 0)
        path = getIntent().getStringExtra("path")

        initView()
        initTess()
    }

//    override fun onResume() {
//        super.onResume()
//        dialog = ProgressDialog(context)
//        dialog!!.setMessage("正在识别...")
//        dialog!!.setCancelable(false)
//        dialog!!.show()
//    }

    private fun initView() {
        imageView = findViewById<ImageView>(R.id.image)
        imageView2 = findViewById<ImageView>(R.id.image2)
        textView = findViewById<TextView>(R.id.text)

        if (width != 0 && height != 0) {
            val screenWidth: Int = Utils.getWidthInPx(this)
            val scale = screenWidth.toFloat() / width.toFloat()
            val lp = imageView!!.getLayoutParams()
            val imgHeight = (scale * height).toInt()
            lp.height = imgHeight
            imageView!!.setLayoutParams(lp)
            Log.e(
                ShowCropperedActivity.Companion.TAG,
                "imageView.getLayoutParams().width:" + imageView!!.getLayoutParams().width
            )
        }
        imageView!!.setImageURI(Uri.parse(path))
    }

    private fun initTess() {
        //字典库
        baseApi.init(
            LANGUAGE_PATH, LANGUAGE
        )
        //设置设别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO)
        val myThread = Thread(runnable)
        dialog = ProgressDialog(context)
        dialog!!.setMessage("正在识别...")
        dialog!!.setCancelable(false)
        dialog!!.show()
        myThread.start()
    }


    /**
     * 灰度化处理
     */
    fun convertGray(bitmap3: Bitmap): Bitmap {
        colorMatrix = ColorMatrix()
        colorMatrix!!.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix!!)

        val paint = Paint()
        paint.setColorFilter(filter)
        val result =
            Bitmap.createBitmap(bitmap3.getWidth(), bitmap3.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        canvas.drawBitmap(bitmap3, 0f, 0f, paint)
        return result
    }

    /**
     * 二值化
     *
     * @param tmp 二值化阈值 默认100
     */
    private fun binaryzation(bitmap22: Bitmap, tmp: Int): Bitmap {
        // 获取图片的宽和高
        val width = bitmap22.getWidth()
        val height = bitmap22.getHeight()
        // 创建二值化图像
        val bitmap: Bitmap
        bitmap = bitmap22.copy(Bitmap.Config.ARGB_8888, true)
        // 遍历原始图像像素,并进行二值化处理
        for (i in 0 until width) {
            for (j in 0 until height) {
                // 得到当前的像素值
                val pixel = bitmap.getPixel(i, j)
                // 得到Alpha通道的值
                val alpha = pixel and -0x1000000
                // 得到Red的值
                var red = (pixel and 0x00FF0000) shr 16
                // 得到Green的值
                var green = (pixel and 0x0000FF00) shr 8
                // 得到Blue的值
                var blue = pixel and 0x000000FF

                if (red > tmp) {
                    red = 255
                } else {
                    red = 0
                }
                if (blue > tmp) {
                    blue = 255
                } else {
                    blue = 0
                }
                if (green > tmp) {
                    green = 255
                } else {
                    green = 0
                }

                // 通过加权平均算法,计算出最佳像素值
                var gray =
                    (red.toFloat() * 0.3 + green.toFloat() * 0.59 + blue.toFloat() * 0.11).toInt()
                // 对图像设置黑白图
                if (gray <= 95) {
                    gray = 0
                } else {
                    gray = 255
                }
                // 得到新的像素值
                val newPiexl = alpha or (gray shl 16) or (gray shl 8) or gray
                // 赋予新图像的像素
                bitmap.setPixel(i, j, newPiexl)
            }
        }
        return bitmap
    }

    /**
     * 识别线程
     */
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            val bitmap_1 = convertGray(BitmapFactory.decodeFile(path))

            baseApi.setImage(bitmap_1)
            result = baseApi.getUTF8Text()
            baseApi.recycle()

            handler.post(object : Runnable {
                override fun run() {
                    imageView2!!.setImageBitmap(bitmap_1)
                    textView!!.setText(result)
                    dialog!!.dismiss()
                }
            })
        }
    }

    companion object {
        //sd卡路径
        private var LANGUAGE_PATH = ""

        //识别语言
        private const val LANGUAGE = "chi_sim" //chi_sim | eng
        private const val TAG = "ShowCropperedActivity"
    }
}
