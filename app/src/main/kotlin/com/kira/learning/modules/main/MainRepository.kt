package com.kira.learning.modules.main

import com.kira.learning.network.ApiResult
import com.kira.learning.network.ComposeApiService
import com.kira.learning.network.safeApiCall
import com.kira.learning.base.repository.BaseRepository
import com.kira.learning.models.dao.AIResponseInfo
import com.kira.learning.models.QuestionProcessInfo
import com.kira.learning.network.Document
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val apiService: ComposeApiService
) : BaseRepository() {

    /**
     * AI聊天请求
     */
    fun sendAIRequest(requestBody: RequestBody): Flow<ApiResult<AIResponseInfo>> {
        return baseResponseCall { apiService.aiSendRequest(requestBody) }
    }

    /**
     * 搜索问题
     */
    fun searchQuestion(requestBody: RequestBody): Flow<ApiResult<QuestionProcessInfo>> {
        return baseResponseCall { apiService.searchQuestion(requestBody) }
    }

    /**
     * 获取测验信息
     */
    fun getQuizInfo(requestBody: RequestBody): Flow<ApiResult<Document>> {
        return baseResponseCall { apiService.getQuizInfo(requestBody) }
    }

    /**
     * 获取主页推荐内容
     */
    suspend fun getRecommendedContent(): ApiResult<List<RecommendedItem>> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // val response = apiService.getRecommendedContent()
            // response.map { it.toDomain() }

            // 模拟推荐内容
            listOf(
                RecommendedItem(
                    id = "1",
                    title = "Kotlin协程入门",
                    description = "学习Kotlin协程的基本概念和使用方法",
                    type = RecommendedItemType.TUTORIAL,
                    imageUrl = "https://example.com/kotlin.jpg"
                ),
                RecommendedItem(
                    id = "2",
                    title = "Android Compose最佳实践",
                    description = "探索Jetpack Compose的高级技巧",
                    type = RecommendedItemType.ARTICLE,
                    imageUrl = "https://example.com/compose.jpg"
                ),
                RecommendedItem(
                    id = "3",
                    title = "算法练习挑战",
                    description = "今日算法练习题目",
                    type = RecommendedItemType.EXERCISE,
                    imageUrl = "https://example.com/algorithm.jpg"
                )
            )
        }
    }

    /**
     * 获取用户学习统计
     */
    suspend fun getUserLearningStats(): ApiResult<LearningStats> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // val response = apiService.getUserLearningStats()
            // response.toDomain()

            // 模拟学习统计
            LearningStats(
                totalStudyTime = 1234, // 分钟
                completedLessons = 45,
                streakDays = 7,
                currentLevel = "中级",
                pointsEarned = 2340
            )
        }
    }
}

/**
 * 推荐内容数据类
 */
data class RecommendedItem(
    val id: String,
    val title: String,
    val description: String,
    val type: RecommendedItemType,
    val imageUrl: String? = null
)

enum class RecommendedItemType {
    TUTORIAL, ARTICLE, EXERCISE, VIDEO
}

/**
 * 学习统计数据类
 */
data class LearningStats(
    val totalStudyTime: Int, // 总学习时间（分钟）
    val completedLessons: Int, // 完成的课程数
    val streakDays: Int, // 连续学习天数
    val currentLevel: String, // 当前等级
    val pointsEarned: Int // 获得的积分
)
