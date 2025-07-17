

package com.kira.ui.filesystem.base.model

@Retention(AnnotationRetention.SOURCE)
annotation class Permission {
    companion object {

        const val EMPTY = 0 // 000000000

        const val OWNER_READ = 1 // 000000001
        const val OWNER_WRITE = 1 shl 1 // 000000010
        const val OWNER_EXECUTE = 1 shl 2 // 000000100

        const val GROUP_READ = 1 shl 3 // 000001000
        const val GROUP_WRITE = 1 shl 4 // 000010000
        const val GROUP_EXECUTE = 1 shl 5 // 000100000

        const val OTHERS_READ = 1 shl 6 // 001000000
        const val OTHERS_WRITE = 1 shl 7 // 010000000
        const val OTHERS_EXECUTE = 1 shl 8 // 100000000
    }
}