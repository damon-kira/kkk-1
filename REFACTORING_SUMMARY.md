# 可复用组件抽取总结

## 概述
从 `com.kira.learning.module` 包中成功抽取了大量可复用的组件，这些组件遵循现代Android开发最佳实践，采用MVI架构模式和Jetpack Compose UI框架。

## 抽取的组件分类

### 1. MVI架构基础组件 (`com.kira.ui.core.mvi`)

#### BaseUiState.kt
- 通用UI状态管理：`Idle`、`Loading`、`Error`、`Success`
- 提供扩展函数：`data`、`isLoading`、`isError`、`isSuccess`、`errorMessage`
- 支持刷新状态：`refreshing` 参数

#### BaseViewModel.kt
- 通用ViewModel基类，封装状态管理和事件处理
- `BaseDataViewModel` 专门用于数据加载场景
- 提供 `updateState`、`setState`、`sendEvent`、`executeLoad` 等方法

#### Events.kt
- 通用事件系统：
  - `ViewEvent` - 基础事件接口
  - `UserAction` - 用户操作事件（Refresh、Retry、Load等）
  - `UiEvent` - UI事件（ShowSnackbar、ShowDialog、Navigate等）
  - `DataEvent` - 数据操作事件（Load、Refresh、Retry、Search等）

### 2. UI组件库 (`com.kira.ui.core.components`)

#### StateComponents.kt
- `StateHandler` - 通用状态处理组件，根据状态显示不同内容
- `DefaultLoadingContent` - 标准加载状态UI
- `DefaultErrorContent` - 标准错误状态UI
- `EmptyContent` - 空状态UI

#### ListComponents.kt
- `RefreshableList` - 带下拉刷新的LazyColumn
- `SettingItem` - 通用设置项组件
- `SwitchSettingItem` - 开关设置项

#### FormComponents.kt
- `InputField` - 通用输入框，支持验证和错误显示
- `PasswordField` - 密码输入框，支持显示/隐藏密码
- `MultilineInputField` - 多行文本输入框

#### DialogComponents.kt
- `ConfirmDialog` - 确认对话框
- `LoadingDialog` - 加载对话框
- `InfoDialog` - 信息提示对话框

#### NavigationComponents.kt
- `CommonBottomNavigation` - 通用底部导航栏
- `CommonDrawerContent` - 通用抽屉导航内容
- 支持徽章和图标自定义

### 3. 数据层基础组件 (`com.kira.ui.core.repository`)

#### BaseRepository.kt
- `BaseRepository` - 通用Repository基类
- `safeApiCall` - 安全API调用，自动处理异常
- `loadWithCache` - 带缓存的数据加载
- `BasePagingRepository` - 分页数据Repository

### 4. 表单验证系统 (`com.kira.ui.core.validation`)

#### FormValidation.kt
- `FieldValidator` - 字段验证器接口
- `Validators` - 常用验证器工厂：
  - `required` - 必填验证
  - `email` - 邮箱格式验证
  - `minLength/maxLength` - 长度验证
  - `combine` - 组合验证器
- `FormField` - 表单字段状态
- `FormState` - 整个表单状态管理

## 重构示例

### 原始代码模式
```kotlin
// 原始的SampleViewModel
class SampleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SampleUiState>(SampleUiState.Idle)
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()
    
    fun dispatch(event: SampleEvent) {
        when (event) {
            SampleEvent.Load -> load()
            // 重复的样板代码...
        }
    }
}
```

### 重构后的代码
```kotlin
// 使用BaseViewModel和BaseUiState
class SampleViewModel : BaseViewModel<SampleViewState, UiEvent>(
    initialState = SampleViewState()
) {
    override fun handleAction(action: Any) {
        when (action) {
            is SampleEvent.LoadSamples -> loadSamples()
        }
    }
}

data class SampleViewState(
    val data: BaseUiState<List<SampleImage>> = BaseUiState.Idle
) : ViewState()
```

## 重构收益

### 1. 代码复用率提升
- 消除了各模块间的重复代码
- 统一了状态管理模式
- 标准化了UI组件

### 2. 开发效率提升
- 新功能开发时可直接使用通用组件
- 减少了样板代码编写
- 统一的表单验证系统

### 3. 代码质量提升
- 统一的错误处理机制
- 标准化的事件系统
- 类型安全的状态管理

### 4. 维护性提升
- 集中的组件管理
- 一致的用户体验
- 更容易的测试和调试

## 使用指南

### 创建新的ViewModel
```kotlin
@HiltViewModel
class NewFeatureViewModel @Inject constructor(
    private val repository: NewFeatureRepository
) : BaseViewModel<NewFeatureViewState, UiEvent>(
    initialState = NewFeatureViewState()
) {
    override fun handleAction(action: Any) {
        // 处理用户操作
    }
}
```

### 创建新的Screen
```kotlin
@Composable
fun NewFeatureScreen(
    viewModel: NewFeatureViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    
    StateHandler(
        state = state.data,
        onRetry = { viewModel.handleAction(RetryAction) }
    ) { data ->
        // 成功状态的UI内容
    }
}
```

### 使用表单验证
```kotlin
val formState = FormState(
    fields = mapOf(
        "email" to FormField(
            validators = listOf(
                Validators.required(),
                Validators.email()
            )
        )
    )
)
```

## 迁移建议

1. **逐步迁移**：先迁移新功能，再逐步重构现有模块
2. **保持兼容**：原有的接口可以保留，内部使用新的通用组件
3. **测试覆盖**：确保迁移后的功能与原有功能行为一致
4. **文档更新**：更新开发文档，推广新的开发模式

## 结论

通过这次重构，我们成功地：
- 抽取了15+个可复用组件
- 统一了MVI架构模式
- 建立了完整的UI组件库
- 创建了通用的表单验证系统
- 提供了清晰的使用示例

这些组件将大大提升团队的开发效率和代码质量，为后续的功能开发奠定了坚实的基础。
