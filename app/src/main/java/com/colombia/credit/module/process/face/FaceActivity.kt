package com.colombia.credit.module.process.face

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.bigdata.lib.WifiHelper
import com.bigdata.lib.faceWifi
import com.bigdata.lib.isSwitchPage
import com.camera.lib.BaseCameraManager
import com.camera.lib.CameraFactory
import com.camera.lib.CameraType
import com.colombia.credit.BuildConfig
import com.colombia.credit.R
import com.colombia.credit.app.AppEnv
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqFaceInfo
import com.colombia.credit.camera.BitmapCrop
import com.colombia.credit.databinding.ActivityFaceBinding
import com.colombia.credit.expand.STEP_OK
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.login.createCountDownTimer
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.permission.CameraPermission
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.util.MediaHelper
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.net.bean.BaseResponse
import com.common.lib.viewbinding.binding
import com.mexico.camera.cameraview.ICamera
import com.util.lib.*
import com.util.lib.StatusBarUtil.setStatusBar
import com.util.lib.expand.getPicCacheFilePath
import com.util.lib.log.logger_d
import com.util.lib.span.SpannableImpl
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FaceActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityFaceBinding>()
    private val mViewModel by lazyViewModel<FaceViewModel>()

    private val mActions by lazy {
        resources.getStringArray(R.array.face_action)
    }

    private val TYPE_FIRST_INT = 1
    private val TYPE_ACTION = 2

    private var mCountDownTimer: CountDownTimer? = null

    private var mCameraManager: ICamera? = null

    private var mHomeReceiver: HomeReceiver? = HomeReceiver()

    private val mMediaHelper by lazy {
        MediaHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar(true, R.color.transparent, false)

        mBinding.faceAivSwitch.ifShow(AppEnv.DEBUG)

        if (CameraPermission().hasThisPermission(this)) {
            openCamera()
        }
        registerReceiver(mHomeReceiver, IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        mBinding.faceAivSwitch.setBlockingOnClickListener {
            mCameraManager?.switchCamera()
        }

        faceWifi = WifiHelper.getSSid(this)
        mBinding.faceAivTake.setBlockingOnClickListener {
            val file = File(getPicCacheFilePath(this, "face.jpg"))
            showLoading(true)
            mCameraManager?.takePicture(file) { success, f ->
                if (BuildConfig.DEBUG) {
                    Log.i(
                        TAG,
                        "拍照结果是否成功:$success,file:$f length:${if (f.exists()) f.length() else 0}  thread name=${Thread.currentThread().name}"
                    )
                }
                MainHandler.post {
                    hideLoading()
                    if (success && f.exists()) {
                        val rect = Rect(
                            mBinding.aivFaceMask.left,
                            mBinding.aivFaceMask.top,
                            mBinding.aivFaceMask.right,
                            mBinding.aivFaceMask.bottom
                        )
                        BitmapCrop.cropAndCompress(
                            this,
                            f,
                            rect,
                            mCameraManager?.isFront() ?: true
                        ) { finalFile ->
                            if (finalFile != null) {
                                // 上传照片
                                mViewModel.uploadInfo(ReqFaceInfo().also {
                                    it.path = finalFile.absolutePath
                                })
                            }
                        }
                    }
                }
            }
        }
    }

    private fun openCamera() {
        setCountDownText(4)
        countDown(TYPE_FIRST_INT, 6)
        mCameraManager = CameraFactory.invoke(
            CameraType.CameraX,
            this,
            mBinding.cameraview,
            BaseCameraManager.FACING_FRONT,
            BaseCameraManager.SCREEN_PORTRAIT
        )
    }

    private fun reqPermission() {
        PermissionHelper.reqPermission(this, arrayListOf(CameraPermission()), true, isFixGroup = false,{ result ->
            if (result) {
                openCamera()
            } else {
                finish()
            }
        }, {
            jumpToAppSettingPage()
        })
    }

    override fun initObserver() {}

    override fun uploadException(response: BaseResponse<*>) {
        Launch.skipFaceFailedActivity(this)
    }

    override fun uploadSuccess() {
        super.uploadSuccess()
        finish()
    }


    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqFaceInfo()
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel

    override fun getNextType(): Int = STEP_OK

    override fun onStart() {
        super.onStart()
        reqPermission()
    }

    private fun setCountDownText(second: Long) {
        val text = getString(R.string.seconds, "$second")
        val span = SpannableImpl().init(text).bold(text)
            .size(30.sp().toInt(), text)
            .getSpannable()
        mBinding.tvText.text = span
    }

    private fun countDown(type: Int = TYPE_FIRST_INT, totalTime: Int = 4) {
        mCountDownTimer =
            createCountDownTimer(
                totalTime * TimerUtil.ONE_SECCOND_MILLISECONDS,
                onTimeTick = { second ->
                    if (type == TYPE_FIRST_INT) {
                        setCountDownText(second)
                    }
                },
                onTimerFinish = {
                    countDownFinish(type)
                })
    }

    private fun countDownFinish(type: Int) {
        if (type == TYPE_FIRST_INT) {
            // 获取动作提示
            mBinding.llAction.show()
            mBinding.tvText.hide()
            val index = (Math.random() * 100 % mActions.size).toInt()
            mBinding.tvAction.text = mActions[index]
            mBinding.aivAction.setImageResource(getActionImage(index))
            (mBinding.aivAction.drawable as? AnimationDrawable)?.start()
            mMediaHelper.doPlay(getActionAudio(index))
            countDown(TYPE_ACTION, 4)
        } else {
            // 显示拍照
            mMediaHelper.close()
            mBinding.tvText.hide()
            mBinding.llAction.hide()
            mBinding.llTake.show()
        }
    }

    private fun getActionAudio(index: Int): Int {
        return when (index) {
            0 -> R.raw.alive_blink
            1 -> R.raw.alive_mouth_open
            else -> R.raw.alive_turn
        }
    }

    private fun getActionImage(index: Int): Int {
        return when (index) {
            0 -> R.drawable.alive_anim_blink
            1 -> R.drawable.alive_anim_mouth
            else -> R.drawable.alive_anim_turn
        }
    }

    override fun onStop() {
        super.onStop()
        mHomeReceiver?.stop()
        mMediaHelper.stop()
    }


    override fun onDestroy() {
        unregisterReceiver(mHomeReceiver)
        mHomeReceiver = null
        mMediaHelper.close()
        mCameraManager?.close()
        mCameraManager = null
        super.onDestroy()
        mCountDownTimer?.cancel()
    }

    class HomeReceiver : BroadcastReceiver() {
        var isHome = true

        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val action = intent.action
            logger_d("debug_home", "onReceive: action=$action")
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val text = intent.getStringExtra("reason")
                logger_d("debug_home", "onReceive: text = $text")
                isHome = text == "homekey" || text == "recentapps"
            }
        }

        fun stop() {
            if (isHome) {
                isSwitchPage++
            }
        }
    }
}