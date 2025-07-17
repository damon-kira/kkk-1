package com.kira.ui.uikit.extensions.ui.uikit

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.kira.ui.uikit.databinding.DialogColorPickerBinding
import com.kira.ui.uikit.extensions.ui.uikit.extensions.toHexString
import com.skydoves.colorpickerview.listeners.ColorListener

abstract class ColorPickerDialog : DialogFragment() {

    abstract val titleRes: Int
    abstract val positiveRes: Int
    abstract val negativeRes: Int
    abstract val initialColor: String

    private var updatingText = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogColorPickerBinding.inflate(layoutInflater)
        binding.colorPicker.attachAlphaSlider(binding.alphaSlideBar)
        binding.colorPicker.attachBrightnessSlider(binding.brightnessSlideBar)
        binding.colorPicker.colorListener = ColorListener { color, fromUser ->
            if (fromUser) {
                updatingText = true
                binding.colorInput.setText(color.toHexString())
                updatingText = false
            }
        }
        binding.colorInput.doOnTextChanged { text, _, _, _ ->
            if (!updatingText) {
                try {
                    val color = Color.parseColor(text.toString())
                    binding.colorPicker.selectByHsvColor(color)
                } catch (e: Exception) {
                    // ignored
                }
            }
        }
        binding.colorPicker.doOnPreDraw {
            binding.colorPicker.setInitialColor(initialColor.toColorInt())
            binding.colorInput.setText(initialColor)
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(titleRes)
            .setView(binding.root)
            .setPositiveButton(positiveRes) { _, _ -> onColorSelected(binding.colorPicker.color) }
            .setNegativeButton(negativeRes, null)
            .create()
    }

    abstract fun onColorSelected(color: Int)
}