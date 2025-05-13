package com.kira.learning.module.upload

import android.os.Bundle
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.req.ReqKycInfo
import com.kira.learning.databinding.ActivityUploadBinding
import com.kira.learning.dialog.UploadDialog
import com.kira.learning.expand.ShowErrorMsg
import com.kira.learning.expand.getStatusBarColor
import com.kira.learning.manager.Launch
import com.kira.learning.manager.Launch.jumpToAppSettingPage
import com.kira.learning.module.home.HomeEvent
import com.kira.learning.module.process.BaseProcessActivity
import com.kira.learning.module.process.BaseProcessViewModel
import com.kira.learning.permission.PermissionHelper
import com.kira.learning.permission.appPermissions
import com.kira.learning.util.GPInfoUtils
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class UploadActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityUploadBinding>()

    private val mViewModel by lazyViewModel<UploadViewModel>()

    private val mUploadDialog by lazy {
        UploadDialog(this)
    }

    private var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(getStatusBarColor(), true)
        mViewModel.resultLiveData.observerNonSticky(this) {
            if (!it.isSuccess() || it.data == false) {
                it.ShowErrorMsg()
            }
            uploadSuccess()
            GPInfoUtils.saveTag(GPInfoUtils.TAG7)
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
        PermissionHelper.reqPermission(this, appPermissions.toList(), true, isFixGroup = true, {
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
        mDisposable = Flowable.just(2).delay(400, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mUploadDialog.dismiss()
                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
                Launch.skipMainActivity(this)
                finish()
            }
    }

    override fun onDestroy() {
        mDisposable?.dispose()
        mDisposable = null
        super.onDestroy()
    }

    override fun initObserver() {}

    override fun getNextType(): Int = 0
}