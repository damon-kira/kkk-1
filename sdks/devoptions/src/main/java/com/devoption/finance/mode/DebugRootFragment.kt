package com.devoption.finance.mode

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.okredito.devoptions.*
import com.devoption.finance.DebugActivity
import com.devoption.finance.DebugBaseFragment
import com.devoption.finance.DebugPackagerUtils
import com.devoption.finance.DebugSPKey
import com.devoption.finance.DebugSPUtils
import com.okredito.devoptions.databinding.FragmentRootBinding
import java.lang.IllegalStateException

/**
 * Created by weishl on 2021/7/9
 *
 */
internal class DebugRootFragment : DebugBaseFragment(), View.OnClickListener {

    private var _binding: FragmentRootBinding? = null
    private val binding = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_root, container, false)
        _binding = FragmentRootBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkbox.isChecked = DebugSPUtils.getBoolean(DebugSPKey.KEY_BEHAVIOR, true)
        binding.btnToken.setOnClickListener(this)
        binding.btnApi.setOnClickListener(this)
        binding.btnRestart.setOnClickListener(this)
        binding.btnCrash.setOnClickListener(this)
        binding.btnPermission.setOnClickListener(this)

        val intent = Intent()
        intent.setPackage(context?.packageName)

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
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
                val token = binding.editToken.text.toString()
                if (token.isNullOrEmpty()) {
                    Toast.makeText(context, "请输入token", Toast.LENGTH_SHORT).show()
                    return
                }
                DebugSPUtils.setString(DebugSPKey.KEY_TOKEN, token)
            }
            R.id.btn_api -> {
                switchFragment(DebugApiHostFragment())
            }
            R.id.btn_restart -> {
//                PackagerUtils.clearCache()
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