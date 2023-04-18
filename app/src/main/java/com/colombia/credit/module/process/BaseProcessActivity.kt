package com.colombia.credit.module.process

import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.DictionaryInfo
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.resp.IRspBaseInfo
import com.colombia.credit.dialog.CustomDialog
import com.colombia.credit.dialog.ProcessBackDialog
import com.colombia.credit.dialog.ProcessSelectorDialog
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.isShowBackDialog
import com.colombia.credit.expand.jumpProcess
import com.colombia.credit.expand.saveShowBackDialog
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.process.bank.BankInfoActivity
import com.colombia.credit.module.process.contact.ContactInfoActivity
import com.colombia.credit.module.process.personalinfo.PersonalInfoActivity
import com.colombia.credit.module.process.work.WorkInfoActivity
import com.colombia.credit.module.process.work.WorkInfoActivity_GeneratedInjector
import com.colombia.credit.view.ToolbarLayout
import com.colombia.credit.view.baseinfo.AbsBaseInfoView
import com.colombia.credit.view.baseinfo.BaseInfoView
import com.common.lib.base.BaseActivity
import com.common.lib.livedata.observerNonSticky
import com.common.lib.net.bean.BaseResponse
import com.util.lib.StatusBarUtil.setStatusBar

abstract class BaseProcessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar(false, R.color.colorPrimary, false)
        val viewModel = getViewModel()
        setViewModelLoading(viewModel)
        viewModel.mUploadLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                uploadSuccess()
            } else {
                uploadException(it)
            }
        }
        initObserver()
    }

    private val mBackDialog by lazy {
        ProcessBackDialog(this).setOnClickListener {
            _backPressed()
        }
    }

    private var mProcessSelectorDialog: ProcessSelectorDialog? = null

    private val mCustomDialog by lazy {
        CustomDialog(this)
    }

    fun showProcessSelectorDialog(
        title: String,
        data: MutableMap<String, String>,
        selectorTag: String? = null,
        listener: (DictionaryInfo) -> Unit
    ) {
        val dialog = mProcessSelectorDialog ?: ProcessSelectorDialog(this)
        dialog.setDataAndTitle(title, data, selectorTag.orEmpty())
        dialog.setOnClickListener {
            listener.invoke(it)
        }
        dialog.show()
    }

    protected fun setBaseInfo(baseInfoView: BaseInfoView, text: String?, tag: String?) {
        baseInfoView.setViewText(text.orEmpty())
        baseInfoView.tag = tag
    }

    protected fun uploadInfo() {
        if (checkCommitInfo()) {
            val commitInfo = getCommitInfo()
            getViewModel().uploadInfo(commitInfo)
        }
    }

    protected fun setToolbarListener(toolbarLayout: ToolbarLayout) {
        toolbarLayout.setOnbackListener {
            onBackPressed()
        }
        toolbarLayout.setCustomClickListener {
            mCustomDialog.show()
        }
    }

    override fun onBackPressed() {
        if (isShowBackDialog()) {
            saveShowBackDialog(false)
            mBackDialog.show()
        } else {
            _backPressed()
        }
    }

    private fun _backPressed() {
        when (this) {
            is PersonalInfoActivity -> {
                Launch.skipMainActivity(this)
            }
            is WorkInfoActivity -> {
                Launch.skipPersonalInfoActivity(this)
            }
            is ContactInfoActivity -> {
                Launch.skipWorkInfoActivity(this)
            }
            is BankInfoActivity -> {
                Launch.skipContactInfoActivity(this)
            }
            else -> {
                Launch.skipMainActivity(this)
            }
        }
        finish()
    }

    override fun onDestroy() {
        getViewModel().saveCacheInfo(getCommitInfo())
        super.onDestroy()
    }

    protected fun checkAndSetErrorHint(
        baseInfoView: AbsBaseInfoView,
        errorHint: String? = null
    ): Boolean {
        val text = baseInfoView.getViewText()
        var result = true
        if (text.isEmpty()) {
            result = false
            if (errorHint.isNullOrEmpty()) {
                baseInfoView.setError(
                    getString(
                        R.string.error_process_hint,
                        baseInfoView.getTitle()
                    )
                )
            } else {
                baseInfoView.setError(getString(R.string.error_process_hint, errorHint))
            }
        }
        return result
    }

    open fun uploadSuccess() {
        jumpProcess(this, getNextType())
    }

    open fun uploadException(response: BaseResponse<*>) {
        response.ShowErrorMsg(::uploadInfo)
    }

    abstract fun getNextType(): Int

    abstract fun initObserver()

    abstract fun checkCommitInfo(): Boolean

    abstract fun getCommitInfo(): IReqBaseInfo

    abstract fun getViewModel(): BaseProcessViewModel
}