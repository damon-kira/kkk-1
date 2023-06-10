package com.util.lib.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder.ImageInfo
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.util.lib.ImageInfoUtil
import com.util.lib.MainHandler
import com.util.lib.StorageUriUtils
import com.util.lib.ThreadPoolUtil
import com.util.lib.log.isDebug
import java.io.*
import kotlin.math.roundToInt


/**图片默认大小 */
val IMAGE_DEFAULT_WIDTH = 720
val IMAGE_DEFAULT_HEIGHT = 1080

/**
 * 服务端图片比对 分辨率不能小于 512*512 宽高 都不能小于
 */
val IMAGE_MIN_SIZE = 512

/**
 * 服务端图片比对 分辨率不能大于 4096 宽高 都不能大于这个值
 */
val IMAGE_MAX_SIZE = 4096

// 无需压缩的最大尺寸
val DONT_NEED_COMPRESS_LIMIT = 1000 * 800

// 生成的文件最大大小
val TARGET_FILE_SIZE_LIMIT = 2 * 1024 * 1024

val COMPRESS_TAG = "compress"

/**
 * 图片压缩 但是没有存储exif信息
 *
 * @param sourcePath
 * @param targetPath
 * @return
 */
fun commonCompressPic(
    sourcePath: String, targetPath: String, targetWidth: Int, targetHeight: Int,
    bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888, quality: Int = 85
): Boolean {
    var width = targetWidth
    var height = targetHeight
    var isSuccess = false
    val matrix = Matrix()
    val opt = BitmapFactory.Options()
    var rotateBitmap: Bitmap? = null
    var bitmap: Bitmap? = null
    if (width <= 0 || height <= 0) {
        width = IMAGE_DEFAULT_WIDTH
        height = IMAGE_DEFAULT_HEIGHT
    }

    var fos: FileOutputStream? = null
    var isNeedRepeadCompress = false
    if (isDebug()) {
        Log.i(COMPRESS_TAG, "bitmapConfig = $bitmapConfig")
    }
    var rotation = 0f
    var targetFileExceedsLimit = false
    val exifInfo = ImageInfoUtil.getImageExifInfo(sourcePath)
    try {
        val exif = ExifInterfaceImpl(sourcePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        rotation = getPhotoOrientation(orientation).toFloat()

        if (isDebug()) {
            Log.i(COMPRESS_TAG, "压缩旋转前图片旋转角度 $rotation")
        }
        // 旋转
        matrix.postRotate(rotation)


        opt.inJustDecodeBounds = true//只取大小，不放在内存
        BitmapFactory.decodeFile(sourcePath, opt)

        if (isDebug()) {
            val inFile = File(sourcePath)
            Log.i(
                COMPRESS_TAG, "压缩&旋转前图片大小 width:${opt.outWidth},height:${opt.outHeight}," +
                        "size:${if (inFile.exists()) inFile.length() else 0}"
            )
        }

        // 先调用GC清理内存，然后再进行压缩
        try {
            System.gc()
            Thread.sleep(100)
        } catch (e: Throwable) {
            if (isDebug()) {
                Log.i(COMPRESS_TAG, "GC和Sleep过程出错:$e")
            }
        }
        opt.inSampleSize = calculateInSampleSize(opt, width, height)

        opt.inJustDecodeBounds = false
        opt.inPreferredConfig = bitmapConfig
        opt.inPurgeable = true
        opt.inInputShareable = true
        opt.inTempStorage = ByteArray(16 * 1024)

        bitmap = BitmapFactory.decodeFile(sourcePath, opt)

        val tempBitmapH = bitmap!!.height.toFloat()
        val tempBitmapW = bitmap.width.toFloat()
        val tempSide = tempBitmapH + tempBitmapW
        val tempHScale = tempBitmapH / tempSide
        val tempWScale = tempBitmapW / tempSide
        val tempHLonger = tempHScale > tempWScale


        var scale = 1f
        if (tempHLonger) {
            if (tempBitmapH > height) {
                scale = height / tempBitmapH
            }
        } else {
            if (tempBitmapW > width) {
                scale = width / tempBitmapW
            }
        }

        matrix.postScale(scale, scale)

        rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        //如果原文件和目标文件相同路径 删除原文件
        if (sourcePath == targetPath) {
            val sourceFile = File(sourcePath)
            if (sourceFile.exists()) {
                sourceFile.delete()
            }
        }
        val file = File(targetPath)

        fos = FileOutputStream(file)

        // 压缩完之后需要设置相关exif信息
        isSuccess = rotateBitmap!!.compress(Bitmap.CompressFormat.JPEG, quality, fos)//85压缩 85％

        if(isDebug()){
            val outFile = File(targetPath)
            Log.i(COMPRESS_TAG,"压缩后图片大小 width:${rotateBitmap.width},height:${rotateBitmap.height}" +
                    ",size:${if(outFile.exists()) outFile.length() else 0}")
        }

        fos.flush()
        if(file.exists() && file.length() >= TARGET_FILE_SIZE_LIMIT
            && Bitmap.Config.ARGB_8888 == bitmapConfig && quality >= 85){

            if(isDebug()){
                Log.i(COMPRESS_TAG,"配置ARGB_8888&压缩质量>=85,压缩完之后图片大小仍然大于2M，需要降低质量继续压缩")
            }

            targetFileExceedsLimit = true
        }
        // 压缩后新生成的图片exif信息中经纬度没有了
    } catch (e: Exception) {

        if(isDebug()){
            Log.i(COMPRESS_TAG,"e: Exception = $e")
        }
    } catch (e: OutOfMemoryError) {
        if(isDebug()){
            Log.i(COMPRESS_TAG,"e: OutOfMemoryError = $e")
            Log.i(COMPRESS_TAG,"当前decode格式：$bitmapConfig")
        }
        if(bitmapConfig == Bitmap.Config.ARGB_8888){
            isNeedRepeadCompress = true
        }else if(bitmapConfig == Bitmap.Config.RGB_565){
            if(isDebug()){
                Log.i(COMPRESS_TAG,"两次压缩都失败，直接拷贝文件")
            }
            isSuccess = copyFileIfNeed(sourcePath,targetPath,opt.outWidth,opt.outHeight, rotation)
        }
    } finally {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        if (rotateBitmap != null && !rotateBitmap.isRecycled) {
            rotateBitmap.recycle()
        }
        try {
            fos?.close()
        }catch (e: Exception){
            if(isDebug()){
                Log.i(COMPRESS_TAG,"关闭输出流失败:$e")
            }
        }
    }
    if(isNeedRepeadCompress || targetFileExceedsLimit){
        if(isDebug()){
            Log.i(COMPRESS_TAG,"isNeedRepeadCompress = $isNeedRepeadCompress")
        }
        val compressQuality = if(targetFileExceedsLimit){
            80
        }else{
            85
        }
        return commonCompressPic_565(sourcePath,targetPath,targetWidth,targetHeight,compressQuality)
    }
    // 设置exif信息
    ImageInfoUtil.saveExifInfo(targetPath, exifInfo)
    return isSuccess
}

fun copyFileIfNeed(sourcePath: String, targetPath: String, width: Int, height: Int, rotation: Float): Boolean {
    /**** 开始：判断图片是否满足不压缩条件,如果满足的话，则不进行压缩直接拷贝文件 ***/
    val sourceFile = File(sourcePath)
    // 旋转角度是0
    val rotationCorrect = rotation == 0f
    // 宽度高度满足要求
    val sizeCorrect = width < IMAGE_MAX_SIZE && height < IMAGE_MAX_SIZE
    // 图片在磁盘的尺寸满足要求
    val fileSizeCorrect = sourceFile.exists() && sourceFile.length() < DONT_NEED_COMPRESS_LIMIT

    if(rotationCorrect && sizeCorrect && fileSizeCorrect){
        val copySuccess = copyFile(sourcePath,targetPath)
        if(isDebug()){
            Log.i(COMPRESS_TAG,"满足图片不需要压缩条件，直接拷贝文件，拷贝成功?$copySuccess")
        }
        if(copySuccess){
            return true
        }
    }
    /**** 判断图片是否满足不压缩条件,如果满足的话，则不进行压缩直接拷贝文件 ***/
    return false
}

fun copyFileIfNeed( context: Context, source: Uri, targetPath: String, width: Int, height: Int, rotation: Float): Boolean {
    /**** 开始：判断图片是否满足不压缩条件,如果满足的话，则不进行压缩直接拷贝文件 ***/
    if (rotation != 0f || width >= IMAGE_MAX_SIZE || height >= IMAGE_MAX_SIZE) return false
    val sourceFileSize = StorageUriUtils.getFileSize(context, source) ?: return false
    return if (sourceFileSize < DONT_NEED_COMPRESS_LIMIT) {
        val inputStream = context.contentResolver.openInputStream(source) ?: return false
        copyFile(inputStream, targetPath)
    } else {
        false
    }
}


fun copyFile(inputStream: InputStream, targetPath: String): Boolean {
    var outputStream: OutputStream? = null
    try {
        inputStream.use {
            val fos = FileOutputStream(targetPath)
            outputStream = fos
            fos.use {
                val buffer = ByteArray(8 * 1024)
                var len = inputStream.read(buffer)
                while (len != -1) {
                    fos.write(buffer,0,len)
                    len = inputStream.read(buffer)
                }
                return true
            }
        }
    }finally {
        inputStream.close()
        outputStream?.close()
    }
}




fun commonCompressPic(context: Context, source: Uri, targetPath: String, targetWidth: Int, targetHeight: Int,
                      bitmapConfig:Bitmap.Config = Bitmap.Config.ARGB_8888, quality: Int = 85): Boolean {
    var width = targetWidth
    var height = targetHeight
    var isSuccess = false
    val matrix = Matrix()
    val opt = BitmapFactory.Options()
    var rotateBitmap: Bitmap? = null
    var bitmap: Bitmap? = null
    if (width <= 0 || height <= 0) {
        width = IMAGE_DEFAULT_WIDTH
        height = IMAGE_DEFAULT_HEIGHT
    }
    var fos: FileOutputStream? = null
    var isNeedRepeatCompress = false
    if(isDebug()) {
        Log.i(COMPRESS_TAG, "bitmapConfig = $bitmapConfig")
    }
    var rotation = 0f
    var targetFileExceedsLimit = false
    var imageExifInfo: HashMap<String, String?>? = null
    try {
        val fd: FileDescriptor = context.contentResolver.openFileDescriptor(source, "r")?.fileDescriptor!!
        imageExifInfo = ImageInfoUtil.getImageExifInfo(fd)
        val orientation = imageExifInfo[androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION]?.toIntOrNull() ?: androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
        rotation = getPhotoOrientation(orientation).toFloat()
        if(isDebug()) {
            Log.i(COMPRESS_TAG, "压缩旋转前图片旋转角度 $rotation")
        }
        // 旋转
        matrix.postRotate(rotation)

        opt.inJustDecodeBounds = true//只取大小，不放在内存
        BitmapFactory.decodeStream(context.contentResolver.openInputStream(source)!!,null, opt)

        opt.inSampleSize = calculateInSampleSize(opt, width, height)
        opt.inJustDecodeBounds = false
        opt.inPreferredConfig = bitmapConfig
        opt.inPurgeable = true
        opt.inInputShareable = true
        opt.inTempStorage = ByteArray(16 * 1024)
        bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(source)!!,null, opt)

        val tempBitmapH = bitmap!!.height.toFloat()
        val tempBitmapW = bitmap.width.toFloat()
        val tempSide = tempBitmapH + tempBitmapW
        val tempHScale = tempBitmapH / tempSide
        val tempWScale = tempBitmapW / tempSide
        val tempHLonger = tempHScale > tempWScale


        var scale = 1f
        if (tempHLonger) {
            if (tempBitmapH > height) {
                scale = height / tempBitmapH
            }
        } else {
            if (tempBitmapW > width) {
                scale = width / tempBitmapW
            }
        }

        matrix.postScale(scale, scale)

        rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        Log.i(COMPRESS_TAG, "rotateBitmap=====")
        //如果原文件和目标文件相同路径 删除原文件
        val file = File(targetPath)
        val scheme = source.scheme
        if("file".equals(scheme,false) || scheme == null){
            val sourceFile = File(source.encodedPath)
            if(sourceFile == file){
                if(sourceFile.exists()){
                    sourceFile.delete()
                }
            }
        }

        fos = FileOutputStream(file)

        // 压缩完之后需要设置相关exif信息
        isSuccess = rotateBitmap!!.compress(Bitmap.CompressFormat.JPEG, quality, fos)//85压缩 85％

        if(isDebug()){
            val outFile = File(targetPath)
            Log.i(COMPRESS_TAG,"压缩后图片大小 width:${rotateBitmap.width},height:${rotateBitmap.height}" +
                    ",size:${if(outFile.exists()) outFile.length() else 0}")
        }

        fos.flush()
        if(file.exists() && file.length() >= TARGET_FILE_SIZE_LIMIT
            && Bitmap.Config.ARGB_8888 == bitmapConfig && quality >= 85){

            if(isDebug()){
                Log.i(COMPRESS_TAG,"配置ARGB_8888&压缩质量>=85,压缩完之后图片大小仍然大于2M，需要降低质量继续压缩")
            }

            targetFileExceedsLimit = true
        }
        // 压缩后新生成的图片exif信息中经纬度没有了
    } catch (e: Exception) {
        e.printStackTrace()
        if(isDebug()){
            Log.i(COMPRESS_TAG,"e: Exception = $e")
        }
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if(isDebug()){
            Log.i(COMPRESS_TAG,"e: OutOfMemoryError = $e")
            Log.i(COMPRESS_TAG,"当前decode格式：$bitmapConfig")
        }
        if(bitmapConfig == Bitmap.Config.ARGB_8888){
            isNeedRepeatCompress = true
        }else if(bitmapConfig == Bitmap.Config.RGB_565){
            if(isDebug()){
                Log.i(COMPRESS_TAG,"两次压缩都失败，直接拷贝文件")
            }
            isSuccess = copyFileIfNeed(context,source,targetPath,opt.outWidth,opt.outHeight, rotation)
        }
    } finally {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        if (rotateBitmap != null && !rotateBitmap.isRecycled) {
            rotateBitmap.recycle()
        }
        try {
            fos?.close()
        }catch (e: Exception){
            if(isDebug()){
                Log.i(COMPRESS_TAG,"关闭输出流失败:$e")
            }
        }
    }
    if(isNeedRepeatCompress || targetFileExceedsLimit){
        if(isDebug()){
            Log.i(COMPRESS_TAG,"isNeedRepeatCompress = $isNeedRepeatCompress")
        }
        val compressQuality = if(targetFileExceedsLimit){
            80
        }else{
            85
        }
        commonCompressPic(context,source,targetPath,targetWidth,targetHeight,Bitmap.Config.RGB_565,compressQuality)
    }
    imageExifInfo?.let {imageInfo ->
        ImageInfoUtil.saveExifInfo(targetPath, imageInfo)
    }
    return isSuccess
}

