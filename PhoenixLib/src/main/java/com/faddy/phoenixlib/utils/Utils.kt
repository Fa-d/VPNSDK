package com.faddy.phoenixlib.utils

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

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

fun humanReadableByteCount(passedBytes: Long, speed: Boolean): String {
    var bytes = passedBytes
    if (speed) bytes *= 8
    val unit: Double = if (speed) 1000.0 else 1024.0
    val exp = max(0.0, min((ln(bytes.toDouble()) / ln(unit)).toInt().toDouble(), 3.0)).toInt()
    val bytesUnit: Float = (bytes / unit.pow(exp.toDouble())).toFloat()

    return if (speed) when (exp) {
        0 -> "$bytesUnit  bit/s"
        1 -> "$bytesUnit  kbit/s"
        2 -> "$bytesUnit  Mbit/s"
        else -> "$bytesUnit  Gbit/s"
    } else when (exp) {
        0 -> "$bytesUnit  B"
        1 -> "$bytesUnit  kB"
        2 -> "$bytesUnit  MB"
        else -> "$bytesUnit  GB"
    }
}
