package com.kira.learning.xml.modules.supereditor

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.updatePadding
import com.kira.learning.databinding.ActivitySuperEditorBinding
import com.common.base.base.BaseActivity
import com.kira.learning.xml.modules.supereditor.viewmodel.SuperEditorViewModel
import com.kira.ui.core.extensions.applySystemWindowInsets
import com.kira.ui.core.extensions.decorFitsSystemWindows
import com.kira.ui.core.extensions.fullscreenMode
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class SuperEditorActivity : BaseActivity() {

    private val mViewModel by lazyViewModel<SuperEditorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val binding = ActivitySuperEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorFitsSystemWindows(false)
        window.fullscreenMode(mViewModel.fullScreenMode)

        binding.navHost.applySystemWindowInsets(false) { left, _, right, _ ->
            binding.navHost.updatePadding(left = left, right = right)
        }

        if (savedInstanceState == null) {
            mViewModel.handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mViewModel.handleIntent(intent)
    }
}