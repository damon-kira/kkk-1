package com.kira.ui.feature.editor.ui.manager

import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.kira.ui.feature.editor.databinding.FragmentMiniEditorBinding
import com.kira.ui.feature.editor.ui.adapter.KeyAdapter
import com.kira.ui.feature.settings.domain.model.KeyModel

class MiniCodingKeyboardManager(private val listener: Listener) {

    var mode: Mode = Mode.NONE
        set(value) {
            field = value
            updateKeyboard()
        }

    private lateinit var binding: FragmentMiniEditorBinding

    private var keyAdapter: KeyAdapter? = null

    fun bind(binding: FragmentMiniEditorBinding) {
        this.binding = binding
        updateKeyboard()

        binding.keyboardRecycler.setHasFixedSize(true)
        binding.keyboardRecycler.adapter = KeyAdapter { keyModel ->
            listener.onKeyButton(keyModel.value)
        }.also {
            keyAdapter = it
        }

        binding.keyboardToolOpen.setOnClickListener { listener.onOpenButton() }
        binding.keyboardToolSave.setOnClickListener { listener.onSaveButton() }
        binding.keyboardToolClose.setOnClickListener { listener.onCloseButton() }
        binding.keyboardToolUndo.setOnClickListener { listener.onUndoButton() }
        binding.keyboardToolRedo.setOnClickListener { listener.onRedoButton() }
    }

    fun submitList(keys: List<KeyModel>) {
        keyAdapter?.submitList(keys)
    }

    private fun updateKeyboard() {
        when (mode) {
            Mode.KEYBOARD -> with(binding) {
                keyboardBackground.isVisible = true
                keyboardDivider.isVisible = true
                keyboardRecycler.isVisible = true
                keyboardToolOpen.isInvisible = true
                keyboardToolSave.isInvisible = true
                keyboardToolClose.isInvisible = true
                keyboardToolUndo.isInvisible = true
                keyboardToolRedo.isInvisible = true
                keyboardSwap.isVisible = true
            }

            Mode.TOOLS -> with(binding) {
                keyboardBackground.isVisible = true
                keyboardDivider.isVisible = true
                keyboardRecycler.isInvisible = true
                keyboardToolOpen.isVisible = true
                keyboardToolSave.isVisible = true
                keyboardToolClose.isVisible = true
                keyboardToolUndo.isVisible = true
                keyboardToolRedo.isVisible = true
                keyboardSwap.isVisible = true
            }

            Mode.NONE -> with(binding) {
                keyboardBackground.isGone = true
                keyboardDivider.isGone = true
                keyboardRecycler.isGone = true
                keyboardToolOpen.isGone = true
                keyboardToolSave.isGone = true
                keyboardToolClose.isGone = true
                keyboardToolUndo.isGone = true
                keyboardToolRedo.isGone = true
                keyboardSwap.isGone = true
            }
        }
    }

    interface Listener {
        fun onKeyButton(char: Char): Boolean
        fun onOpenButton(): Boolean
        fun onSaveButton(): Boolean
        fun onCloseButton(): Boolean
        fun onUndoButton(): Boolean
        fun onRedoButton(): Boolean
    }

    enum class Mode {
        KEYBOARD,
        TOOLS,
        NONE,
    }
}