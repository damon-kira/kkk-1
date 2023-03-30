package com.common.lib.dialog

import androidx.annotation.IntRange
import com.common.lib.dialog.DialogPriority.HIGH
import com.common.lib.dialog.DialogPriority.LOW

/**
 * Created by weishl on 2020/4/8
 *
 */
interface IDialogTask {

    fun addDialog(dialog: IDialog, @IntRange(from = HIGH.toLong(),to = LOW.toLong()) priority: Int = DialogPriority.NORMAL, mode: DialogHandleMode = DialogHandleMode.SAME_PRIORITY_LAST)

    fun removeDialog(dialog: IDialog)

    fun stopShowDialog()

    fun startShowDialog()

    fun getCurrDialog(): IDialog?

}


enum class DialogHandleMode{
    REMOVE_OTHERS,//移除其他对话框
    SAME_PRIORITY_REMOVE_OTHERS,//移除相同优先级的其他对话框
    ALL_PRIORITY_FIRST,//添加的对话框 添加到当前对话框队列的第一位
    SAME_PRIORITY_FIRST, //添加的对话框 添加到当前优先级的对话框该队列的第一位
    SAME_PRIORITY_LAST//添加的对话框 添加到当前优先级的对话框该队列的尾部
}

object DialogPriority {
    const val HIGH = -1 // 高优先级
    const val NORMAL =  10 // 普通优先级
    const val LOW = 100 // 低优先级
}