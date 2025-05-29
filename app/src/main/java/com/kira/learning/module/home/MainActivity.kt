package com.kira.learning.module.home

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.common.lib.base.BaseFragment
import com.common.lib.base.BaseFragmentActivity
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.LiveDataBusObserve
import com.common.lib.livedata.observerNonStickyForever
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.databinding.ActivityMainBinding
import com.kira.learning.module.account.MineFragment
import com.kira.learning.module.home.vm.HomeLoanViewModel
import com.kira.learning.module.dashboard.RepayTabFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseFragmentActivity() {

    private val mBinding: ActivityMainBinding by binding()

    private val mHomeViewModel by lazyViewModel<HomeLoanViewModel>()

    private val mHomeFragment by lazy(LazyThreadSafetyMode.NONE) {
        BaseFragment.getInstance(this, HomeFragment::class.java)
    }

    private val mRepayFragment by lazy(LazyThreadSafetyMode.NONE) {
        BaseFragment.getInstance(this, RepayTabFragment::class.java)
    }

    private val mMineFragment by lazy(LazyThreadSafetyMode.NONE) {
        BaseFragment.getInstance(this, MineFragment::class.java)
    }

    private var mMainEventObserve: LiveDataBusObserve<MainEvent>? = null

    override fun getFragmentViewId(): Int = R.id.fl_main_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRadioButton()
        mMainEventObserve = LiveDataBus.getLiveData(MainEvent::class.java).observerNonStickyForever {
            if (it.event == MainEvent.EVENT_SHOW_HOME) {
                mBinding.rbHomeLoan.isChecked = true
            } else if (it.event == MainEvent.EVENT_SHOW_REPAY) {
                mBinding.rbHomeRepay.isChecked = true
            }
        }

        // 设置导航菜单点击监听
        setupDrawer()
    }

    private fun setupDrawer() {
        mBinding.navView.setNavigationItemSelectedListener { menuItem ->
            // 处理菜单项点击事件
            when (menuItem.itemId) {
                R.id.nav_home -> showHomeFragment()
                R.id.nav_profile -> showProfileFragment()
                R.id.nav_settings -> openSettings()
            }
            // 关闭抽屉菜单
            mBinding.drawerLayout.closeDrawer(mBinding.navView)
            true  // 表示已处理点击事件
        }
    }

    fun drawerToggle(){
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    // 示例Fragment切换方法
    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main_container, FirstLoanFragment())
            .commit()
    }

    private fun showProfileFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main_container, NoProductFragment())
            .commit()
    }

    // 示例设置页面跳转
    private fun openSettings() {
//        startActivity(Intent(this, SettingsActivity::class.java))
    }

    // 处理返回键（关闭抽屉优先于退出）
//    override fun onBackPressed() {
//        if (mBinding.drawerLayout.isDrawerOpen(mBinding.navView)) {
//            mBinding.drawerLayout.closeDrawer(mBinding.navView)
//        } else {
//            super.onBackPressed()
//        }
//    }


    private fun initRadioButton() {
        mBinding.rbHomeLoan.buttonDrawable = StateListDrawable()
        mBinding.rbHomeRepay.buttonDrawable = StateListDrawable()
        mBinding.rbAccount.buttonDrawable = StateListDrawable()

        mBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_home_loan -> {
                    switchFragment(mHomeFragment)
                }
                R.id.rb_home_repay -> {
                    switchFragment(mRepayFragment)
                    try {
                        supportFragmentManager.executePendingTransactions()
                    } catch (e: Exception) {

                    }
                }
                R.id.rb_account -> {
                    switchFragment(mMineFragment)
                    try {
                        supportFragmentManager.executePendingTransactions()
                    } catch (e: Exception) {

                    }
                }
            }
        }
        mBinding.rbHomeLoan.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.removeObserve(MainEvent::class.java, mMainEventObserve)
    }
}