package com.kira.learning.module.process

import android.os.Bundle
import com.kira.learning.R
import com.kira.learning.bean.DictionaryInfo
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.dialog.ProcessBackDialog
import com.kira.learning.dialog.ProcessSelectorDialog
import com.kira.learning.expand.*
import com.kira.learning.manager.Launch
import com.kira.learning.module.process.bank.BankInfoActivity
import com.kira.learning.module.process.contact.ContactInfoActivity
import com.kira.learning.module.process.personalinfo.PersonalInfoActivity
import com.kira.learning.view.ToolbarLayout
import com.kira.learning.view.baseinfo.AbsBaseInfoView
import com.kira.learning.view.baseinfo.BaseInfoView
import com.common.lib.base.BaseActivity
import com.common.lib.livedata.observerNonSticky
import com.common.lib.net.bean.BaseResponse
import com.util.lib.StatusBarUtil.setStatusBar

abstract class BaseProcessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar(false, R.color.colorPrimary, false)
        initViewModel()
        initObserver()
    }

    open fun initViewModel() {
        getViewModel()?.let {viewModel ->
            setViewModelLoading(viewModel)
            viewModel.mUploadLiveData.observerNonSticky(this) {
                if (it.isSuccess()) {
                    uploadSuccess()
                } else {
                    uploadException(it)
                }
            }
        }
    }

    private val mBackDialog by lazy {
        ProcessBackDialog(this).setOnClickListener {
            _backPressed()
        }
    }

    private var mProcessSelectorDialog: ProcessSelectorDialog? = null

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
            getViewModel()?.uploadInfo(commitInfo)
        }
    }

    protected fun setToolbarListener(toolbarLayout: ToolbarLayout) {
        toolbarLayout.setOnbackListener {
            onBackPressed()
        }
        toolbarLayout.setCustomClickListener {
            showCustomDialog()
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
            is ContactInfoActivity -> {
                Launch.skipPersonalInfoActivity(this)
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
        getViewModel()?.saveCacheInfo(getCommitInfo())
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

    abstract fun getViewModel(): BaseProcessViewModel?
}