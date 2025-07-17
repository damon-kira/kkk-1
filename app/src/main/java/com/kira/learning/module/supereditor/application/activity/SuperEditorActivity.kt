package com.kira.learning.module.supereditor.application.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.updatePadding
import com.kira.ui.core.extensions.applySystemWindowInsets
import com.kira.ui.core.extensions.decorFitsSystemWindows
import com.kira.ui.core.extensions.fullscreenMode
import com.common.lib.base.BaseActivity
import com.kira.learning.databinding.ActivitySuperEditorBinding
import com.kira.learning.module.supereditor.application.viewmodel.SuperEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuperEditorActivity : BaseActivity() {

//    @Inject
//    lateinit var inAppUpdate: InAppUpdate

//    private val superEditorViewModel by viewModels<SuperEditorViewModel>()
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

//        inAppUpdate.checkForUpdates(this) {
//            Snackbar.make(
//                binding.root,
//                R.string.message_in_app_update_ready,
//                Snackbar.LENGTH_INDEFINITE
//            )
//                .setAction(R.string.action_restart) { inAppUpdate.completeUpdate() }
//                .show()
//        }

        if (savedInstanceState == null) {
            mViewModel.handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mViewModel.handleIntent(intent)
    }
}