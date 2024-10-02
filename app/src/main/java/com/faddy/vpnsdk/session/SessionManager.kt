package com.faddy.vpnsdk.session;

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(private val preferences: SharedPreferences) {


    fun getDisAllowedAppList() = preferences.getString("disAllowedAppList", "") ?: ""
    fun setDisAllowedAppList(value: String) {
        preferences.edit().putString("disAllowedAppList", value).apply()
    }

    fun getAllowedAppList() = preferences.getString("allowedAppList", "")
    fun setAllowedAppList(value: String) {
        preferences.edit().putString("allowedAppList", value).apply()
    }
}