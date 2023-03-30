package com.devoption.finance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.okredito.devoptions.R
import com.devoption.finance.mode.DebugRootFragment

/**
 * Created by weishl on 2021/7/9
 *
 */
internal class DebugActivity : AppCompatActivity() {

    private val mRootFragment = DebugRootFragment()

    private var mCurrTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        switchFragment(mRootFragment, false)
    }

    fun switchFragment(fragmentDebug: DebugBaseFragment, addToBack:Boolean = false) {
        mCurrTag = DebugFragmentUtils.switchFragment(supportFragmentManager, R.id.fl_debug_root, fragmentDebug, mCurrTag, addToBack)
    }

    fun getCurrFragment(): DebugBaseFragment? {
        return DebugFragmentUtils.getCurrFragment(supportFragmentManager, R.id.fl_debug_root, mCurrTag)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getCurrFragment()?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getCurrFragment()?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}