package com.common.lib.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.common.lib.BuildConfig
import com.common.lib.dialog.DefaultDialog
import com.common.lib.dialog.hideLoadingDialog
import com.common.lib.dialog.showDialoga
import com.common.lib.dialog.showLoadingDialog
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i

/**
 * Created by weisl on 2019/9/24.
 * laotie#liuliuliu
 */
open class BaseFragment : InjectorFragment(), OnFragmentVisibilityChangedListener,
    View.OnAttachStateChangeListener, ILoading, IFragmentSwitch {

    protected val TAG = "debug_${this.javaClass.simpleName}"

    protected val isDebug = BuildConfig.DEBUG

    companion object {
        inline fun <reified T : BaseFragment> getInstance(context: Context, clazz: Class<T>, bundle: Bundle? = null): T {
            val cla = FragmentFactory.loadFragmentClass(context.classLoader, clazz.name)
            val fragment = cla.newInstance()
            bundle?.let {
                bundle.classLoader = cla.classLoader
                fragment.arguments = bundle
            }
            return fragment as T
        }
    }

    private var mLoadingDialog: DefaultDialog? = null

    private var isDestroyView: Boolean = false

    private var mListener: OnFragmentVisibilityChangedListener? = null

    private var mVisible: Boolean = false

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is BaseFragment) {
            setOnVisibilityChangedListener(childFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        checkVisibility(true)
    }

    override fun onStop() {
        super.onStop()
        if (mVisible) {
            checkVisibility(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isDestroyView = false
        view.addOnAttachStateChangeListener(this)

        viewLifecycleOwner
    }

    final override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!isDestroyView) {
            checkVisibility(!hidden)
        }
    }

    open fun onRefresh() {}

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
        isDestroyView = true
    }

    fun isDestroyView() = isDestroyView

    private fun checkVisibility(visible: Boolean) {
        val parentFragment = parentFragment
        val superVisible = if (parentFragment == null) this.isShow() else super.isVisible()
        val parentVisible =
            parentFragment?.isShow() ?: true
        val tempVisible = parentVisible && superVisible && !isDestroyView() && visible && !isHidden

        loge("visible = $mVisible   temp = $tempVisible   isHidden = $isHidden  ")
        if (tempVisible != mVisible) {
            mVisible = tempVisible
            onFragmentVisibilityChanged(tempVisible)
        }
    }

    private fun Fragment.isShow(): Boolean {
        return this.isAdded && !this.isHidden && this.view != null && this.view?.windowToken != null
    }

    final override fun onViewDetachedFromWindow(v: View?) {
        v?.removeOnAttachStateChangeListener(this)
        checkVisibility(false)
    }

    final override fun onViewAttachedToWindow(v: View?) {
        checkVisibility(true)
    }

    @CallSuper
    override fun onFragmentVisibilityChanged(visible: Boolean) {
        loge("onFragmentVisibilityChanged visible ==== $visible")
        mListener?.onCheckChildVisiblity(visible)
    }

    final override fun onCheckChildVisiblity(visible: Boolean) {
        checkVisibility(visible)
    }

    fun isFragmentVisible() = mVisible

    open fun onFragmentBackPressed(): Boolean {
        return false
    }

    fun getSupportContext(): Context = requireActivity()

    protected fun setViewModelLoading(viewModel: BaseViewModel) {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) showLoading() else hideLoading()
        }
    }


    fun isFinish(): Boolean {
        return (activity?.isFinishing == true || isDetached || isDestroyView)
    }

    fun finish() {
        activity?.finish()
    }

    override fun showLoading(cancelable: Boolean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = getBaseActivity()?.showLoadingDialog(cancelable)
            logger_i(TAG, "dialog = null")
        } else {
            mLoadingDialog?.setCancelable(cancelable)
            if (mLoadingDialog?.isShowing == false) {
                getBaseActivity()?.showDialoga(mLoadingDialog)
            } else {
                logger_i(TAG, "dialog is showing")
            }
            logger_i(TAG, "dialog != null")
        }
    }

    override fun hideLoading() {
        getBaseActivity()?.hideLoadingDialog(mLoadingDialog)
    }

    fun getBaseActivity(): BaseActivity? = activity as? BaseActivity

    fun <T> getCurrentActivity(): T? {
        try {
            val activity = activity
            if (activity != null && !activity.isFinishing) {
                return activity as? T
            }
        } catch (e: Exception) {
        }
        return null
    }

    fun hideSoftInput() {
        activity?.let {
            com.common.lib.expand.hideSoftInput(it)
        }
    }

    fun setOnVisibilityChangedListener(listener: OnFragmentVisibilityChangedListener?) {
        mListener = listener
    }

    override fun switchFragment(fragment: BaseFragment) {
        hideSoftInput()
        (activity as? IFragmentSwitch)?.switchFragment(fragment)
    }

    override fun replaceFragment(fragment: BaseFragment) {
        hideSoftInput()
        (activity as? IFragmentSwitch)?.replaceFragment(fragment)
    }

    override fun reloadRootFragment(fragment: BaseFragment) {
        hideSoftInput()
        (activity as? IFragmentSwitch)?.reloadRootFragment(fragment)
    }

    override fun showToFragment(fragment: BaseFragment) {
        hideSoftInput()
        (activity as? IFragmentSwitch)?.showToFragment(fragment)
    }

    override fun pop(): Boolean {
        hideSoftInput()
        return (activity as? IFragmentSwitch)?.pop() == true
    }


    protected inline fun loge(message: String) {
        logger_e(TAG, message)
    }


}

interface OnFragmentVisibilityChangedListener {
    fun onFragmentVisibilityChanged(visible: Boolean)

    fun onCheckChildVisiblity(visible: Boolean)
}