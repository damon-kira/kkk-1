package com.kira.learning.module.codeeditor

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kira.learning.R

class CodePlaygroundActivity : AppCompatActivity() {

    private lateinit var codeExecutor: CodeExecutor
    private lateinit var editorFragment: CodeEditorFragment
    private lateinit var consoleFragment: ConsoleFragment
    private lateinit var visualFragment: VisualOutputFragment
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_playground)

        // 应用深色主题
//        setTheme(R.style.AppTheme_Dark)

        // 初始化执行引擎
        codeExecutor = CodeExecutor(this)

        // 设置编辑器
        editorFragment = CodeEditorFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.editor_container, editorFragment)
            .commit()

        // 设置输出区域
        consoleFragment = ConsoleFragment()
        visualFragment = VisualOutputFragment()

        // 设置标签页
        viewPager = findViewById(R.id.output_pager)
        tabLayout = findViewById(R.id.output_tabs)

        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(consoleFragment, "控制台")
        adapter.addFragment(visualFragment, "可视化输出")
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        // 添加输出监听
        codeExecutor.addOutputListener(object : CodeExecutor.OutputListener {
            override fun onOutput(output: String) {
                consoleFragment.addConsoleOutput(output)
            }

            override fun onVisualData(data: Float) {
                visualFragment.addDataPoint(data)
            }
        })

        // 设置按钮
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            editorFragment.resetCode()
            consoleFragment.clearConsole()
            visualFragment.clearVisualization()
            codeExecutor.stopExecution()
        }

        findViewById<Button>(R.id.run_button).setOnClickListener {
            runCode()
        }
    }

    private fun runCode() {
        consoleFragment.clearConsole()
        visualFragment.clearVisualization()
        codeExecutor.stopExecution()

        // 获取代码并执行
        val code = editorFragment.getCode()
        if (code.isNotEmpty()) {
            consoleFragment.addConsoleOutput("执行中...\n")
            codeExecutor.execute(code, "python")
        } else {
            consoleFragment.addConsoleOutput("没有要执行的代码\n")
        }
    }

    fun sendInput(input: String) {
        codeExecutor.sendInput(input)
    }

    override fun onDestroy() {
        super.onDestroy()
        codeExecutor.stopExecution()
    }
}

// 适配器类
class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    fun getPageTitle(position: Int): String {
        return titles[position]
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}