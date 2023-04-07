package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.bean.resp.AppUpgradeInfo
import com.colombia.credit.databinding.DialogAppUpgradeBinding
import com.colombia.credit.manager.Launch
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.ifShow

class AppUpgradeDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogAppUpgradeBinding>()

    private var mJumpAddress: String? = null

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.88f, WRAP)
        mBinding.tvUpdate.setBlockingOnClickListener {
            Launch.skipAppStore(mJumpAddress, true)
        }
        mBinding.aivClose.setBlockingOnClickListener { dismiss() }
    }

//    var bt35AvNbu: Int = 0 //是否需要更新 1是 0否
//    var iteL2w: String? = null // 最新版本号
//    var CHDnt3v: Int = 0 //   当前版本是否最新版本app版本号 1是 0否
//    var aTzLhoFtcl: String? = null//下载链接
//    var Fjg55aR0kc: String? = null // 版本描述

    fun setAppUpdateInfo(info: AppUpgradeInfo): AppUpgradeDialog {
        mBinding.tvTitle.text = info.iteL2w
        mBinding.tvText1.text = info.Fjg55aR0kc
        mJumpAddress = info.aTzLhoFtcl
        mBinding.aivClose.ifShow(info.CHDnt3v != 2)
        return this
    }
}