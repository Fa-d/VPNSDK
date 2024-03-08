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

        /*      val usedServices = UsedServices(this)
                DefaultNetworkMonitor.init(usedServices)
                DefaultNetworkListener.init(usedServices)
                BoxService.Companion.init(usedServices)
                BoxService.init(usedServices)
                ProfileManager.init(usedServices)
                Settings.init(usedServices, Room.databaseBuilder(
                    this,
                    KeyValueDatabase::class.java,
                    Path.SETTINGS_DATABASE_PATH,
                ).allowMainThreadQueries().fallbackToDestructiveMigration()
                    .enableMultiInstanceInvalidation().setQueryExecutor { GlobalScope.launch { it.run() } }
                    .build().keyValuePairDao())
          */
    }

    companion object {
        var vpnSdk: PhoenixVPN? = null
    }
}


