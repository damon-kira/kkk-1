//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharedFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import java.util.concurrent.ConcurrentHashMap
//
///**
// * 现代化事件总线实现，基于 Kotlin Flow
// * 支持粘性和非粘性事件，自动管理生命周期
// */
//object FlowBus {
//
//    // 存储非粘性事件流 (SharedFlow)
//    private val events = ConcurrentHashMap<Class<*>, MutableSharedFlow<Any>>()
//
//    // 存储粘性事件流 (StateFlow)
//    private val stickyEvents = ConcurrentHashMap<Class<*>, MutableStateFlow<Any?>>()
//
//    /**
//     * 发送非粘性事件
//     * @param event 事件对象
//     */
//    suspend fun <T : Any> post(event: T) {
//        val eventClass = event.javaClass
//        val flow = events.getOrPut(eventClass) {
//            MutableSharedFlow(extraBufferCapacity = 64)
//        }
//        flow.emit(event)
//    }
//
//    /**
//     * 发送粘性事件
//     * @param event 事件对象
//     */
//    fun <T : Any> postSticky(event: T) {
//        val eventClass = event.javaClass
//        val flow = stickyEvents.getOrPut(eventClass) {
//            MutableStateFlow(null)
//        }
//        flow.value = event
//    }
//
//    /**
//     * 获取非粘性事件流
//     * @param eventClass 事件类
//     * @return 事件流
//     */
//    fun <T : Any> observe(eventClass: Class<T>): SharedFlow<T> {
//        return events.getOrPut(eventClass) {
//            MutableSharedFlow(extraBufferCapacity = 64)
//        }.asSharedFlow() as SharedFlow<T>
//    }
//
//    /**
//     * 获取粘性事件流
//     * @param eventClass 事件类
//     * @return 事件流
//     */
//    fun <T : Any> observeSticky(eventClass: Class<T>): StateFlow<T?> {
//        return stickyEvents.getOrPut(eventClass) {
//            MutableStateFlow(null)
//        }.asStateFlow() as StateFlow<T?>
//    }
//
//    /**
//     * 移除粘性事件
//     * @param eventClass 事件类
//     */
//    fun <T : Any> removeStickyEvent(eventClass: Class<T>) {
//        stickyEvents[eventClass]?.value = null
//    }
//
//    /**
//     * 安全观察事件（自动绑定生命周期）
//     * @param owner 生命周期拥有者
//     * @param eventClass 事件类
//     * @param minActiveState 最小生命周期状态（默认 RESUMED）
//     * @param action 事件处理
//     */
//    inline fun <reified T : Any> safeObserve(
//        owner: LifecycleOwner,
//        minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
//        crossinline action: (T) -> Unit
//    ) {
//        owner.lifecycleScope.launch {
//            owner.repeatOnLifecycle(minActiveState) {
//                observe(T::class.java).collect { event ->
//                    action(event)
//                }
//            }
//        }
//    }
//
//    /**
//     * 安全观察粘性事件（自动绑定生命周期）
//     * @param owner 生命周期拥有者
//     * @param eventClass 事件类
//     * @param minActiveState 最小生命周期状态（默认 RESUMED）
//     * @param action 事件处理
//     */
//    inline fun <reified T : Any> safeObserveSticky(
//        owner: LifecycleOwner,
//        minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
//        crossinline action: (T) -> Unit
//    ) {
//        owner.lifecycleScope.launch {
//            owner.repeatOnLifecycle(minActiveState) {
//                observeSticky(T::class.java).collect { event ->
//                    event?.let { action(it) }
//                }
//            }
//        }
//    }
//
//    /**
//     * 清除所有事件流
//     */
//    fun clear() {
//        events.clear()
//        stickyEvents.clear()
//    }
//}
//
//// 扩展函数：简化使用
//suspend inline fun <reified T : Any> FlowBus.post() = FlowBus.post(T::class.java)
//suspend inline fun <reified T : Any> FlowBus.post(event: T) = FlowBus.post(event)
//inline fun <reified T : Any> FlowBus.postSticky(event: T) = FlowBus.postSticky(event)
//inline fun <reified T : Any> FlowBus.observe(): SharedFlow<T> = FlowBus.observe(T::class.java)
//inline fun <reified T : Any> FlowBus.observeSticky(): StateFlow<T?> =
//    FlowBus.observeSticky(T::class.java)