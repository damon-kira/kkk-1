package com.common.lib.glide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.common.lib.base.BaseActivity
import com.common.lib.net.NetBaseParamsManager
import com.util.lib.MainHandler
import com.bumptech.glide.request.target.Target
import com.util.lib.dp

/**
 * Created by weisl on 2019/11/1.
 */
object GlideUtils {
    fun loadImageNoCache(
        fragment: Fragment,
        width: Int,
        height: Int,
        url: String,
        radius: Float = 0f,
        resultSuccess: (bitmap: Bitmap?) -> Unit,
        resultError: (error: Exception?) -> Unit
    ) {
        loadImage(Glide.with(fragment), width, height, url, radius, resultSuccess, resultError)
    }

    fun loadImageNoCache(
        activity: BaseActivity,
        width: Int,
        height: Int,
        url: String,
        radius: Float = 0f,
        resultSuccess: (bitmap: Bitmap?) -> Unit,
        resultError: (error: Exception?) -> Unit
    ) {
        loadImage(Glide.with(activity), width, height, url, radius, resultSuccess, resultError)
    }

    fun loadImageFromRes(context: Context, resId: Int, imageView: ImageView) {
        Glide.with(context)
            .asBitmap()
            .apply(createRequestOptions(true, DecodeFormat.PREFER_RGB_565))
            .load(resId)
            .into(imageView)
    }

    fun loadCornerImageFromUrl(context: Context, url: String, imageView: ImageView, corner: Float) {
        Glide.with(context)
            .asBitmap()
            .apply(
                createCornerRequestOptions(
                    imageView.width,
                    imageView.height,
                    false,
                    corner = corner
                )
            )
            .load(getUrl(url))
            .into(imageView)
    }

    fun getGlideCacheUrl(context: Context, url: String, result: (resultPath: String?) -> Unit) {
        if (!url.startsWith("https")) {
            result.invoke("")
            return
        }
//        val disposable = Flowable.fromPublisher<String> {
//            try {
//                val path = Glide.with(context)
//                    .load(getUrl(url))
//                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get().absolutePath
//                it.onNext(path)
//                it.onComplete()
//            } catch (e: Exception) {
//                it.onError(e)
//            }
//        }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                result.invoke(it)
//            }, {
//                result.invoke(null)
//            })
    }

    private fun createRequestOptions(
        width: Int,
        height: Int,
        skipMemory: Boolean = true,
        strategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
        radius: Float = 0f
    ): RequestOptions {
        var options = RequestOptions()
        if (radius > 0f) {
            options = options.transform(RoundedCorners(radius.dp()))
        }
        return options.skipMemoryCache(skipMemory).diskCacheStrategy(strategy)
            .override(width, height)
    }

    private fun createRequestOptions(
        skipMemory: Boolean = true,
        format: DecodeFormat,
        strategy: DiskCacheStrategy = DiskCacheStrategy.NONE
    ): RequestOptions {
        return RequestOptions().skipMemoryCache(skipMemory).format(format)
            .diskCacheStrategy(strategy)
    }

    @SuppressLint("CheckResult")
    private fun createCornerRequestOptions(
        width: Int,
        height: Int,
        skipMemory: Boolean = true,
        strategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
        corner: Float
    ): RequestOptions {
        val requestOptions = RequestOptions()
        if (corner > 0) {
            requestOptions.transform(
                RoundedCorners(
                    corner.dp()
                )
            )
        }
        requestOptions.skipMemoryCache(skipMemory).diskCacheStrategy(strategy)
            .override(width, height)
        return requestOptions
    }

    private fun loadImage(
        requestManger: RequestManager, width: Int,
        height: Int,
        url: String,
        radius: Float,
        resultSuccess: (bitmap: Bitmap?) -> Unit,
        resultError: (error: Exception?) -> Unit
    ) {
        requestManger.asBitmap()
            .load(getUrl(url))
            .apply(createRequestOptions(width, height, radius = radius))
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    MainHandler.post {
                        resultError(e)
                    }
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    MainHandler.post {
                        resultSuccess(resource)
                    }
                    return true

                }
            }).submit()
    }

    private fun getUrl(url: String): Any{
        return if (url.startsWith("https")) {
            GlideUrl(url, LazyHeaders
                .Builder()
                .addHeader("x-app-sign", NetBaseParamsManager.getSignVersion())
                .build())
        } else url
    }
}