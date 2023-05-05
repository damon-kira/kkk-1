package com.colombia.credit.util

import android.content.Context
import android.media.MediaPlayer
import com.util.lib.MainHandler

class MediaHelper(private val context: Context) {

    private var mMediaPlayer: MediaPlayer? = null

    private val TAG = "debug_MediaHelper"

    init {
        mMediaPlayer = MediaPlayer()

    }

    private var mRawId: Int = 0
    private val mRunnable by lazy {
        Runnable {
            doPlay(mRawId)
        }
    }

    @Synchronized
    fun doPlay(rawId: Int) {
        try {
            val mediaPlay = mMediaPlayer ?: return
            mediaPlay.reset()
            setPlayCompleteListener {
                MainHandler.postDelayed(mRunnable, 100)
            }
            context.resources.openRawResourceFd(rawId).use { assetFileDescriptor ->
                mediaPlay.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                mediaPlay.setOnPreparedListener { mp -> mp?.start() }
                mediaPlay.prepareAsync()
            }
        } catch (e: Exception) {
        }
    }

    fun stop() {
        if(mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
        }
    }

    fun reset() {
        try {
            mMediaPlayer?.reset()
        } catch (e: Exception) {
        }
    }

    private fun setPlayCompleteListener(listener: () -> Unit) {
        mMediaPlayer?.setOnCompletionListener {
            mMediaPlayer?.setOnPreparedListener(null)
            listener.invoke()
        }
    }

    fun close() {
        try {
            stop()
            MainHandler.remove(mRunnable)
            mMediaPlayer?.setOnCompletionListener(null)
            mMediaPlayer?.reset()
            mMediaPlayer?.release()
            mMediaPlayer = null
        } catch (e: Exception) {
        }
    }
}