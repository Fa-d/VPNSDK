package com.faddy.vpnsdk

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

object VpnSdk {

    @Volatile
    private var appContext: Context? = null

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface VpnSdkEntryPoint {
        fun tunnelKit(): TunnelKit
    }

    fun init(context: Context, licenseToken: String) {
        appContext = context.applicationContext
        // License verification will be added in Phase 3.
    }

    fun tunnelKit(): TunnelKit {
        val ctx = appContext
            ?: throw IllegalStateException("VpnSdk.init() must be called before tunnelKit()")
        return EntryPoints.get(ctx, VpnSdkEntryPoint::class.java).tunnelKit()
    }
}
