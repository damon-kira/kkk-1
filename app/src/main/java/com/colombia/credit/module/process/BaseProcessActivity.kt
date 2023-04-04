package com.colombia.credit.module.process

import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.DictionaryInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.dialog.ProcessSelectorDialog
import com.colombia.credit.view.baseinfo.AbsBaseInfoView
import com.colombia.credit.view.baseinfo.BaseInfoView
import com.common.lib.base.BaseActivity
import com.util.lib.StatusBarUtil.setStatusBar

abstract class BaseProcessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar(false, R.color.colorPrimary, false)
    }

    private var mProcessSelectorDialog: ProcessSelectorDialog? = null

    fun showProcessSelectorDialog(
        title: String,
        data: MutableMap<String, String>,
        selectorTag: String = "",
        listener: (DictionaryInfo) -> Unit
    ) {
        val dialog = mProcessSelectorDialog ?: ProcessSelectorDialog(this)
        dialog.setDataAndTitle(title, data, selectorTag)
        dialog.setOnClickListener {
            listener.invoke(it)
        }
        dialog.show()
    }

    protected fun uploadInfo() {
        if (checkCommitInfo()) {
            val commitInfo = getCommitInfo()
        }
    }

    protected fun checkAndSetErrorHint(baseInfoView: AbsBaseInfoView, errorHint: String? = null): Boolean {
        val text = baseInfoView.getViewText()
        var result = true
        if (text.isEmpty()) {
            result = false
            if (errorHint.isNullOrEmpty()) {
                baseInfoView.setError(getString(R.string.error_process_hint, baseInfoView.getTitle()))
            } else {
                baseInfoView.setError(getString(R.string.error_process_hint, errorHint))
            }
        }
        return result
    }

    abstract fun checkCommitInfo(): Boolean

    abstract fun getCommitInfo(): IBaseInfo
}