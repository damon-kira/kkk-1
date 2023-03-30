package com.common.lib.dialog

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*

/**
 * Created by weishl on 2020/4/6
 *
 */
class DialogLifecycleManager constructor(private val host: AppCompatActivity) :
    LifecycleObserver,
    DefaultDialog.OnDialogDismissListener, IDialogTask {

    private var mDialogTask = TreeMap<Int, LinkedList<IDialog>>()

    private var mCurrShowDialog: IDialog? = null // 当前显示的dialog
    private val mShowDialogEvent = MutableLiveData<Boolean>()

    private var isCanShowDialog: Boolean = true // 是否可以显示

    private val mObserver = Observer<Boolean> { show ->
        if ((mCurrShowDialog?.isShowing() == false || mCurrShowDialog == null) && show) {
            showNextDialog()
        }
    }

    init {
        host.lifecycle.addObserver(this)
        mShowDialogEvent.observe(host, mObserver)
    }

    /**
     * @param level 0：默认递增，否则{@link DialogLevel.LEVEL }
     *     level等于DialogLevel.LEVEL_CLEAR时，则清除所有，弹出当前这个
     */
    @MainThread
    override fun addDialog(dialog: IDialog, priority: Int, mode: DialogHandleMode) {

        when (mode) {
            // 1.清除所有的dialog
            DialogHandleMode.REMOVE_OTHERS -> {
                mDialogTask.clear()
                mCurrShowDialog?.dismiss()
                addDialog(dialog, priority, true)
            }

            DialogHandleMode.SAME_PRIORITY_REMOVE_OTHERS -> { // 2.移除相同优先级其他弹框
                mDialogTask.remove(priority)
                addDialog(dialog, priority, true)
            }
            DialogHandleMode.ALL_PRIORITY_FIRST -> {// 3.插队到头部 该模式下会忽略设置的优先级，优先级优先级统一设置为高优先级
                addDialog(dialog, DialogPriority.HIGH, true)
            }
            DialogHandleMode.SAME_PRIORITY_FIRST -> {// 4. 插队相同优先级的头部
                addDialog(dialog, priority, true)
            }
            else -> {// 添加到相同优先级的尾部
                addDialog(dialog, priority, false)
            }
        }
        showDialog()
    }

    private fun addDialog(dialog: IDialog, priority: Int, isHeader: Boolean) {
        var list = mDialogTask[priority]
        if (list == null) {
            list = LinkedList()
            mDialogTask[priority] = list
        }
        if (isHeader) {
            list.addFirst(dialog)
        } else list.add(dialog)
    }


    @MainThread
    override fun removeDialog(dialog: IDialog) {
        mDialogTask.values.forEach { list ->
            if (list.contains(dialog)) {
                list.remove(dialog)
            }
        }
    }

    override fun stopShowDialog() {
        isCanShowDialog = false
    }

    override fun startShowDialog() {
        isCanShowDialog = true
        showDialog()
    }

    override fun getCurrDialog(): IDialog? {
        return mCurrShowDialog

    }

    @MainThread
    fun release() {
        mShowDialogEvent.removeObserver(mObserver)
        mDialogTask.clear()
        mCurrShowDialog?.dismiss()
    }

    fun showDialog() {
        mShowDialogEvent.postValue(true)
    }

    private fun addDismissListener(dialog: IDialog) {
        dialog.setOnDialogDismissListener(this)
    }

    private fun showNextDialog() {
        if (mDialogTask.isEmpty() || !isCanShowDialog) return
        mDialogTask.forEach {
            val list = it.value
            if (list.isNotEmpty()) {
                val dialog = list.removeFirst()
                if (host.isFinishing) {
                    return@forEach
                }
                addDismissListener(dialog)
                dialog.show()
                mCurrShowDialog = dialog
                return@forEach
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        release()
    }

    override fun onDialogDismiss() {
        mCurrShowDialog?.let {
            it.setOnDialogDismissListener(null)
            removeDialog(it)
        }
        mCurrShowDialog = null
        showDialog()
    }
}