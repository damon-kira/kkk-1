package com.colombia.credit.util.image.builder

import android.graphics.Bitmap
import android.net.Uri
import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.data.CompressResult
import com.colombia.credit.util.image.data.CompressParams
import com.colombia.credit.util.image.worker.CompressWorker
import com.colombia.credit.util.image.worker.Worker
import com.util.lib.getContext
import com.util.lib.image.commonCompressPic
import java.io.File

class CompressImageBuilder(functionManager: FunctionManager) :
    BaseBuilder<CompressResult>(functionManager) {

    private var fileToSave: File? = null

    private var sourceFile: Uri? = null

    private var quality: Int = 85

    private var targetWidth: Int = -1

    private var targetHeight: Int = -1

    private var customCompressor: ImageCompressor? = null

    private var compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG

    private var bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888


    fun source(source: File): CompressImageBuilder {
        this.sourceFile = Uri.fromFile(source)
        return this
    }

    fun source(source: Uri): CompressImageBuilder {
        this.sourceFile = source
        return this
    }

    fun quality(quality: Int): CompressImageBuilder {
        this.quality = quality
        return this
    }

    fun fileToSave(fileToSave: File): CompressImageBuilder {
        this.fileToSave = fileToSave
        return this
    }

    fun targetWidth(width: Int): CompressImageBuilder {
        this.targetWidth = width
        return this
    }

    fun targetHeight(height: Int): CompressImageBuilder {
        this.targetHeight = height
        return this
    }

    fun customCompressor(compressor: ImageCompressor): CompressImageBuilder {
        this.customCompressor = compressor
        return this
    }

    override fun createWorker(): Worker<CompressResult> {
        if (customCompressor == null){
            customCompressor = getImageCompressor()
        }
        val params =
            CompressParams(
                sourceFile,
                fileToSave,
                bitmapConfig,
                compressFormat,
                quality,
                targetWidth,
                targetHeight,
                customCompressor
            )
        return CompressWorker(functionManager.container, params)
    }

    private fun getImageCompressor(): ImageCompressor {
        return object : ImageCompressor {
            override fun compress(
                source: Uri,
                outputFile: File,
                bitmapConfig: Bitmap.Config,
                compressFormat: Bitmap.CompressFormat,
                quality: Int,
                targetWidth: Int,
                targetHeight: Int
            ): Boolean {
                return commonCompressPic(
                    getContext(),
                    source,
                    outputFile.absolutePath,
                    targetWidth,
                    targetHeight,
                    bitmapConfig,
                    quality
                )
            }
        }
    }
}

interface ImageCompressor {

    fun compress(
        source: Uri,
        outputFile: File,
        bitmapConfig: Bitmap.Config,
        compressFormat: Bitmap.CompressFormat,
        quality: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Boolean
}
