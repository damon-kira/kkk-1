package com.devoption.module.mode

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import com.devoption.module.DebugBaseFragment
import com.devoption.module.databinding.FragmentDebugPermissionBinding

internal class DebugPermissionFragment : DebugBaseFragment() {

    private lateinit var mBinding: FragmentDebugPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDebugPermissionBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvPermissions.movementMethod = ScrollingMovementMethod.getInstance()

        mBinding.tvSetting.setOnClickListener {
            startSetting(view.context)
        }
        getRequestPermissions()
    }

    private fun checkPermission(context: Context, permission: String): Boolean {
        return PermissionChecker.checkSelfPermission(
            context,
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getRequestPermissions() {
        val ctx = context ?: return
        val packageInfo =
            ctx.packageManager.getPackageInfo(ctx.packageName, PackageManager.GET_PERMISSIONS)
        val permissions = packageInfo.requestedPermissions
        val sb = StringBuilder()
        permissions?.forEach {
            val result = checkPermission(ctx, it)
            sb.append(it).append(" :").append("$result").append("\n")
        }

        mBinding.tvPermissions.text = sb.toString()
    }

    private fun startSetting(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts(
                "package",
                context.packageName, null
            )
            startActivity(intent)
        } catch (e: Exception) {
            try {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            } catch (e: Exception) {

            }
        }
    }
}