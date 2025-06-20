package com.kira.learning.camera

class UtilBox private constructor() {
    /**
     * UI工具类（使用延迟初始化）
     * 注意：Kotlin中非空属性必须立即初始化或声明为lateinit
     */
    val ui: UtilUI by lazy { UtilUI() }

    /**
     * 图像工具类（线程安全延迟初始化）
     */
    val bitmap: UtilBitmap by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        UtilBitmap() // 自动保证线程安全初始化
    }

    companion object {
        /**
         * 通过单例Holder模式实现线程安全的懒加载
         * Kotlin官方推荐的单例写法，等效于Java的静态内部类方式
         */
        val instance: UtilBox by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            UtilBox()
        }
    }

    /**
     * 初始化非急需工具类（不再需要显式线程管理）
     * Kotlin的lazy属性会在首次访问时自动初始化
     */
    private fun initBox() {
        // 通过访问bitmap属性触发初始化
        bitmap.toString() // 仅用于触发初始化，实际使用不需要这行
    }
}