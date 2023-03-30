package com.common.lib.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.common.lib.R
import com.common.lib.dialog.DefaultDialog
import com.common.lib.viewmodel.ViewModelFactory
import javax.inject.Inject

abstract class BaseDialogFragment : AppCompatDialogFragment() {

    companion object {

        inline fun <reified T : BaseDialogFragment> getInstance(
            context: Context,
            clazz: Class<T>,
            bundle: Bundle? = null
        ): T {
            return instantiate(context, clazz.name, bundle) as T
        }
    }

//    lateinit var viewModelFactory: ViewModelProvider.Factory

    inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> {
        return lazy { ViewModelProvider(this)[T::class.java] }
    }

    private var mDismissListener: DefaultDialog.OnDialogDismissListener? = null

//    @Inject
//    fun injectViewModelFactory(viewModelFactory: ViewModelFactory) {
//        if (this::viewModelFactory.isInitialized) return
//        this.viewModelFactory = viewModelFactory
//    }

    abstract fun getLayoutId(): Int

    fun setDismissListener(dismissListener: DefaultDialog.OnDialogDismissListener?) {
        this.mDismissListener = dismissListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setStyle(STYLE_NORMAL, theme)
        }
    }

    override fun getTheme(): Int {
        return R.style.App_Dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(getLayoutId(), container)
    }

    override fun onStart() {
        super.onStart()
        setWindowStyle()
    }

    open fun setWindowStyle() {
        dialog?.window?.apply {
            val size = getSize(windowManager)
            val params = attributes
            params.width = size.x
            params.height = size.y
            params.gravity = Gravity.CENTER
            attributes = params
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mDismissListener?.onDialogDismiss()
    }


    fun show(manager: FragmentManager) {
        show(manager, this::class.java.name)
    }


    open fun getSize(windowManager: WindowManager): Point {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        point.x = WindowManager.LayoutParams.WRAP_CONTENT
        point.y = WindowManager.LayoutParams.WRAP_CONTENT
        return point
    }

    fun getSupportContext(): Context = requireContext()
}