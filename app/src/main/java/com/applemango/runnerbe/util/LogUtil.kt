package com.applemango.runnerbe.util

import android.util.Log

object LogUtil {
    private fun getTag(): String {
        return Thread.currentThread().stackTrace
            .firstOrNull {
                it.className != LogUtil::class.java.name
                        && !it.className.contains("java.lang.Thread")
            }?.className?.substringAfterLast('.')
            ?: "UnknownClass"
    }

    fun errorLog(message: String) {
        Log.e(getTag(), message)
    }

    fun debugLog(message: String) {
        Log.d(getTag(), message)
    }
}