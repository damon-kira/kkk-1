package com.colombia.credit.module.process.contact

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.PhoneAndName
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqContactInfo
import com.colombia.credit.bean.resp.RspContactInfo
import com.colombia.credit.databinding.ActivityContactInfoBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.ContactObtainHelper
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.permission.HintDialog
import com.colombia.credit.util.DictionaryUtil
import com.colombia.credit.view.baseinfo.BaseInfoView
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StrMatchUtil
import com.util.lib.dp
import com.util.lib.isHide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactInfoActivity : BaseProcessActivity(), View.OnClickListener {

    private val mBinding by binding<ActivityContactInfoBinding>()

    private val mViewModel by lazyViewModel<ContactViewModel>()

    private val mRelationship by lazy {
        DictionaryUtil.getRelationShip()
    }

    private val mHintDialog by lazy {
        HintDialog(this).setMessage(getString(R.string.contact_no_hint))
            .showBtn(false)
            .setMessageTextSize(18f)
            .showTitle(HintDialog.TYPE_INVISIBLE)
    }

    private val mAutoHelper by lazy(LazyThreadSafetyMode.NONE) {
        object : ContactAutoHelper(mBinding) {
            override fun showItemDialog(index: Int) {
                when (index) {
                    ITEM_RELATIONSHIP -> onClick(mBinding.bivRelationship)
                    ITEM_CONTACT1 -> onClick(mBinding.bivContact1)
//                    ITEM_CONTACT2 -> onClick(mBinding.bivContact2)
                }
            }
        }
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
        if (!mContactResult) { // 是否进入联系人选择后无法带回信息
            showMobileEdit()
        }
        val nameInfilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (StrMatchUtil.isLetter(
                    source.toString().replace(" ".toRegex(), "")
                ) || StrMatchUtil.isSpace(source) && dstart != 0
            ) {
                return@InputFilter null
            } else {
                return@InputFilter ""
            }
        }
        val nameFilters = arrayOf(nameInfilter)
        mBinding.bivContact1.setFilters(nameFilters)
        mBinding.bivContact2.setFilters(nameFilters)

        mViewModel.getCacheInfo()?.also { info ->
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
                showMobileEdit()
                mBinding.bivContact1Number.setViewText(info.ifunMf6ZLx.orEmpty())
            }

            mBinding.bivContact2.setViewText(info.VWHN.orEmpty())
            if (!info.fHdl.isNullOrEmpty()) {
                showMobileEdit()
                mBinding.bivContact2Number.setViewText(info.fHdl.orEmpty())
            }
        }
        mViewModel.getInfo()
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
                    showMobileEdit()
                    mBinding.bivContact1Number.setViewText(info.fTvY4N5.orEmpty())
                }

                mBinding.bivContact2.setViewText(info.dZgCz3.orEmpty())
                if (!info.fWvRFuMb.isNullOrEmpty()) {
                    showMobileEdit()
                    mBinding.bivContact2Number.setViewText(info.fWvRFuMb.orEmpty())
                }
            }
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        mAutoHelper.clearFocus()
        when (v.id) {
            R.id.biv_relationship -> {
                showProcessSelectorDialog(
                    mBinding.bivRelationship.getTitle(),
                    mRelationship,
                    mBinding.bivRelationship.tag?.toString()
                ) {
                    mBinding.bivRelationship.setViewText(it.value)
                    mBinding.bivRelationship.tag = it.key
                    mAutoHelper.startCheckNext()
                }
            }
            R.id.biv_contact1 -> {
                openContacts(mBinding.bivContact1, mBinding.bivContact1Number)
            }
            R.id.biv_contact2 -> {
                openContacts(mBinding.bivContact2, mBinding.bivContact2Number)
            }
            R.id.tv_commit -> {
                uploadInfo()
            }
        }
    }

    /**
     * 打开系统联系人页面
     */
    private fun openContacts(infoView: BaseInfoView, mobileInfoView: BaseInfoView) {
        ContactObtainHelper.createObtainContact(this).openContact { _, data ->
            data ?: return@openContact
            setContactInfo(infoView, mobileInfoView, data)
        }
    }

    private fun setContactInfo(
        infoView: BaseInfoView,
        mobileInfoView: BaseInfoView,
        data: PhoneAndName
    ) {
        val loginMobile = getMobile()
        // 选择的手机号为空
        val tempMobile = data.phone
        if (tempMobile.isNullOrEmpty()) {
            mContactResult = false
            showMobileEdit()
            return
        }
        if (loginMobile.contains(data.phone) || data.phone.contains(loginMobile)) {
            clearInfo(infoView, mobileInfoView)
            infoView.setError(R.string.error_mobile_same_login)
            return
        }
        infoView.setViewText(data.name)
        infoView.setCanEdit(true)
        mobileInfoView.show()
        mobileInfoView.setViewText(data.phone)

        val contact1 = mBinding.bivContact1Number.getViewText()
        val contact2 = mBinding.bivContact2Number.getViewText()
        if (isSameNumber(contact1, contact2)) {
            clearInfo(infoView, mobileInfoView)
            infoView.setError(R.string.error_mobile_same)
            return
        }
        if (data.name.isEmpty()) {
            infoView.setError(R.string.contact_error_name)
            return
        }

        if (infoView != mBinding.bivContact2) {
            mAutoHelper.startCheckNext()
        }
    }

    private fun showMobileEdit() {
        val hintName = getString(R.string.contact_input_name)
        if (mBinding.bivContact1Number.isHide()) {
            mBinding.bivContact1.setEditHint(hintName)
            mBinding.bivContact1.setCanEdit(true)
            mBinding.bivContact1Number.show()
        }
        if (mBinding.bivContact2Number.isHide()) {
            mBinding.bivContact2.setEditHint(hintName)
            mBinding.bivContact2.setCanEdit(true)
            mBinding.bivContact2Number.show()
        }
    }

    private fun clearInfo(infoView: BaseInfoView, mobileInfoView: BaseInfoView) {
        infoView.setViewText("")
        infoView.setDesc("")
        mobileInfoView.setViewText("")
    }

    override fun checkCommitInfo(): Boolean {
        var result = checkAndSetErrorHint(mBinding.bivRelationship)
        result = if (mBinding.bivContact1Number.isHide()) {
            result.and(checkAndSetErrorHint(mBinding.bivContact1, getString(R.string.error_contact_hint)))
                .and(checkAndSetErrorHint(mBinding.bivContact2, getString(R.string.error_contact_hint)))
        } else {
            result.and(checkInfoView(mBinding.bivContact1, getString(R.string.contact_error_name)))
                .and(checkInfoView(mBinding.bivContact2, getString(R.string.contact_error_name)))

            val check1 = checkMobileText(mBinding.bivContact1Number)
            val check2 = checkMobileText(mBinding.bivContact2Number)
            if (check1 && check2) {
                result = result.and(!checkContactSame())
            }
            result.and(check1).and(check2)
        }
        return result
    }

    private fun checkMobileText(infoView: BaseInfoView): Boolean {
        val text = infoView.getViewText()
         val checkLength = (text.length in 10..20).also {result ->
            if (!result) {
                infoView.setError(R.string.contact_error_mobile)
            }
        }
        // 长度校验
        if (!checkLength) return false
        // 是否与登录手机号一样
        val loginMobile = getMobile()
        if (text.contains(loginMobile) || loginMobile.contains(text)) {
            infoView.setViewText("")
            infoView.setError(R.string.error_mobile_same_login)
            return false
        }
        return true
    }

    // 是否两个手机号一样
    private fun checkContactSame(): Boolean {
        val mobile1 = mBinding.bivContact1Number.getViewText()
        val mobile2 = mBinding.bivContact2Number.getViewText()
        if (mobile1.isNotEmpty() && mobile2.isNotEmpty() && (mobile2.contains(mobile1) || mobile1.contains(mobile2))) {
            mBinding.bivContact1Number.setError(R.string.error_mobile_same)
            mBinding.bivContact2Number.setError(R.string.error_mobile_same)
            return true
        } else {
            mBinding.bivContact2Number.clearTextError()
            mBinding.bivContact1Number.clearTextError()
        }
        return false
    }

    private fun checkInfoView(infoView: BaseInfoView, errorHint: String): Boolean {
        val text = infoView.getViewText()
        var result = true
        if (text.isNullOrEmpty()) {
            result = false
            infoView.setError(errorHint)
        }
        return result
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqContactInfo().also {
            it.gQdRCJKOEJ = mBinding.bivRelationship.tag?.toString()
            it.ifunMf6ZLx = mBinding.bivContact1Number.getViewText()
            it.zAqGvHgHls = mBinding.bivContact1.getViewText()

            it.fHdl = mBinding.bivContact2Number.getViewText()
            it.VWHN = mBinding.bivContact2.getViewText()
        }
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel
    override fun getNextType(): Int = STEP4

    override fun uploadSuccess() {
        if (isGpAccount()) {
            Launch.skipApplySuccessActivity(this)
            finish()
        } else super.uploadSuccess()
    }
}