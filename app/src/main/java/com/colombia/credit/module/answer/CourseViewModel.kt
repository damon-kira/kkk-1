package com.colombia.credit.module.answer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class CourseViewModel : ViewModel() {
    private val repository = CourseRepository(createMockService())
    private val _courseData = MutableLiveData<Course>()
    val courseData: LiveData<Course> = _courseData

    init {
        loadData()
    }

    private fun createMockService(): CourseService {
        return object : CourseService {
            override suspend fun getCourse(): Response<Course> {
                // 模拟网络延迟
                delay(500)
                return Response.success(
                    Course(
                        units = listOf(
                            CourseUnit(
                                title = "Unit 1: Introduction and First Methods",
                                skills = listOf(
                                    "Basics of differential equations",
                                    "Modeling systems with ODES",
                                    "Separation of variables method"
                                ),
                                quizEnabled = true
                            ),
                            CourseUnit(
                                title = "Unit 2: Graphical Approaches",
                                skills = emptyList(),
                                quizEnabled = false
                            )
                        )
                    )
                )
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _courseData.value = repository.loadCourse().body()
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }
}