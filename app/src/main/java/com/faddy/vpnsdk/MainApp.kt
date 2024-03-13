package com.faddy.vpnsdk

import android.app.Application
import com.faddy.phoenixlib.PhoenixVPN
import dagger.hilt.android.HiltAndroidApp
import go.Seq

@HiltAndroidApp
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Seq.setContext(this)
        vpnSdk = PhoenixVPN.init(applicationContext)

    }

    companion object {
        var vpnSdk: PhoenixVPN? = null
    }
}


