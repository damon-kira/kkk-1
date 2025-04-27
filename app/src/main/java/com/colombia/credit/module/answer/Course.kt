package com.colombia.credit.module.answer

import retrofit2.Response
import retrofit2.http.GET

// 实体类
data class Course(
    val title: String = "Differential Equations",
    val totalUnits: Int = 3,
    val totalSkills: Int = 100,
    val currentSkills: Int = 10,
    val units: List<CourseUnit>
)

data class CourseUnit(
    val title: String,
    val skills: List<String>,
    val quizEnabled: Boolean
)

// 模拟API接口
interface CourseService {
    @GET("course")
    suspend fun getCourse(): Response<Course>
}

// 仓库层
class CourseRepository(private val service: CourseService) {
    suspend fun loadCourse() = service.getCourse()
}