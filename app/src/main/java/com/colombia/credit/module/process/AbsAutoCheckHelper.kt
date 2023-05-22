package com.colombia.credit.module.process

import androidx.viewbinding.ViewBinding
import com.colombia.credit.view.baseinfo.AbsBaseInfoView
import com.util.lib.MainHandler
import com.util.lib.log.logger_d

// 自动进入下一项
abstract class AbsAutoCheckHelper<VB : ViewBinding>(protected val vb: VB, private var isAuto: Boolean = true){

    protected val TAG = "debug_${this.javaClass.simpleName}"

    private var mCurrIndex = 0
        set(value) = if (value < 0) field = 0 else field = value

    private val mAutoRunnable by lazy {
        Runnable {
            mCurrIndex++
            _startCheckNext()
        }
    }

    fun startCheckNext() {
        isAuto = true
        mCurrIndex = 0
        _startCheckNext()
    }

    protected fun _startCheckNext() {
        val currIndex = mCurrIndex
        logger_d(TAG, "currindex = $currIndex")
        if (checkIndexResult(currIndex)) {
            return
        }
        logger_d(TAG, "checkByValue = $currIndex")
        checkByValue(currIndex)
    }

    /**
     *  是否已经全部完成
     * @return true:全部完成
     * */
    protected abstract fun checkIndexResult(index: Int): Boolean

    protected abstract fun checkByValue(index: Int)

    protected abstract fun showItemDialog(index: Int)

    protected fun checkNextView() {
        MainHandler.remove(mAutoRunnable)
        MainHandler.post(mAutoRunnable)
    }

    protected fun checkInfoEmpty(infoView: AbsBaseInfoView): Boolean =
        infoView.getViewText().isEmpty()

}