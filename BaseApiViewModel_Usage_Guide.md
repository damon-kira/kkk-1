# BaseApiViewModel 使用指南

## 概述

`BaseApiViewModel` 是一个统一的 ViewModel 基类，为所有需要进行 API 调用的 ViewModel 提供标准化的错误处理和加载状态管理。它继承自 `BaseViewModel` 并扩展了 API 调用功能。

## 主要功能

### 1. 标准化 API 调用
- **executeApiCall**: 执行单次 API 调用并自动处理 Loading 和 Error 状态
- **executeApiFlow**: 执行 Flow API 调用并自动处理状态变化
- **executeBatchApiCalls**: 批量执行多个 API 调用
- **retryApiCall**: 带重试机制的 API 调用

### 2. 自动错误处理
- 统一的错误处理机制
- 支持自定义错误处理回调
- 自动显示错误消息（可选）

### 3. 加载状态管理
- 自动管理 Loading 状态
- 支持自定义加载消息
- 子类可自定义加载状态的 UI 表现

## 使用方法

### 1. 基本设置

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : BaseApiViewModel<MyViewState, MyEvent>(
    initialState = MyViewState()
) {

    override fun handleAction(action: MyEvent) {
        when (action) {
            is MyEvent.LoadData -> loadData()
            // 其他事件处理
        }
    }

    override fun updateLoadingState(isLoading: Boolean, message: String) {
        updateState { 
            copy(
                isLoading = isLoading,
                loadingMessage = if (isLoading) message else null
            )
        }
    }
}
```

### 2. 执行 API 调用

```kotlin
private fun loadData() {
    executeApiCall(
        apiCall = { repository.fetchData() },
        onSuccess = { data ->
            updateState { copy(data = data) }
        },
        onError = { error ->
            updateState { copy(errorMessage = error.message) }
        },
        showLoading = true,
        showErrorMessage = true
    )
}
```

### 3. 执行 Flow API 调用

```kotlin
private fun loadDataStream() {
    executeApiFlow(
        apiFlow = repository.fetchDataStream(),
        onSuccess = { data ->
            updateState { copy(data = data) }
        },
        onError = { error ->
            updateState { copy(errorMessage = error.message) }
        },
        onLoading = { message ->
            updateState { copy(loadingMessage = message) }
        }
    )
}
```

### 4. 批量 API 调用

```kotlin
private fun loadMultipleData() {
    executeBatchApiCalls(
        { repository.fetchUserProfile() },
        { repository.fetchSettings() },
        { repository.fetchNotifications() },
        onAllSuccess = {
            // 所有调用都成功
            updateState { copy(allDataLoaded = true) }
        },
        onAnyError = { errors ->
            // 有任何调用失败
            val errorMessage = errors.joinToString(", ") { it.message }
            updateState { copy(errorMessage = errorMessage) }
        }
    )
}
```

## 已迁移的 ViewModel

以下 ViewModel 已成功迁移到 `BaseApiViewModel`：

### 1. ProfileViewModel
- **功能**: 用户资料管理，设置保存
- **API 调用**: 加载资料、保存资料、更新头像、退出登录、保存设置
- **优势**: 简化了复杂的状态管理，统一了错误处理

### 2. AnswerViewModel  
- **功能**: 答题系统，问题获取和提交
- **API 调用**: 获取问题列表
- **优势**: 标准化了 MVI 模式，简化了状态更新

### 3. CodingViewModel
- **功能**: 代码编辑和执行
- **API 调用**: 代码执行请求
- **优势**: 统一了执行状态管理，简化了错误处理

### 4. MainViewModel
- **功能**: 主题和设置管理
- **API 调用**: 主要是本地设置，适合展示基础 MVI 模式
- **优势**: 标准化了事件处理模式

## 迁移前后对比

### 迁移前 (手动处理)
```kotlin
private fun loadProfile() {
    viewModelScope.launch {
        updateState { copy(profileData = BaseUiState.Loading) }

        when (val result = repo.load()) {
            is ApiResult.Success -> {
                updateState {
                    copy(profileData = BaseUiState.Success(result.data))
                }
            }
            is ApiResult.Error -> {
                updateState { copy(profileData = BaseUiState.Error(result.message)) }
                sendUiEvent(UiEvent.ShowSnackbar(result.message))
            }
            is ApiResult.NetworkUnavailable -> {
                val message = "网络不可用"
                updateState { copy(profileData = BaseUiState.Error(message)) }
                sendUiEvent(UiEvent.ShowSnackbar(message))
            }
            is ApiResult.Loading -> {
                // Loading状态已在开始时设置
            }
        }
    }
}
```

### 迁移后 (使用 BaseApiViewModel)
```kotlin
private fun loadProfile() {
    executeApiCall(
        apiCall = { repo.load() },
        onSuccess = { profile ->
            updateState {
                copy(profileData = BaseUiState.Success(profile))
            }
        },
        onError = { error ->
            updateState { copy(profileData = BaseUiState.Error(error.message)) }
        }
    )
}
```

## 最佳实践

1. **始终实现 updateLoadingState**: 每个继承 `BaseApiViewModel` 的类都必须实现此方法
2. **合理使用 showLoading 参数**: 对于不需要显示加载状态的操作，设置为 false
3. **自定义错误处理**: 对于需要特殊错误处理的场景，使用 onError 回调
4. **利用批量调用**: 对于需要同时执行多个 API 的场景，使用 executeBatchApiCalls

## 总结

通过将 ViewModel 迁移到 `BaseApiViewModel`，我们实现了：

- ✅ **代码简化**: 减少了重复的 API 调用处理代码
- ✅ **错误处理统一**: 所有 API 错误都通过统一的机制处理
- ✅ **加载状态管理**: 自动化的加载状态管理
- ✅ **类型安全**: 强类型的 API 调用处理
- ✅ **可测试性**: 更容易进行单元测试
- ✅ **一致性**: 项目中所有 API 调用都遵循相同的模式
