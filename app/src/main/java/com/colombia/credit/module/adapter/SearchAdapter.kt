package com.colombia.credit.module.adapter

import android.widget.Filter
import android.widget.Filterable
import com.colombia.credit.bean.SearchInfo
import org.jetbrains.annotations.NotNull

abstract class SearchAdapter<T>(@NotNull private val items: ArrayList<T>, layoutRes: Int) :
    BaseRecyclerViewAdapter<T>(items, layoutRes),
    Filterable {

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                val list: ArrayList<T> = arrayListOf()
                if (charString.isNullOrEmpty()) {
                    list.addAll(items)
                } else {
                    for (str in items) {
                        if (str is SearchInfo && str.match(charString)) {
                            list.add(str)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = list

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val values = results?.values
                if (values != null) {
                    currentItems = values as ArrayList<T>
                    notifyDataSetChanged()
                }
            }
        }

    }
}