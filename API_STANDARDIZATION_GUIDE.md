# API标准化使用指南

## 概述

本项目已实现API返回格式统一和错误处理策略标准化，所有API调用都应遵循统一的标准和最佳实践。

## 统一的API返回格式

### ApiResult 结构
```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val code: Int? = null,
        val message: String,
        val errorType: ErrorType = ErrorType.UNKNOWN,
        val throwable: Throwable? = null
    ) : ApiResult<Nothing>()
    data class Loading(val message: String = "加载中...") : ApiResult<Nothing>()
    object NetworkUnavailable : ApiResult<Nothing>()
}
```

### 错误类型分类
- `NETWORK`: 网络连接错误
- `SERVER`: 服务器错误（5xx）
- `AUTHENTICATION`: 认证错误（401）
- `AUTHORIZATION`: 授权错误（403）
- `VALIDATION`: 验证错误（4xx）
- `TIMEOUT`: 超时错误
- `UNKNOWN`: 未知错误

## Repository层标准化

### 1. 继承BaseRepository
```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {
    
    // 推荐：使用executeApiFlow进行响应式调用
    fun getUserProfile(): Flow<ApiResult<UserProfile>> {
        return executeApiFlow {
            apiService.getUserProfile()
        }
    }
    
    // 推荐：使用executeBaseResponseCall处理标准响应
    suspend fun updateProfile(profile: UserProfile): ApiResult<UserProfile> {
        return executeBaseResponseCall {
            apiService.updateProfile(profile)
        }
    }
    
    // 推荐：使用executeApiWithRetry处理重要请求
    fun uploadAvatar(file: MultipartBody.Part): Flow<ApiResult<String>> {
        return executeApiWithRetry(maxRetries = 3) {
            apiService.uploadAvatar(file)
        }
    }
}
```

### 2. 缓存策略
```kotlin
class ProductRepository : BaseCachedRepository() {
    
    fun getProducts(forceRefresh: Boolean = false): Flow<ApiResult<List<Product>>> {
        return loadWithCache(
            loadFromCache = { cacheDao.getProducts() },
            loadFromNetwork = { apiService.getProducts().data!! },
            saveToCache = { cacheDao.saveProducts(it) },
            forceRefresh = forceRefresh
        )
    }
}
```

## ViewModel层标准化

### 1. 继承BaseApiViewModel
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseApiViewModel<UserViewState, UiEvent>(
    initialState = UserViewState()
) {
    
    // 自动处理Loading和Error状态
    fun loadUserProfile() {
        executeApiFlow(
            apiFlow = userRepository.getUserProfile(),
            onSuccess = { profile ->
                updateState { copy(userProfile = profile, isLoading = false) }
            },
            onLoading = { message ->
                updateState { copy(isLoading = true, loadingMessage = message) }
            }
        )
    }
    
    // 自定义错误处理
    fun updateProfile(profile: UserProfile) {
        executeApiCall(
            apiCall = { userRepository.updateProfile(profile) },
            onSuccess = { updatedProfile ->
                updateState { copy(userProfile = updatedProfile) }
                sendEvent(UiEvent.ShowSnackbar("更新成功"))
            },
            onError = { error ->
                when (error.errorType) {
                    ErrorType.VALIDATION -> {
                        updateState { copy(validationErrors = parseValidationErrors(error)) }
                    }
                    else -> {
                        // 使用默认错误处理
                        return@executeApiCall
                    }
                }
            }
        )
    }
    
    override fun updateLoadingState(isLoading: Boolean, message: String) {
        updateState { copy(isLoading = isLoading, loadingMessage = message) }
    }
}
```

## UI层标准化

### 1. 统一的错误处理
```kotlin
@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // 统一事件处理
    LaunchedEffect(viewModel) {
        viewModel.viewEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.ShowToast -> {
                    // 显示Toast
                }
                is UiEvent.Navigate -> {
                    // 导航处理
                }
                // ... 其他事件
            }
        }
    }
    
    // UI内容
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // ... UI内容
    }
}
```

## 最佳实践

### 1. Repository方法命名规范
- `get*()`: 获取单个数据，返回 `Flow<ApiResult<T>>`
- `load*()`: 加载数据（可能包含缓存），返回 `Flow<ApiResult<T>>`
- `update*()`: 更新数据，返回 `suspend ApiResult<T>`
- `delete*()`: 删除数据，返回 `suspend ApiResult<Unit>`
- `upload*()`: 上传文件，返回 `Flow<ApiResult<T>>`

### 2. 错误处理优先级
1. **自定义错误处理**: 在ViewModel中针对特定业务逻辑的错误处理
2. **BaseApiViewModel默认处理**: 通用的错误类型处理
3. **GlobalErrorHandler**: 全局兜底错误处理

### 3. Loading状态管理
```kotlin
// 推荐：使用executeApiFlow自动管理Loading状态
executeApiFlow(
    apiFlow = repository.getData(),
    onSuccess = { data -> /* 处理成功 */ },
    onLoading = { message -> /* 自定义Loading显示 */ }
)

// 不推荐：手动管理Loading状态
viewModelScope.launch {
    updateState { copy(isLoading = true) }
    val result = repository.getData()
    updateState { copy(isLoading = false) }
    // ...
}
```

### 4. 网络请求重试策略
```kotlin
// 重要请求使用重试机制
fun uploadImportantData(): Flow<ApiResult<String>> {
    return executeApiWithRetry(maxRetries = 3) {
        apiService.uploadData()
    }
}

// 普通请求不使用重试
fun getNormalData(): Flow<ApiResult<String>> {
    return executeApiFlow {
        apiService.getData()
    }
}
```

## 迁移指南

### 现有代码迁移步骤

1. **Repository层迁移**
   ```kotlin
   // 旧代码
   suspend fun login(): ApiResult<LoginData> {
       return safeApiCallWithMapping {
           apiService.login(request)
       }
   }
   
   // 新代码
   suspend fun login(): ApiResult<LoginData> {
       return executeBaseResponseCall {
           apiService.login(request)
       }
   }
   ```

2. **ViewModel层迁移**
   ```kotlin
   // 旧代码
   class AuthViewModel : BaseViewModel<State, Event>()
   
   // 新代码
   class AuthViewModel : BaseApiViewModel<State, Event>()
   ```

3. **错误处理迁移**
   ```kotlin
   // 旧代码
   when (result) {
       is ApiResult.Success -> { /* 处理成功 */ }
       is ApiResult.Error -> { /* 手动错误处理 */ }
   }
   
   // 新代码
   executeApiCall(
       apiCall = { repository.someCall() },
       onSuccess = { /* 处理成功 */ },
       // 错误处理自动化
   )
   ```

## 注意事项

1. **保持一致性**: 所有新的API调用都应使用统一的格式
2. **错误处理**: 优先使用框架提供的错误处理，特殊情况才自定义
3. **Loading状态**: 使用统一的Loading管理，避免重复代码
4. **测试**: 使用统一的Mock和测试工具
5. **文档**: 及时更新API文档和错误码说明
