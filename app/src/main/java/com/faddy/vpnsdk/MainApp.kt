package com.faddy.vpnsdk

import android.app.Application
import com.faddy.phoenixlib.PhoenixVPN
import com.faddy.phoenixlib.vpnCores.VpnSwitchFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application() {
    @Inject
    lateinit var vpnSwitchFactory: VpnSwitchFactory
    var vpnSdk: PhoenixVPN? = null

    override fun onCreate() {
        super.onCreate()
        vpnSdk = PhoenixVPN(vpnSwitchFactory, this).init()
    }

    override fun onTerminate() {
        super.onTerminate()
        vpnSdk = null
    }
}


