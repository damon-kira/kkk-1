package com.colombia.credit.module.process.kyc

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.bean.resp.KycOcrInfo
import com.colombia.credit.bean.resp.RspKycInfo
import com.colombia.credit.databinding.ActivityKycInfoBinding
import com.colombia.credit.dialog.DatePickerDialog
import com.colombia.credit.expand.TYPE_FACE
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
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
        setToolbarListener(mBinding.processToolbar)
        setViewModelLoading(mViewModel)
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

        mViewModel.getCacheInfo()?.also { info ->
            info as ReqKycInfo
            mBinding.kycBivNuip.setViewText(info.ALKxGTZ4FQ.orEmpty())
            val names = info.y6hQBtv?.split("|").orEmpty()
            if (names.size > 1) {
                mBinding.kycBivName.setViewText(names[1])
                mBinding.kycBivSurname.setViewText(names[0])
            }
            mBinding.kycBivBirthday.setViewText(info.GJmhwzsK5.orEmpty())
            if (mGender.containsKey(info.W8mqV)) {
                setBaseInfo(mBinding.kycBivGender, mGender[info.W8mqV], info.W8mqV)
            }
        }
        mViewModel.getInfo()
    }

    private fun loadImage(url: String?, type: Int) {
        if (url.isNullOrEmpty()) return
        GlideUtils.loadImageNoCache(this, 0, 0, url, 4f, { bitmap ->
            if (bitmap != null) {
                if (type == PicType.PIC_FRONT) {
                    mBinding.ilPic.setLeftImage(bitmap)
                    mBinding.ilPic.setLeftStatus(IdentityPicStatus.STATUS_SUCCESS)
                    mBinding.ilPic.setLeftEnable(false)
                } else {
                    mBinding.ilPic.setRightImage(bitmap)
                    mBinding.ilPic.setRightStatus(IdentityPicStatus.STATUS_SUCCESS)
                    mBinding.ilPic.setRightEnable(false)
                }
            }
        }, {})
    }

    override fun onRestart() {
        super.onRestart()
        if (isJumpSetting) {
            isJumpSetting = false
            reqPermission()
        }
    }

    override fun initObserver() {
        mViewModel.imageLivedata.observerNonSticky(this) {
            if (!it.isSuccess() || !mBinding.ilPic.isAllSuccess()) {
                return@observerNonSticky
            }
            it.getData()?.let { info ->
                setDetailInfo(info)
                mBinding.ilPic.setEnable(leftEnable = false, rightEnable = false)
            }
        }

        mViewModel.mInfoLiveData.observerNonSticky(this) { rspInfo ->
            if (rspInfo !is RspKycInfo) return@observerNonSticky
            rspInfo.jmWujylO6j?.let { info ->
                mBinding.llKycInfo.show()
                mBinding.kycBivNuip.setViewText(info.Wa7f.orEmpty())
                mBinding.kycBivName.setViewText(info.JSusdh7YE.orEmpty())
                mBinding.kycBivSurname.setViewText(info.FStwV6Fge7.orEmpty())
                mBinding.kycBivBirthday.setViewText(info.YiWtoa1.orEmpty())
                if (mGender.containsKey(info.DrD60)) {
                    setBaseInfo(mBinding.kycBivGender, mGender[info.DrD60], info.DrD60)
                }
                loadImage(info.fefFSZ, PicType.PIC_FRONT)
                loadImage(info.YZ7Mlc8yf, PicType.PIC_FRONT)
            }
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

//    override fun onBackPressed() {
//        Launch.skipMainActivity(this)
//        super.onBackPressed()
//    }

    // 显示详细信息
    private fun setDetailInfo(kycInfo: KycOcrInfo) {
        mBinding.llKycInfo.show()
        kycInfo.G5JGlRQZWl?.let {
            mBinding.kycBivNuip.setViewText(it.BUJQ2NTER.orEmpty())
            mBinding.kycBivName.setViewText(it.NNdlFOp.orEmpty())
            mBinding.kycBivSurname.setViewText(it.LlK8Tt.orEmpty())
        }
        kycInfo.tAUA?.let {
            mBinding.kycBivBirthday.setViewText(it.OXOcXj.orEmpty())
            val gender = it.COXa.orEmpty()
            if(mGender.containsKey(gender)) {
                setBaseInfo(mBinding.kycBivGender, mGender[gender], gender)

            }
        }
    }

    override fun checkCommitInfo(): Boolean {
        var result = checkAndSetErrorHint(mBinding.kycBivNuip)
            .and(checkAndSetErrorHint(mBinding.kycBivSurname))
            .and(checkAndSetErrorHint(mBinding.kycBivName))
            .and(checkAndSetErrorHint(mBinding.kycBivGender))
            .and(checkAndSetErrorHint(mBinding.kycBivBirthday))
        return result
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqKycInfo().also {
            it.ALKxGTZ4FQ = mBinding.kycBivNuip.getViewText()
            val name = mBinding.kycBivName.getViewText()
            val surname = mBinding.kycBivSurname.getViewText()
            it.y6hQBtv = "$surname|$name"
            it.W8mqV = mBinding.kycBivGender.tag?.toString()
            it.GJmhwzsK5 = mBinding.kycBivBirthday.getViewText()
        }
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel
    override fun getNextType(): Int = TYPE_FACE
}