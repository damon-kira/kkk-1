package com.common.lib.base

import android.content.res.Resources
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.common.lib.BuildConfig
import com.common.lib.dialog.*
import com.util.lib.log.logger_i
import me.jessyan.autosize.AutoSizeCompat


open class BaseActivity : InjectorActivity(), ILoading, IDialogTask, Injectable {

    protected val TAG = "debug_${this.javaClass.simpleName}"

    protected val isDebug = BuildConfig.DEBUG

    private var mLoadingDialog: DefaultDialog? = null

    private val mDialogManager by lazy(LazyThreadSafetyMode.NONE) {
        DialogLifecycleManager(this)
    }

    override fun getCurrDialog(): IDialog? = mDialogManager.getCurrDialog()

    override fun addDialog(dialog: IDialog, priority: Int, mode: DialogHandleMode) {
        mDialogManager.addDialog(dialog, priority, mode)
    }

    override fun removeDialog(dialog: IDialog) {
        mDialogManager.removeDialog(dialog)
        dialog.dismiss()
    }

    override fun stopShowDialog() {
        mDialogManager.stopShowDialog()
    }

    override fun startShowDialog() {
        mDialogManager.startShowDialog()
    }

    override fun showLoading(cancelable: Boolean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = showLoadingDialog(cancelable)
            logger_i(TAG, "dialog = null")
        } else {
            mLoadingDialog?.setCancelable(cancelable)
            if (!mLoadingDialog!!.isShowing) {
                showDialoga(mLoadingDialog)
            } else {
                logger_i(TAG, "dialog is showing")
            }
            logger_i(TAG, "dialog != null")
        }
    }

    override fun hideLoading() {
        hideLoadingDialog(mLoadingDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) //禁止截屏
    }

    override fun onStop() {
        super.onStop()
        //确保界面销毁前dialog都关闭
        hideLoading()
    }


    protected fun setViewModelLoading(viewModel: BaseViewModel) {
        viewModel.loading.observe(this, Observer {
            if (it) showLoading() else hideLoading()
        })
    }


    override fun getResources(): Resources {
        if (needAutoSize()) {
            try {
                AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    throw e
                }
            }
        }
        return super.getResources()
    }

    open fun needAutoSize() = true
}