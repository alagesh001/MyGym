package com.oxad.fitox.utils

import android.content.Context

object SharedPrefHelper {
    private const val PREF_NAME = "UserSession"

    fun save(context: Context, key: String, value: Any) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw IllegalArgumentException("Unsupported data type")
        }

        editor.apply()
    }

    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(key, defaultValue) ?: defaultValue
    }
    fun getFloat(context: Context, key: String, defaultValue: Float = 0f): Float {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getFloat(key, defaultValue)
    }
    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(key, defaultValue)
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(key, defaultValue)
    }
}
