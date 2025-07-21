package com.kira.learning.module.upload.real

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.databinding.ActivityQuickUploadBinding


class QuickUploadActivity : BaseActivity() {

    private val mBinding by binding<ActivityQuickUploadBinding>()
    private val notificationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(mBinding.root)

        checkPostNotificationsPermission()
        mBinding.singleFile.setOnClickListener {
            startActivity(Intent(this, QuickUploadSingleActivity::class.java))
        }
        mBinding.singleMultiple.setOnClickListener {
            startActivity(Intent(this, QuickUploadMultipleSingleFileActivity::class.java))
        }
        mBinding.multipleSimultaneous.setOnClickListener {
            startActivity(Intent(this, QuickUploadMultipleFilesSimultaneouslyActivity::class.java))
        }

    }

    private fun checkPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}