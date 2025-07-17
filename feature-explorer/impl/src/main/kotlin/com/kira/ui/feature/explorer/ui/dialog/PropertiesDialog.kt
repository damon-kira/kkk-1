

package com.kira.ui.feature.explorer.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.data.utils.toReadableDate
import com.kira.ui.feature.explorer.data.utils.toReadableSize
import com.kira.ui.feature.explorer.databinding.DialogPropertiesBinding
import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.Permission
import com.kira.ui.filesystem.base.utils.hasFlag
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertiesDialog : DialogFragment() {

    private val navArgs by navArgs<PropertiesDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fileModel = Gson().fromJson(navArgs.data, FileModel::class.java) // FIXME

        val readableSize = fileModel.size.toReadableSize()
        val readableDate = fileModel.lastModified
            .toReadableDate(getString(R.string.properties_date_format))

        val binding = DialogPropertiesBinding.inflate(layoutInflater)

        binding.textFileName.setText(fileModel.name)
        binding.textFilePath.setText(fileModel.path)
        binding.textLastModified.setText(readableDate)
        binding.textFileSize.setText(readableSize)

        binding.readable.isChecked = fileModel.permission hasFlag Permission.OWNER_READ
        binding.writable.isChecked = fileModel.permission hasFlag Permission.OWNER_WRITE
        binding.executable.isChecked = fileModel.permission hasFlag Permission.OWNER_EXECUTE

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_properties)
            .setView(binding.root)
            .create()
    }
}