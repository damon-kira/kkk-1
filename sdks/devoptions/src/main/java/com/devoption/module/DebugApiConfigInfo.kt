package com.devoption.module

internal object DebugApiConfigInfo {
    var isApiDebug: Boolean
        get() {
            return DebugSPUtils.getBoolean(DebugSPKey.KEY_API_MODE, true)
        }
        set(value) {
            DebugSPUtils.setBoolean(DebugSPKey.KEY_API_MODE, value)
        }
    var isH5Debug: Boolean
        get() {
            return DebugSPUtils.getBoolean(DebugSPKey.KEY_H5_MODE, true)
        }
        set(value) {
            DebugSPUtils.setBoolean(DebugSPKey.KEY_H5_MODE, value)
        }
    var isBigDataDebug: Boolean
        get() {
            return DebugSPUtils.getBoolean(DebugSPKey.KEY_BIGDATA_MODE, true)
        }
        set(value) {
            DebugSPUtils.setBoolean(DebugSPKey.KEY_BIGDATA_MODE, value)
        }
}