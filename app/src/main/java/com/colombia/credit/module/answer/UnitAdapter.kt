package com.colombia.credit.module.answer

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R

class UnitAdapter(private val items: List<CourseUnit>) :
    RecyclerView.Adapter<UnitAdapter.UnitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_unit, parent, false)
        return UnitViewHolder(view)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        holder.bind(items[position])


    }

    override fun getItemCount() = items.size

    class UnitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.unitTitle)
        private val skillsContainer: LinearLayout = view.findViewById(R.id.skillsContainer)
        private val quizButton: Button = view.findViewById(R.id.quizButton)

        fun bind(unit: CourseUnit) {
            titleText.text = unit.title
            
            // 动态生成技能条目
            skillsContainer.removeAllViews()
            unit.skills.forEach { skill ->
                TextView(itemView.context).apply {
                    text = skill
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_check_circle, 0, 0, 0
                    )
                    compoundDrawablePadding = 16.dpToPx()
                    setTextColor(Color.WHITE)
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply { bottomMargin = 8.dpToPx() }
                }.also { skillsContainer.addView(it) }
            }

            // 控制测验按钮可见性
            quizButton.visibility = if (unit.quizEnabled) View.VISIBLE else View.GONE
        }

        private fun Int.dpToPx() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 
            this.toFloat(), 
            itemView.context.resources.displayMetrics
        ).toInt()
    }

}