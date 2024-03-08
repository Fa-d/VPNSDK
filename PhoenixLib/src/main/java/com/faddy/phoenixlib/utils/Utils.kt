package com.faddy.phoenixlib.utils

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
    val mediatorLiveData = MediatorLiveData<T>()
    mediatorLiveData.addSource(this) {
        mediatorLiveData.value = it
    }
    return mediatorLiveData
}

fun ping(url: String): String {
    try {
        val command = "/system/bin/ping -c 1 $url"
        val process = Runtime.getRuntime().exec(command)
        val allText = process.inputStream.bufferedReader().use { it.readText() }
        if (!TextUtils.isEmpty(allText)) {
            val tempInfo = allText.substring(allText.indexOf("min/avg/max/mdev") + 19)
            val temps = tempInfo.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (temps.isNotEmpty() && temps[0].length < 10) {
                return temps[0].toFloat().toInt().toString() + " ms"
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "100 ms"
}