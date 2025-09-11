package com.kira.learning.modules.uidemo

import com.kira.learning.network.ApiResult
import com.kira.learning.network.ComposeApiService
import com.kira.learning.network.safeApiCall
import com.kira.learning.base.repository.BaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiDemoRepository @Inject constructor(
    private val apiService: ComposeApiService
) : BaseRepository() {

    /**
     * 获取UI组件演示数据
     */
    suspend fun getDemoData(): ApiResult<UiDemoData> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // val response = apiService.getUiDemoData()
            // response.toDomain()

            // 模拟UI演示数据
            UiDemoData(
                buttons = listOf(
                    ButtonDemo("Primary Button", "primary"),
                    ButtonDemo("Secondary Button", "secondary"),
                    ButtonDemo("Outlined Button", "outlined"),
                    ButtonDemo("Text Button", "text")
                ),
                cards = listOf(
                    CardDemo("基础卡片", "这是一个基础卡片的示例内容"),
                    CardDemo("图片卡片", "包含图片的卡片示例", "https://picsum.photos/300/200"),
                    CardDemo("操作卡片", "带有操作按钮的卡片示例")
                ),
                dialogs = listOf(
                    DialogDemo("确认对话框", "这是一个确认对话框的示例"),
                    DialogDemo("表单对话框", "包含表单输入的对话框示例"),
                    DialogDemo("列表对话框", "显示选项列表的对话框示例")
                ),
                animations = listOf(
                    AnimationDemo("淡入淡出", "fadeInOut"),
                    AnimationDemo("滑动动画", "slide"),
                    AnimationDemo("缩放动画", "scale"),
                    AnimationDemo("旋转动画", "rotation")
                )
            )
        }
    }

    /**
     * 提交UI反馈
     */
    suspend fun submitFeedback(feedback: UiFeedback): ApiResult<Unit> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // apiService.submitUiFeedback(feedback.toDto())

            // 模拟提交成功
        }
    }

    /**
     * 获取主题配置
     */
    suspend fun getThemeConfig(): ApiResult<ThemeConfig> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // val response = apiService.getThemeConfig()
            // response.toDomain()

            // 模拟主题配置
            ThemeConfig(
                primaryColor = "#2196F3",
                secondaryColor = "#03DAC5",
                backgroundColor = "#FFFFFF",
                surfaceColor = "#F5F5F5",
                errorColor = "#F44336",
                isDarkMode = false,
                fontFamily = "Roboto",
                borderRadius = 8
            )
        }
    }

    /**
     * 保存主题配置
     */
    suspend fun saveThemeConfig(config: ThemeConfig): ApiResult<Unit> {
        return safeApiCall {
            // TODO: 实现真实的API调用
            // apiService.saveThemeConfig(config.toDto())

            // 模拟保存成功
        }
    }
}

/**
 * UI演示数据
 */
data class UiDemoData(
    val buttons: List<ButtonDemo>,
    val cards: List<CardDemo>,
    val dialogs: List<DialogDemo>,
    val animations: List<AnimationDemo>
)

data class ButtonDemo(
    val title: String,
    val type: String,
    val isEnabled: Boolean = true
)

data class CardDemo(
    val title: String,
    val content: String,
    val imageUrl: String? = null
)

data class DialogDemo(
    val title: String,
    val description: String
)

data class AnimationDemo(
    val name: String,
    val type: String
)

/**
 * UI反馈
 */
data class UiFeedback(
    val componentType: String,
    val rating: Int, // 1-5星评分
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 主题配置
 */
data class ThemeConfig(
    val primaryColor: String,
    val secondaryColor: String,
    val backgroundColor: String,
    val surfaceColor: String,
    val errorColor: String,
    val isDarkMode: Boolean,
    val fontFamily: String,
    val borderRadius: Int
)
