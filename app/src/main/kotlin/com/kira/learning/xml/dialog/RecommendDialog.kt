package com.kira.learning.xml.dialog

import android.content.Context
import android.os.Bundle
import com.kira.learning.R
import com.kira.learning.model.res.RepeatProductInfo
import com.kira.learning.databinding.DialogRecommenBinding
import com.kira.learning.xml.expand.getUnitString
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.glide.GlideUtils
import com.common.lib.viewbinding.binding
import com.util.lib.dp


typealias ClickRecommend = ((RepeatProductInfo) -> Unit)

// 复盘推荐弹窗
class RecommendDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogRecommenBinding>()

    private var mClickRecommend: ClickRecommend? = null

    private var mInfo: RepeatProductInfo? = null

    private val mTexts: ArrayList<Int> by lazy {
        ArrayList<Int>().also {
            it.add(R.string.recommend_text1)
            it.add(R.string.recommend_text2)
            it.add(R.string.recommend_text3)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDisplaySize(0.88f, WRAP)
        setCanceledOnTouchOutside(false)
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    fun setInfo(info: RepeatProductInfo?) {
        info ?: return
        this.mInfo = info
        mBinding.tvAmount.text = getUnitString(info.g7tzi)
        mBinding.tvLeft.text = info.xXgaK4
        mBinding.tvName.text = info.S9ig78H
        val periods = StringBuilder()
        val text = periods.append(info.D9hR.orEmpty()).append("~").append(info.cQ75eX5.orEmpty()).toString()
        mBinding.tvRight.text = text

        GlideUtils.loadCornerImageFromUrl(context,
            info.Gk9MGh.orEmpty(),
            mBinding.aivIcon,
            4.dp(),
            R.drawable.ic_normal_image
        )
    }

    fun setClickListener(click: ClickRecommend): RecommendDialog{
        this.mClickRecommend = click
        return this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.tvBtn.setBlockingOnClickListener {
            mInfo?.let {info ->
                mClickRecommend?.invoke(info)
            }
        }
        val index = (Math.random() * 100).toInt() % mTexts.size
        mBinding.tvRecommDesc.setText(mTexts[index])
    }
}