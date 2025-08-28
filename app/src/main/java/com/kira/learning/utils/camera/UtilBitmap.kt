package com.kira.learning.utils.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import kotlin.math.roundToInt
import androidx.core.graphics.scale


class UtilBitmap {
    /**
     * 图片缩放比例
     */
    private val BITMAP_SCALE = 0.2f

    /**
     * 将Drawable对象转化为Bitmap对象
     *
     * @param drawable Drawable对象
     * @return 对应的Bitmap对象
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        val bitmap: Bitmap?

        //如果本身就是BitmapDrawable类型 直接转换即可
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }

        //取得Drawable固有宽高
        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            //创建一个1x1像素的单位色图
            bitmap = createBitmap(1, 1)
        } else {
            //直接设置一下宽高和ARGB
            bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        }

        //重新绘制Bitmap
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    /**
     * 模糊ImageView
     *
     * @param context
     * @param img     ImageView
     * @param level   模糊等级【0 ~ 25之间】
     */
    fun blurImageView(context: Context?, img: ImageView, level: Float) {
        // 将图片处理成模糊
        val bitmap = blurBitmap(context, drawableToBitmap(img.getDrawable())!!, level)
        if (bitmap != null) {
            img.setImageBitmap(bitmap)
        }
    }

    /**
     * 模糊ImageView
     *
     * @param context
     * @param img     ImageView
     * @param level   模糊等级【0 ~ 25之间】
     * @param color   为ImageView蒙上一层颜色
     */
    fun blurImageView(context: Context, img: ImageView, level: Float, color: Int) {
        // 将图片处理成模糊
        val bitmap = blurBitmap(context, drawableToBitmap(img.getDrawable())!!, level)
        if (bitmap != null) {
            val drawable = coverColor(context, bitmap, color)
            img.setScaleType(ImageView.ScaleType.FIT_XY)
            img.setImageDrawable(drawable)
        } else {
            img.setImageBitmap(null)
            img.setBackgroundColor(color)
        }
    }

    /**
     * 将bitmap转成蒙上颜色的Drawable
     *
     * @param context
     * @param bitmap
     * @param color   要蒙上的颜色
     * @return Drawable
     */
    fun coverColor(context: Context, bitmap: Bitmap, color: Int): Drawable {
        val paint = Paint()
        paint.setColor(color)
        val rect = RectF(0f, 0f, bitmap.getWidth().toFloat(), bitmap.getHeight().toFloat())
        Canvas(bitmap).drawRoundRect(rect, 0f, 0f, paint)
        return bitmap.toDrawable(context.resources)
    }


    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param bitmap  需要模糊的图片
     * @return 模糊处理后的图片
     */
    fun blurBitmap(context: Context?, bitmap: Bitmap, blurRadius: Float): Bitmap? {
        var blurRadius = blurRadius
        if (blurRadius < 0) {
            blurRadius = 0f
        }
        if (blurRadius > 25) {
            blurRadius = 25f
        }
        var outputBitmap: Bitmap? = null
        try {
            Class.forName("android.renderscript.ScriptIntrinsicBlur")
            // 计算图片缩小后的长宽
            val width = (bitmap.getWidth() * BITMAP_SCALE).roundToInt()
            val height = (bitmap.getHeight() * BITMAP_SCALE).roundToInt()
            if (width < 2 || height < 2) {
                return null
            }

            // 将缩小后的图片做为预渲染的图片。
            val inputBitmap = bitmap.scale(width, height, false)
            // 创建一张渲染后的输出图片。
            outputBitmap = Bitmap.createBitmap(inputBitmap)

            // 创建RenderScript内核对象
            val rs = RenderScript.create(context)
            // 创建一个模糊效果的RenderScript的工具对象
            val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

            // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
            // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

            // 设置渲染的模糊程度, 25f是最大模糊度
            blurScript.setRadius(blurRadius)
            // 设置blurScript对象的输入内存
            blurScript.setInput(tmpIn)
            // 将输出数据保存到输出内存中
            blurScript.forEach(tmpOut)

            // 将数据填充到Allocation中
            tmpOut.copyTo(outputBitmap)

            return outputBitmap
        } catch (e: Exception) {
            Log.e("Bemboy_Error", "Android版本过低")
            return null
        }
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 要旋转的图片
     * @return 旋转后的图片
     */
    fun rotate(bitmap: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.getWidth(),
            bitmap.getHeight(), matrix, true
        )
    }
}
