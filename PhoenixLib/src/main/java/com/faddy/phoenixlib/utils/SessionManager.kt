package com.faddy.phoenixlib.utils;

import android.content.SharedPreferences;

import javax.inject.Inject;

class SessionManager @Inject constructor(private val preferences: SharedPreferences) {

    fun getUserName() = preferences.getString("username", "")

    fun setUserName(value: String) {
        preferences.edit().putString("username", value).apply()
    }

    fun getPass() = preferences.getString("pass", "")

    fun setPass(value: String) = preferences.edit().putString("pass", value).apply()

    fun getLastConnVpnType() = preferences.getString("lastConnVpnType", "")

    fun setLastConnVpnType(value: String) =
        preferences.edit().putString("lastConnVpnType", value).apply()

    fun getLastConnStartTime() = preferences.getLong("lastConnStartTime", 0L)
    fun setLastConnStartTime(value: Long) {
        preferences.edit().putLong("lastConnStartTime", value).apply()
    }

    fun getLastConnServerIP() = preferences.getString("lastConnServerIP", "0.0.0.0")
    fun setLastConnServerIP(value: String) {
        preferences.edit().putString("lastConnServerIP", value).apply()
    }

}