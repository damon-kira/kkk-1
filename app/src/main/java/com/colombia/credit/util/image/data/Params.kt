package com.colombia.credit.util.image.data

import android.graphics.Bitmap
import android.net.Uri
import com.colombia.credit.util.image.annotations.CameraFace
import com.colombia.credit.util.image.annotations.ImageType
import com.colombia.credit.util.image.annotations.PickRange
import com.colombia.credit.util.image.builder.ImageCompressor
import java.io.File

data class PickPictureParams(@PickRange val pickRange: Int, @ImageType val fileType: Int, val checkPermission: Boolean, val needLocationInfo: Boolean)


data class TakePhotoParams(@CameraFace val cameraFace: Int, val fileToSave: File?, val checkPermission: Boolean)


data class IdPictureParams(val type: Int, val picType: Int, val fileToSave: String, val checkPermission: Boolean)



data class CompressParams(val source: Uri?,
                          val fileToSave: File?,
                          val bitmapConfig: Bitmap.Config,
                          val compressFormat: Bitmap.CompressFormat,
                          val quality: Int,
                          val targetWidth:Int,
                          val targetHeight:Int,
                          val customCompressor: ImageCompressor?)

data class VoucherPictureParams(val fileToSave: String, val checkPermission: Boolean)