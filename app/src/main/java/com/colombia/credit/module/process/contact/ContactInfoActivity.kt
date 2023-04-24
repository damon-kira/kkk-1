package com.colombia.credit.module.process.contact

import android.os.Bundle
import android.view.View
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.R
import com.colombia.credit.bean.PhoneAndName
import com.colombia.credit.bean.req.ReqContactInfo
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.resp.RspContactInfo
import com.colombia.credit.databinding.ActivityContactInfoBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.ContactObtainHelper
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.permission.ContactPermission
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.util.DictionaryUtil
import com.colombia.credit.view.baseinfo.BaseInfoView
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactInfoActivity : BaseProcessActivity(), View.OnClickListener {

    private val mBinding by binding<ActivityContactInfoBinding>()

    private var isJumpSetting = false

    private val mViewModel by lazyViewModel<ContactViewModel>()

    private val mRelationship by lazy {
        DictionaryUtil.getRelationShip()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarListener(mBinding.processToolbar)
        mBinding.bivContact1.setDescMarginLeft(35f.dp())
        mBinding.bivContact2.setDescMarginLeft(35f.dp())
        mBinding.tvCommit.setBlockingOnClickListener(this)
        mBinding.bivRelationship.setBlockingOnClickListener(this)
        mBinding.bivContact1.setBlockingOnClickListener(this)
        mBinding.bivContact2.setBlockingOnClickListener(this)

        val cache = mViewModel.getCacheInfo()?.also { info ->
            info as ReqContactInfo
            if (mRelationship.containsKey(info.gQdRCJKOEJ)) {
                setBaseInfo(
                    mBinding.bivRelationship,
                    mRelationship[info.gQdRCJKOEJ],
                    info.gQdRCJKOEJ
                )
            }
            mBinding.bivContact1.setViewText(info.zAqGvHgHls.orEmpty())
            if (!info.ifunMf6ZLx.isNullOrEmpty()) {
                mBinding.bivContact1.setDesc(getString(R.string.mobile_s, info.ifunMf6ZLx))
                mBinding.bivContact1.tag = info.ifunMf6ZLx
            }

            mBinding.bivContact2.setViewText(info.VWHN.orEmpty())
            if (!info.fHdl.isNullOrEmpty()) {
                mBinding.bivContact2.setDesc(getString(R.string.mobile_s, info.fHdl))
                mBinding.bivContact2.tag = info.fHdl
            }
        }
        if (cache == null) {
            mViewModel.getInfo()
        }
    }

    override fun initObserver() {
        mViewModel.mInfoLiveData.observerNonSticky(this) { rspInfo ->
            if (rspInfo !is RspContactInfo) return@observerNonSticky
            rspInfo.Rwfbhdu1?.let { info ->
                if (mRelationship.containsKey(info.yYVUx)) {
                    setBaseInfo(
                        mBinding.bivRelationship,
                        mRelationship[info.yYVUx],
                        info.yYVUx
                    )
                }
                mBinding.bivContact1.setViewText(info.MGwL.orEmpty())
                if (!info.fTvY4N5.isNullOrEmpty()) {
                    mBinding.bivContact1.setDesc(getString(R.string.mobile_s, info.fTvY4N5))
                    mBinding.bivContact1.tag = info.fTvY4N5
                }

                mBinding.bivContact2.setViewText(info.dZgCz3.orEmpty())
                if (!info.fWvRFuMb.isNullOrEmpty()) {
                    mBinding.bivContact2.setDesc(getString(R.string.mobile_s, info.fWvRFuMb))
                    mBinding.bivContact2.tag = info.fWvRFuMb
                }
            }
        }
    }

    private var mCurrView: BaseInfoView? = null

    private fun reqPermission(baseInfoView: BaseInfoView) {
        mCurrView = baseInfoView
        PermissionHelper.reqPermission(this, arrayListOf(ContactPermission()), !isGp, {
            isJumpSetting = false
            openContacts(baseInfoView)
        }, {
            isJumpSetting = true
            jumpToAppSettingPage()
        })
    }

    override fun onRestart() {
        super.onRestart()
        if (isJumpSetting) {
            isJumpSetting = false
            mCurrView?.let {
                reqPermission(it)
            }
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.biv_relationship -> {
                showProcessSelectorDialog(
                    mBinding.bivRelationship.getTitle(),
                    mRelationship,
                    mBinding.bivRelationship.tag?.toString()
                ) {
                    mBinding.bivRelationship.setViewText(it.value)
                    mBinding.bivRelationship.tag = it.key
                }
            }
            R.id.biv_contact1 -> {
                reqPermission(mBinding.bivContact1)
            }
            R.id.biv_contact2 -> {
                reqPermission(mBinding.bivContact2)
            }
            R.id.tv_commit -> {
                uploadInfo()
            }
        }
    }

    /**
     * 打开系统联系人页面
     */
    private fun openContacts(infoView: BaseInfoView) {
        ContactObtainHelper.createObtainContact(this).openContact { _, data ->
            data ?: return@openContact
            setContactInfo(infoView, data)
        }
    }

    private fun setContactInfo(infoView: BaseInfoView, data: PhoneAndName) {
        val loginMobile = getMobile()
        if (data.phone.startsWith("57") && data.phone.length > 10) {
            val mobile = data.phone
            data.phone = mobile.substring(2, mobile.length)
        } else if (data.phone.startsWith("+57") && data.phone.length > 10) {
            val mobile = data.phone
            data.phone = mobile.substring(2, mobile.length)
        }

        // 是否与登录手机号是一个
        if (loginMobile == data.phone) {
            infoView.setError(R.string.error_mobile_same_login)
            return
        }
        infoView.setViewText(data.name)
        infoView.tag = data.phone
        infoView.setDesc(getString(R.string.mobile_s, data.phone))

        val contact1 = mBinding.bivContact1.tag?.toString().orEmpty()
        val contact2 = mBinding.bivContact2.tag?.toString().orEmpty()
        if (isSameNumber(contact1, contact2)) {
            infoView.setViewText("")
            infoView.setDesc("")
            infoView.tag = ""
            infoView.setError(R.string.error_mobile_same)
        }
    }

    override fun checkCommitInfo(): Boolean {
        return checkAndSetErrorHint(mBinding.bivRelationship)
            .and(checkAndSetErrorHint(mBinding.bivContact1, getString(R.string.error_contact_hint)))
            .and(checkAndSetErrorHint(mBinding.bivContact2, getString(R.string.error_contact_hint)))
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqContactInfo().also {
            it.gQdRCJKOEJ = mBinding.bivRelationship.tag?.toString()
            it.ifunMf6ZLx = mBinding.bivContact1.tag?.toString()
            it.zAqGvHgHls = mBinding.bivContact1.getViewText()

            it.fHdl = mBinding.bivContact2.tag?.toString()
            it.VWHN = mBinding.bivContact2.getViewText()
        }
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel
    override fun getNextType(): Int = TYPE_BANK

    override fun uploadSuccess() {
        if (isGp) {
            Launch.skipApplySuccessActivity(this)
            finish()
        } else super.uploadSuccess()
    }
}