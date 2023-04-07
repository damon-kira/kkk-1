package com.colombia.credit.module.setting

import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivitySettingBinding
import com.colombia.credit.dialog.LogoutDialog
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.toast
import com.colombia.credit.module.home.MainEvent
import com.colombia.credit.permission.HintDialog
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity() {

    private val mBinding by binding<ActivitySettingBinding>()

    private val mViewModel by lazyViewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val dialog = HintDialog(this).setTitleText(getString(R.string.delete_hint_title))
            .setMessage(getString(R.string.delete_hint))
            .setBtnText(getString(R.string.delete_btn))

        mBinding.tvDelete.setBlockingOnClickListener {
            dialog.setOnClickListener {
                toast("清除数据成功")
            }
        }
        mBinding.tvLogout.setBlockingOnClickListener {
            LogoutDialog(this).setLogoutListener {
                mViewModel.logout()
            }.show()
        }

        mViewModel.mLogoutLivedata.observerNonSticky(this) {
            if (it.isSuccess()) {
                LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
                finish()
            } else {
                it.ShowErrorMsg()
            }
        }
    }
}