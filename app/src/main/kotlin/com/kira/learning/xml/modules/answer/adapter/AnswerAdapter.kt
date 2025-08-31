package com.kira.learning.xml.modules.answer.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.kira.learning.R
import com.kira.learning.bean.QuestionProcessInfo
import com.kira.learning.xml.adapter.BaseRecyclerViewAdapter
import com.kira.learning.xml.adapter.BaseViewHolder

class AnswerAdapter(
    items: ArrayList<QuestionProcessInfo>,
    private val recyclerView: RecyclerView
) :
    BaseRecyclerViewAdapter<QuestionProcessInfo>(
        items,
        R.layout.item_answer
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        val btnToggle: Button = holder.getView(R.id.btn_answer_switch)
        val itemAnswer: ConstraintLayout = holder.getView(R.id.item_answer)
        btnToggle.setOnClickListener {
            val expandableGroup = holder.getView<Group>(R.id.expandable_group)
            val isExpanded = expandableGroup.isVisible
            expandableGroup.visibility = if (isExpanded) View.GONE else View.VISIBLE
            // 添加动画（可选）
            TransitionManager.beginDelayedTransition(itemAnswer, AutoTransition())
        }
        return holder
    }

    override fun convert(
        holder: BaseViewHolder,
        item: QuestionProcessInfo,
        position: Int
    ) {
//        bindExpandView(holder)
//        val ec:ExpandableCardView = holder.getView<ExpandableCardView>(R.id.expand_cardview_item_answer)
//        ec.setTitle(titleText = "Quesion ${position + 1}")
        val rvQuestion: RecyclerView = holder.getView<RecyclerView>(R.id.layout_question_rv)
        if (rvQuestion.layoutManager == null) {
            rvQuestion.layoutManager = LinearLayoutManager(recyclerView.context)
        }
        val questionAdapter = QuestionAdapter(item.steps, rvQuestion)
        Log.e("adapteradapter", "convert: ${item.steps.size}")
        rvQuestion.adapter = questionAdapter
//        questionAdapter.notifyDataSetChanged()
    }

    private fun bindExpandView(holder: BaseViewHolder) {
//        val btnToggle: Button = holder.getView(R.id.btn_answer_switch)
//        val itemAnswer: ConstraintLayout = holder.getView(R.id.item_answer)
//        btnToggle.setOnClickListener {
//            val expandableGroup = holder.getView<Group>(R.id.expandable_group)
//            val isExpanded = expandableGroup.isVisible
//            expandableGroup.visibility = if (isExpanded) View.GONE else View.VISIBLE
//            // 添加动画（可选）
//            TransitionManager.beginDelayedTransition(itemAnswer, AutoTransition())
//        }
    }


    override fun getItemId(position: Int): Long {
        return position * 1L
    }

}