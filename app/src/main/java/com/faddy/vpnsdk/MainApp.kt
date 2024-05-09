package com.faddy.vpnsdk

import android.app.Application
import com.faddy.tunnelkitlib.TunnelKit
import com.faddy.tunnelkitlib.vpnCores.VpnSwitchFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application() {
    @Inject
    lateinit var vpnSwitchFactory: VpnSwitchFactory
    var vpnSdk: TunnelKit? = null

    override fun onCreate() {
        super.onCreate()
        vpnSdk = TunnelKit(vpnSwitchFactory, this).init()
    }

    override fun onTerminate() {
        super.onTerminate()
        vpnSdk = null
    }
}


