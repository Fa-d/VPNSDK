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

class CustomApplication {

    fun init(passedApplication: Context) {
        application = passedApplication
        application?.registerReceiver(AppChangeReceiver(), IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addDataScheme("package")
        })
        Seq.setContext(application)
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