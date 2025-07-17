

package com.kira.ui.feature.editor.ui.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.kira.ui.editorkit.model.ColorScheme
import com.kira.ui.editorkit.plugin.autocomplete.SuggestionAdapter
import com.kira.ui.feature.editor.R
import com.kira.ui.feature.editor.databinding.ItemSuggestionBinding
import com.kira.ui.language.base.model.Suggestion

class AutoCompleteAdapter(
    context: Context,
    private val colorScheme: ColorScheme,
) : SuggestionAdapter(context, R.layout.item_suggestion) {

    override fun createViewHolder(parent: ViewGroup): SuggestionViewHolder {
        return AutoCompleteViewHolder.create(parent, colorScheme.suggestionQueryColor)
    }

    class AutoCompleteViewHolder(
        private val binding: ItemSuggestionBinding,
        private val queryColor: Int,
    ) : SuggestionViewHolder(binding.root) {

        companion object {

            fun create(parent: ViewGroup, queryColor: Int): SuggestionViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemSuggestionBinding.inflate(inflater, parent, false)
                return AutoCompleteViewHolder(binding, queryColor)
            }
        }

        override fun bind(suggestion: Suggestion?, query: String) {
            if (suggestion != null) {
                val spannable = SpannableString(suggestion.toString())
                if (query.length < spannable.length) {
                    spannable.setSpan(
                        ForegroundColorSpan(queryColor),
                        0,
                        query.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }

                binding.itemType.isVisible = suggestion.type != Suggestion.Type.NONE
                binding.itemType.text = suggestion.type.value
                binding.itemSuggestion.text = spannable
                binding.itemReturnType?.text = suggestion.returnType
            }
        }
    }
}