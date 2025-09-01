package com.kira.learning

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.annotation.WorkerThread
import com.util.lib.log.logger_d
import java.util.concurrent.ConcurrentHashMap
import java.util.EnumMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 统一延迟/分阶段初始化调度器：
 * - LIGHT: 首帧后尽快
 * - MEDIUM: 首帧后 + 200ms
 * - HEAVY: 首帧后 + 1s 或设备空闲
 */
object DeferredStartup {
    private val main = Handler(Looper.getMainLooper())
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    enum class Phase { LIGHT, MEDIUM, HEAVY }

    data class Task(
        val name: String,
        val phase: Phase,
        val block: () -> Unit
    )

    private val tasks = CopyOnWriteArrayList<Task>()

    @Volatile
    private var dispatched = false
    private val executed = ConcurrentHashMap<String, Long>() // taskName -> durationMs
    private val phaseCancelled = EnumMap<Phase, AtomicBoolean>(Phase::class.java).apply {
        Phase.values().forEach { put(it, AtomicBoolean(false)) }
    }
    @Volatile
    private var debugImmediate = false
    @Volatile
    private var enableLogging = false

    /** 调用以配置调度行为，应在 Application.onCreate 最早阶段 */
    fun configure(debugImmediate: Boolean, enableLogging: Boolean) {
        this.debugImmediate = debugImmediate
        this.enableLogging = enableLogging
    }

    /** 取消某个阶段（未执行任务不再执行） */
    fun cancelPhase(phase: Phase) {
        phaseCancelled[phase]?.set(true)
    }

    /** 低内存信号：直接跳过 HEAVY */
    fun signalLowMemory() {
        cancelPhase(Phase.HEAVY)
    }

    fun register(task: Task) {
        if (!dispatched) tasks += task
    }

    fun dispatchAll(application: Application) {
        if (dispatched) return
        dispatched = true
        if (debugImmediate) {
            // 立即顺序执行所有阶段
            Phase.values().forEach { runPhase(it) }
            return
        }
        // LIGHT 立即(首帧后由 LoanApplication 触发)
        runPhase(Phase.LIGHT)
        // MEDIUM
        main.postDelayed({ runPhase(Phase.MEDIUM) }, 200)
        // HEAVY
        main.postDelayed({ runPhase(Phase.HEAVY) }, 1000)
    }

    private fun runPhase(phase: Phase) {
        if (phaseCancelled[phase]?.get() == true) {
            if (enableLogging) logger_d("DeferredStartup", "Phase $phase cancelled, skip")
            return
        }
        val phaseTasks = tasks.filter { it.phase == phase }
        if (enableLogging) logger_d("DeferredStartup", "Run phase=$phase tasks=${phaseTasks.size}")
        phaseTasks.forEach { t -> scope.launch { safeRun(t) } }
    }

    @WorkerThread
    private fun safeRun(task: Task) {
        val start = System.nanoTime()
        if (enableLogging) logger_d(
            "DeferredStartup",
            "START task=${task.name} phase=${task.phase}"
        )
        try {
            task.block()
        } catch (e: Throwable) {
            if (enableLogging) logger_d("DeferredStartup", "ERROR task=${task.name} ${e.message}")
        } finally {
            val durMs = (System.nanoTime() - start) / 1_000_000
            executed[task.name] = durMs
            if (enableLogging) logger_d(
                "DeferredStartup",
                "END task=${task.name} phase=${task.phase} dur=${durMs}ms"
            )
        }
    }

    /** 获取已执行任务耗时快照 */
    fun metrics(): Map<String, Long> = executed.toMap()
}