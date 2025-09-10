package com.kira.learning.xml.modules.player

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.core.widget.doOnTextChanged
import com.arezoonazer.player.argument.PlayerParams
import com.arezoonazer.player.argument.VideoSubtitle
import com.arezoonazer.player.extension.startPlayer
import com.kira.learning.databinding.ActivityPlayerManageBinding
import com.common.base.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerManageActivity : ComponentActivity() {

    private val mBinding by binding<ActivityPlayerManageBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        with(mBinding) {
            mBinding.playButton.setOnClickListener { startPlayer(getPlayerParam()) }

            videoUrlEditText.doOnTextChanged { text, _, _, _ ->
                customPlayButton.isEnabled = text.isNullOrEmpty().not()
            }

            customPlayButton.setOnClickListener { onCustomPlayButtonClicked() }
        }
    }

    private fun getPlayerParam(): PlayerParams {
        val subtitleList = mutableListOf<VideoSubtitle>().apply {
            add(
                VideoSubtitle(
                    title = "English",
                    url = "https://amara.org/en/subtitles/sbpf8fMnSckL/en/7/download/Big%20Buck%20Bunny.en.vtt"
                )
            )
        }

        return PlayerParams(
            url = "https://5b44cf20b0388.streamlock.net:8443/vod/smil:bbb.smil/playlist.m3u8",
            subtitles = subtitleList
        )
    }

    private fun onCustomPlayButtonClicked() {
        with(mBinding) {
            val videoUrl = videoUrlEditText.text.toString()

            if (videoUrl.isEmpty()) {
                showToast("Please insert video url")
                return
            }

            val subtitleUrl1 = subtitle1UrlEditText.text.toString()
            val subtitleUrl2 = subtitle2UrlEditText.text.toString()

            val subtitleList = mutableListOf<VideoSubtitle>().apply {
                if (subtitleUrl1.isEmpty().not()) {
                    add(
                        VideoSubtitle(
                            title = "subtitle 1",
                            url = subtitleUrl1
                        )
                    )
                }

                if (subtitleUrl2.isEmpty().not()) {
                    add(
                        VideoSubtitle(
                            title = "subtitle 2",
                            url = subtitleUrl2
                        )
                    )
                }
            }

            startPlayer(PlayerParams(videoUrl, subtitleList))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}