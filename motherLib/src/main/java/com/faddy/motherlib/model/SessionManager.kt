package com.faddy.motherlib.model;

import android.content.SharedPreferences;

import javax.inject.Inject;

class SessionManager @Inject constructor(private val preferences: SharedPreferences) {

    fun getUserName() = preferences.getString("username", "")

    fun setUserName(value: String) {
        preferences.edit().putString("username", value).apply()
    }

    fun getPass() = preferences.getString("pass", "")

    fun setPass(value: String) = preferences.edit().putString("pass", value).apply()

}