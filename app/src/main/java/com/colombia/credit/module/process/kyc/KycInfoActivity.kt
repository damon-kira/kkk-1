package com.colombia.credit.module.process.kyc

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.bean.resp.KycOcrInfo
import com.colombia.credit.databinding.ActivityKycInfoBinding
import com.colombia.credit.dialog.DatePickerDialog
import com.colombia.credit.expand.TYPE_FACE
import com.colombia.credit.expand.jumpProcess
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.module.process.IBaseProcessViewModel
import com.colombia.credit.permission.CameraPermission
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.StoragePermission
import com.colombia.credit.util.DictionaryUtil
import com.colombia.credit.util.image.annotations.PicType
import com.colombia.credit.view.identity.IdentityPicStatus
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.glide.GlideUtils
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KycInfoActivity : BaseProcessActivity(), View.OnClickListener {

    private val mBinding by binding<ActivityKycInfoBinding>()

    private val mViewModel by lazyViewModel<KycViewModel>()

    private var isJumpSetting = false

    private val mGender by lazy {
        DictionaryUtil.getGender()
    }

    private val mKycPicHelper by lazy {
        KycPicHelper()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setToolbarListener(mBinding.processToolbar)
        observeLivedata()
        mBinding.tvCommit.setBlockingOnClickListener(this)
        mBinding.kycBivGender.setBlockingOnClickListener(this)
        mBinding.kycBivBirthday.setBlockingOnClickListener(this)
        mBinding.ilPic.setClickListener({
            mKycPicHelper.showPicImageModeDialog(this, PicType.PIC_FRONT)
        }, {
            mKycPicHelper.showPicImageModeDialog(this, PicType.PIC_BACK)
        })

        mKycPicHelper.mResultListener = { filePath, picType ->
            if (!filePath.isNullOrEmpty()) {
                GlideUtils.loadImageNoCache(
                    this@KycInfoActivity,
                    0,
                    0,
                    filePath,
                    0f,
                    { bitmap ->
                        logger_d(TAG, "onSuccess: bitmap")
                        bitmap?.let {
                            mViewModel.uploadImage(filePath, picType)
                            setImageBitmap(picType, bitmap)
                        }
                    },
                    {
                        logger_e(TAG, "exception = $it")
                    })
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (isJumpSetting) {
            isJumpSetting = false
            reqPermission()
        }
    }

    private fun observeLivedata() {
        mViewModel.imageLivedata.observerNonSticky(this) {
            if (!it.isSuccess()) {
                mBinding
                return@observerNonSticky
            }
            it.getData()?.let { info ->
                setDetailInfo(info)
            }
            setDetailInfo(KycOcrInfo())
        }
    }

    private fun reqPermission() {
        PermissionHelper.reqPermission(this,
            arrayListOf(CameraPermission(), StoragePermission()),
            true, result = {
            }, skipSettingListener = {
                isJumpSetting = true
                jumpToAppSettingPage()
            })
    }


    private fun setImageBitmap(picType: Int, bitmap: Bitmap) {
        if (picType == PicType.PIC_FRONT) {
            mBinding.ilPic.setLeftStatus(IdentityPicStatus.STATUS_SUCCESS)
            mBinding.ilPic.setLeftImage(bitmap)
        } else {
            mBinding.ilPic.setRightStatus(IdentityPicStatus.STATUS_SUCCESS)
            mBinding.ilPic.setRightImage(bitmap)
        }
    }


    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.kyc_biv_gender -> {
                showProcessSelectorDialog(mBinding.kycBivGender.getTitle(), mGender) {
                    mBinding.kycBivGender.setViewText(it.value)
                    mBinding.kycBivGender.tag = it.key
                }
            }
            R.id.kyc_biv_birthday -> {
                DatePickerDialog(this).show()
            }
            R.id.tv_commit -> {
                uploadInfo()
            }
        }
    }

    override fun onBackPressed() {
        Launch.skipMainActivity(this)
        super.onBackPressed()
    }

    // 显示详细信息
    private fun setDetailInfo(kycInfo: KycOcrInfo) {
        mBinding.llKycInfo.show()
    }

    override fun checkCommitInfo(): Boolean {
        var result = checkAndSetErrorHint(mBinding.kycBivNuip)
            .and(checkAndSetErrorHint(mBinding.kycBivSurname))
            .and(checkAndSetErrorHint(mBinding.kycBivName))
            .and(checkAndSetErrorHint(mBinding.kycBivGender))
            .and(checkAndSetErrorHint(mBinding.kycBivBirthday))
        //生日格式

        return result
    }

    override fun getCommitInfo(): IReqBaseInfo {
        val nuip = mBinding.kycBivNuip.getViewText()
        val name = mBinding.kycBivName.getViewText()
        val surname = mBinding.kycBivSurname.getViewText()
        val gender = mBinding.kycBivGender.tag
        val birthday = mBinding.kycBivBirthday.getViewText()
        return ReqKycInfo()
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel

    override fun uploadSuccess() {
        jumpProcess(this, TYPE_FACE)
    }
}