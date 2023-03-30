package com.colombia.credit.permission

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.colombia.credit.R
import com.colombia.credit.databinding.DlgPermissionLayoutBinding
import com.colombia.credit.expand.toast
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.util.lib.dp
import com.util.lib.shape.ShapeImpl

class PermissionDialog(context: Context): DefaultDialog(context) {

    private lateinit var mContentView: LinearLayout

    init {
        initViews()
    }

    private lateinit var mBinding: DlgPermissionLayoutBinding
    private fun initViews() {
        mBinding = DlgPermissionLayoutBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setDisplaySize(0.9f, 0.8f)
        mContentView = mBinding.dlContentLl

        mBinding.llContainer.background = ShapeImpl(context).shapeSolid().color(Color.WHITE)
            .then().shapeCorners().radius(8.dp())
            .getShape()
    }

    fun setData(
        clickListener: () -> Unit = {},
        deniedList: List<AbsPermissionEntity>,
        agreementClick: () -> Unit
    ) {
        showPermissionItem(mContentView, deniedList)
        mBinding.okBtn.setOnClickListener {
            if (!mBinding.aivProtocol.isSelected) {
                toast(R.string.permission_check_hint)
                return@setOnClickListener
            }
            clickListener()
        }

        initAgreement(agreementClick)
    }

    private fun initAgreement(agreementClick: () -> Unit) {
//        val agreementParam = context.getString(R.string.permission_privacy_policy_link)
//        val hint = context.getString(R.string.permission_dlg_text1, agreementParam)
//        val span = SpannableStringBuilder(hint)
//        span.append("\u200b")
//        setAgreementClickableSpan(R.color.colorPrimary, context, span, agreementParam) {
//            agreementClick.invoke()
////            Launch.skipWebViewActivity(context, H5UrlManager.URL_PRIVACY_PROTOCOL)
//        }
//        tv_permission_hint.highlightColor = ContextCompat.getColor(context, R.color.transparent)
//        tv_permission_hint.movementMethod = LinkMovementMethod.getInstance()
//        tv_permission_hint.text = span

        mBinding.aivProtocol.setBlockingOnClickListener {
            mBinding.aivProtocol.isSelected = !mBinding.aivProtocol.isSelected
        }
    }

    private fun showPermissionItem(
        contentView: LinearLayout,
        deniedList: List<AbsPermissionEntity>
    ) {
        val context = contentView.context
        val titleResIdSet = LinkedHashSet<Int>()
        deniedList.forEach {
            val titleResId: Int = it.getHintIfNoPermission().first
            val subTitleResId = it.getHintIfNoPermission().second
            if (titleResIdSet.contains(titleResId) || titleResId == 0)
                return@forEach
            titleResIdSet.add(titleResId)
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.dlg_permission_item_layout, null)
            itemView.findViewById<TextView>(R.id.item_title_tv).text = context.getString(titleResId)
            val tvSubtitle = itemView.findViewById<TextView>(R.id.item_subtitle_tv)
            tvSubtitle.text = Html.fromHtml(context.getString(subTitleResId))
            contentView.addView(itemView)
        }
        titleResIdSet.clear()
    }
}