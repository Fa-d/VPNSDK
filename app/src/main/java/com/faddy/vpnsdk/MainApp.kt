package com.faddy.vpnsdk

import android.app.Application
import com.faddy.tunnelkitlib.TunnelKit
import dagger.hilt.android.HiltAndroidApp
import go.Seq

@HiltAndroidApp
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Seq.setContext(this)
        vpnSdk = TunnelKit.init(applicationContext)

    }

    companion object {
        var vpnSdk: TunnelKit? = null
    }
}


