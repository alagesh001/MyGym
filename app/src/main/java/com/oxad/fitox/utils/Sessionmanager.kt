package com.oxad.fitox.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_PIC = "userPic"


    }

    // Save login session
    fun saveLogin(userPic:String,username:String,email: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_NAME, username)
        editor.putString(KEY_USER_PIC, userPic)

        editor.apply()
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Get logged-in user's email
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, "")
    }
    fun getUserPic(): String? {
        return sharedPreferences.getString(KEY_USER_PIC, "")
    }
    fun setUserName(nameIs:String) {
        editor.putString(KEY_USER_NAME, nameIs)
        editor.apply()
    }
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, "")
    }
    // Logout user
    fun logout() {
        editor.clear()
        editor.apply()
    }
}