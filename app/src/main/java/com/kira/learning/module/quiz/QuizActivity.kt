package com.kira.learning.module.quiz

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.common.lib.base.BaseActivity
import com.kira.learning.R
import com.kira.learning.module.quiz.vm.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class QuizActivity : BaseActivity() {
//    internal lateinit var viewModel: QuizViewModel
    internal val viewModel by lazyViewModel<QuizViewModel>()
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // 初始化ViewModel
//        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        // 设置ViewPager2
        viewPager = findViewById(R.id.viewpage2_quiz)
        val adapter = QuestionPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = true // 允许滑动切换

        // 观察进度变化
        viewModel.currentPosition.observe(this) { position ->
            updateProgress(position)
        }

        // 按钮点击事件
        findViewById<Button>(R.id.btnPrevious).setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem = viewPager.currentItem - 1
            }
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            if (viewPager.currentItem < viewModel.questions.size - 1) {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }

        // ViewPager页面切换监听
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setPosition(position)
            }
        })
    }

    // 更新进度条
    private fun updateProgress(position: Int) {
        val progress = (position + 1) * 100 / viewModel.questions.size
        findViewById<ProgressBar>(R.id.progressBar).progress = progress
    }

    // ViewPager适配器
    private inner class QuestionPagerAdapter(context: Context) : FragmentStateAdapter(context as FragmentActivity) {
        override fun getItemCount(): Int = viewModel.questions.size

        override fun createFragment(position: Int): Fragment {
            return QuizFragment.newInstance(position)
        }
    }
}