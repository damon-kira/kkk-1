package com.colombia.credit.dialog

import android.content.Context
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.databinding.DialogRecommenBinding
import com.colombia.credit.expand.getUnitString
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding


typealias ClickRecommend = ((RepeatProductInfo) -> Unit)

// 复贷推荐弹窗
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