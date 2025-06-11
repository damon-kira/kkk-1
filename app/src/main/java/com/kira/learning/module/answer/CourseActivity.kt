package com.kira.learning.module.answer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kira.learning.databinding.ActivityCourseBinding
import com.kira.learning.module.answer.adapter.UnitAdapter
import com.kira.learning.module.answer.vm.CourseViewModel

class CourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseBinding
    private val viewModel by viewModels<CourseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.courseData.observe(this) { course ->
            course?.let { updateUI(it) }
        }
    }

    private fun updateUI(course: Course) {
        // 手动更新所有UI组件
        binding.titleText.text = course.title
        binding.progressBar.max = course.totalSkills
        binding.progressBar.progress = course.currentSkills
//        binding.progressBar.pointerIcon = PointerIcon()
        binding.progressText.text = 
            "Level up ${course.totalSkills} skills to next trophy\n${course.currentSkills}/9"

        // 配置RecyclerView
        binding.unitsRecycler.layoutManager = LinearLayoutManager(this)
        binding.unitsRecycler.adapter = UnitAdapter(course.units)
    }
}