package com.colombia.credit.permission

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.colombia.credit.Launch
import com.colombia.credit.R
import com.colombia.credit.databinding.DlgPermissionLayoutBinding
import com.colombia.credit.manager.H5UrlManager
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.dp
import com.util.lib.shape.ShapeImpl

class PermissionDialog(context: Context): DefaultDialog(context) {

    private val mBinding by binding<DlgPermissionLayoutBinding>()

    init {
        initViews()
    }
    private lateinit var mContentView: LinearLayout

    private fun initViews() {
        setContentView(mBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setDisplaySize(0.9f, 0.8f)
        mContentView = mBinding.dlContentLl

        mBinding.llContainer.background = ShapeImpl(context).shapeSolid().color(Color.WHITE)
            .then().shapeCorners().radius(8.dp())
            .getShape()

        initAgreement()

        mBinding.aivProtocol.setBlockingOnClickListener {
            mBinding.aivProtocol.isSelected = !mBinding.aivProtocol.isSelected
            mBinding.okBtn.isEnabled = mBinding.aivProtocol.isSelected
        }
    }

    fun setData(
        clickListener: () -> Unit = {},
        deniedList: List<AbsPermissionEntity>
    ) {
        showPermissionItem(mContentView, deniedList)
        mBinding.okBtn.setOnClickListener {
            clickListener()
        }
    }

    private fun initAgreement() {
        val agreementParam = context.getString(R.string.permission_album)
        val hint = context.getString(R.string.permission_protocol, agreementParam)
        val span = SpannableStringBuilder(hint)
        span.append("\u200b")
        setAgreementClickableSpan(R.color.colorPrimary, context, span, agreementParam) {
            Launch.skipWebViewActivity(context, H5UrlManager.URL_PRIVACY_PROTOCOL)
        }
        mBinding.tvProtocol.movementMethod = LinkMovementMethod.getInstance()
        mBinding.tvProtocol.text = span

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