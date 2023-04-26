package com.colombia.credit.module.upload

import android.os.Bundle
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.databinding.ActivityUploadBinding
import com.colombia.credit.dialog.UploadDialog
import com.colombia.credit.expand.getStatusBarColor
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.appPermissions
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.MainHandler
import com.util.lib.StatusBarUtil.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityUploadBinding>()

    private val mViewModel by lazyViewModel<UploadViewModel>()

    private val mUploadDialog by lazy {
        UploadDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(getStatusBarColor(), true)
        mViewModel.resultLiveData.observerNonSticky(this) {
            uploadSuccess()
        }
    }

    override fun initViewModel() {
//        super.initViewModel()
    }

    override fun onStart() {
        super.onStart()
        reqPermission()
    }

    private fun reqPermission() {
        PermissionHelper.reqPermission(this, appPermissions.toList(), true, {
            showDialog()
            mViewModel.checkAndUpload()
        }, {
            jumpToAppSettingPage()
        })
    }

    private fun showDialog() {
        addDialog(mUploadDialog)
    }

    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqKycInfo()
    }

    override fun getViewModel(): BaseProcessViewModel? = null

    override fun uploadSuccess() {
        mUploadDialog.end()
        MainHandler.postDelay({
            mUploadDialog.dismiss()
            LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
            Launch.skipMainActivity(this)
        }, 600)
    }

    override fun initObserver() {}

    override fun getNextType(): Int = 0
}