/**
 * ARGB_8888压缩图片
 */
fun commonCompressPic(sourcePath: String, targetPath: String, targetWidth: Int, targetHeight: Int):Boolean{
    return commonCompressPic(sourcePath,targetPath,targetWidth,targetHeight,Bitmap.Config.ARGB_8888)
}

/**
 * RGB_565压缩图片
 */
fun commonCompressPic_565(sourcePath: String, targetPath: String, targetWidth: Int, targetHeight: Int,quality: Int = 85):Boolean{
    return commonCompressPic(sourcePath,targetPath,targetWidth,targetHeight,Bitmap.Config.RGB_565,quality)
}


fun commonCompressPic(sourcePath: String, targetPath: String):Boolean{
    return commonCompressPic(sourcePath,targetPath,0,0)
}

/** 在io线程压缩图片
 *
 * @param sourcePath 原路径
 * @param targetPath 压缩后路径
 * @param success 压缩成功回调
 * */
fun commonCompressPicIO(sourcePath: String, targetPath: String, success: (path: String?) -> Unit) {
    ThreadPoolUtil.executor("commonCompressPicIO"){
        val result = commonCompressPic(sourcePath, targetPath)
        MainHandler.post {
            success(if (result) targetPath else null)
        }
    }
//    return Observable.fromArray(sourcePath)
//        .subscribeOn(Schedulers.io())
//        .map {
//            commonCompressPic(sourcePath, targetPath)
//        }
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe({
//            success(targetPath)
//        }, {})
}


/**
 * 获得文件路径 exif 信息
 *
 * @param sourcePath
 * @return
 */
fun getPhotoOrientation(sourcePath: String): Int {
    var orientation = -1
    try {
        val exif = ExifInterfaceImpl(sourcePath)
        if (exif != null) {
            orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }
    } catch (e: Exception) {

    }

    return orientation
}

fun copyFile(sourcePath: String, targetPath: String): Boolean {
    if (sourcePath == targetPath) {
        return true
    }
    if (File(sourcePath).exists()) {
        val fis = FileInputStream(sourcePath)
        fis.use {
            val fos = FileOutputStream(targetPath)
            fos.use {
                val buffer = ByteArray(8 * 1024)
                var len = fis.read(buffer)
                while (len != -1) {
                    fos.write(buffer, 0, len)
                    len = fis.read(buffer)
                }
                return true
            }
        }
    }
    return false
}

/**
 * 获得缩放大小
 *
 * @param options
 * @param reqWidth
 * @param reqHeight
 * @return
 */
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
        val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio

        if (isDebug()) {
            Log.i(COMPRESS_TAG, "获取inSampleSize:第一次计算(根据宽高比) - $inSampleSize")
        }

        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        if (isDebug()) {
            Log.i(COMPRESS_TAG, "获取inSampleSize:第二次计算(根据面积) - $inSampleSize")
        }

        // 把inSampleSize修正为2的n次方幂
        var pow = 0.0
        while (Math.pow(2.0, pow) < inSampleSize) {
            val temp = pow + 1
            if (Math.pow(2.0, temp) <= inSampleSize) {
                pow = temp
            } else {
                break
            }
        }

        inSampleSize = Math.pow(2.0, pow).toInt()
        if (isDebug()) {
            Log.i(COMPRESS_TAG, "获取inSampleSize:第三次计算(修复成2的倍数) - $inSampleSize")
        }
        // 如果计算结果导致图片size小于256，则修改inSampleSize的值
        while ((height / inSampleSize < IMAGE_MIN_SIZE || width / inSampleSize < IMAGE_MIN_SIZE) && inSampleSize >= 2) {
            inSampleSize /= 2
        }

        if (isDebug()) {
            Log.i(COMPRESS_TAG, "获取inSampleSize:第四次计算(根据256计算出来的最终结果) - $inSampleSize")
        }
    }
    return inSampleSize
}

/**
 * 获取照片的方向
 *
 * @param orientation 方向
 * @return
 * @author sunsg
 * @since 2015-10-17
 */
fun getPhotoOrientation(orientation: Int): Int {
    var degree = 0
    when (orientation) {
        ExifInterface.ORIENTATION_TRANSPOSE,
        ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
        ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
        ExifInterface.ORIENTATION_TRANSVERSE,
        ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        else -> degree = 0
    }
    return degree
}