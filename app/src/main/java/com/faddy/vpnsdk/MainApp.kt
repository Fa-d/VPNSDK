package com.faddy.vpnsdk

import android.app.Application
import com.faddy.tunnelkitlib.TunnelKit
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application() {
    @Inject
    lateinit var vpnSdk: TunnelKit
}


