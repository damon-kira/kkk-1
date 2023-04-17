package com.colombia.credit.module.process.face

import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.bigdata.lib.WifiHelper
import com.camera.lib.BaseCameraManager
import com.camera.lib.CameraFactory
import com.camera.lib.CameraType
import com.colombia.credit.BuildConfig
import com.colombia.credit.R
import com.colombia.credit.app.AppEnv
import com.colombia.credit.bean.req.ReqFaceInfo
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.camera.BitmapCrop
import com.colombia.credit.databinding.ActivityFaceBinding
import com.colombia.credit.expand.TYPE_SUCCESS
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.login.createCountDownTimer
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.module.process.IBaseProcessViewModel
import com.colombia.credit.util.faceWifi
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.net.bean.BaseResponse
import com.common.lib.viewbinding.binding
import com.util.lib.*
import com.util.lib.StatusBarUtil.setStatusBar
import com.util.lib.expand.getPicCacheFilePath
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

    override fun onCreate(savedInstanceState:  Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar(true, R.color.transparent, false)
        setCountDownText(4)

        mBinding.faceAivSwitch.ifShow(AppEnv.DEBUG)

        val manager = CameraFactory.invoke(
            CameraType.CameraX,
            this,
            mBinding.cameraview,
            BaseCameraManager.FACING_FRONT,
            BaseCameraManager.SCREEN_PORTRAIT
        )

        mBinding.faceAivSwitch.setBlockingOnClickListener {
            manager.switchCamera()
        }

        faceWifi = WifiHelper.getSSid(this)
        mBinding.faceAivTake.setBlockingOnClickListener {
            val file = File(getPicCacheFilePath(this, "face.jpg"))
            showLoading(true)
            manager.takePicture(file) { success, f ->
                if (BuildConfig.DEBUG) {
                    Log.i(
                        TAG,
                        "拍照结果是否成功:$success,file:$f length:${if (f.exists()) f.length() else 0}  thread name=${Thread.currentThread().name}"
                    )
                }
                MainHandler.post {
                    hideLoading()
                    if (success && f.exists()) {
                        val rect = Rect(mBinding.aivFaceMask.left, mBinding.aivFaceMask.top, mBinding.aivFaceMask.right, mBinding.aivFaceMask.bottom)
                        BitmapCrop.cropAndCompress(this, f, rect, manager.isFront()) { finalFile ->
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

    override fun initObserver() {}

    override fun uploadException(response: BaseResponse<*>) {
        Launch.skipFaceFailedActivity(this)
    }


    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqFaceInfo()
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel

    override fun getNextType(): Int = TYPE_SUCCESS

    override fun onStart() {
        super.onStart()
        countDown(TYPE_FIRST_INT, 6)
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
            val index = (Math.random() * 100 % mActions.size).toInt()
            mBinding.tvText.text = mActions[index]
            countDown(TYPE_ACTION, 4)
        } else {
            // 显示拍照
            mBinding.tvText.hide()
            mBinding.llTake.show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer?.cancel()
    }
}