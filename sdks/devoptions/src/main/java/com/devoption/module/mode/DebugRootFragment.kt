package com.devoption.module.mode

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.devoption.module.*
import com.devoption.module.databinding.FragmentRootBinding

internal class DebugRootFragment : DebugBaseFragment(), View.OnClickListener {

    private lateinit var mBinding: FragmentRootBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRootBinding.inflate(inflater)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.checkbox.isChecked = DebugSPUtils.getBoolean(DebugSPKey.KEY_BEHAVIOR, true)
        mBinding.btnToken.setOnClickListener(this)
        mBinding.btnApi.setOnClickListener(this)
        mBinding.btnRestart.setOnClickListener(this)
        mBinding.btnCrash.setOnClickListener(this)
        mBinding.btnPermission.setOnClickListener(this)

        val intent = Intent()
        intent.setPackage(context?.packageName)

        mBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            DebugSPUtils.setBoolean(DebugSPKey.KEY_BEHAVIOR, isChecked)
        }
    }

    private fun switchFragment(fragment: DebugBaseFragment) {
        (activity as? DebugActivity)?.switchFragment(fragment, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        (activity as? DebugActivity)?.getCurrFragment()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.btn_token -> {
                // 保存token
                val token = mBinding.editToken.text.toString()
                if (token.isEmpty()) {
                    Toast.makeText(context, "请输入token", Toast.LENGTH_SHORT).show()
                    return
                }
                DebugSPUtils.setString(DebugSPKey.KEY_TOKEN, token)
            }
            R.id.btn_api -> {
                switchFragment(DebugApiHostFragment())
            }
            R.id.btn_restart -> {
                if (DebugModeHolder.isApiChanged()) {
                    DebugPackagerUtils.clearCache()
                }
                DebugPackagerUtils.restartApp()
            }
            R.id.btn_crash -> {
                throw IllegalStateException("debug mode test ..")
            }
            R.id.btn_permission -> {
                switchFragment(DebugPermissionFragment())
            }
        }
    }
}