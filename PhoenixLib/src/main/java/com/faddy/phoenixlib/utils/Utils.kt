package com.faddy.phoenixlib.utils

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale

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

fun getIPAddress(useIPv4: Boolean): String {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
            for (addr in addrs) {
                if (!addr.isLoopbackAddress) {
                    val sAddr = addr.hostAddress
                    val isIPv4 = sAddr.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%')
                            return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                0, delim
                            ).uppercase(
                                Locale.getDefault()
                            )
                        }
                    }
                }
            }
        }
    } catch (ignored: Exception) {
    }
    return "8.8.8.8"
}