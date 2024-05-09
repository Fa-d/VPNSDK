package com.faddy.singbox

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.PowerManager
import androidx.core.content.getSystemService
import com.faddy.singbox.bg.AppChangeReceiver
import go.Seq
import javax.inject.Inject

class CustomApplication @Inject constructor(private val passedApplication: Context) {

    init {
        application = passedApplication
        passedApplication?.registerReceiver(AppChangeReceiver(), IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addDataScheme("package")
        })
        Seq.setContext(passedApplication)
    }

    companion object {
        var application: Context? = null
        val notification by lazy { application?.getSystemService<NotificationManager>()!! }
        val connectivity by lazy { application?.getSystemService<ConnectivityManager>()!! }
        val packageManager by lazy { application?.packageManager }
        val powerManager by lazy { application?.getSystemService<PowerManager>()!! }
        val notificationManager by lazy { application?.getSystemService<NotificationManager>()!! }
        val wifiManager by lazy { application?.getSystemService<WifiManager>()!! }
    }

}