package com.faddy.phoenixlib.di

import android.content.Context
import com.faddy.phoenixlib.vpnCores.VpnSwitchFactory
import com.faddy.wgtunlib.service.WireGuardTunnel
import com.wireguard.android.backend.GoBackend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllModules {
    @Provides
    @Singleton
    fun providesGoBackend(@ApplicationContext context: Context) = GoBackend(context)

    @Provides
    @Singleton
    fun providesWireGuardTunnel(goBackend: GoBackend) = WireGuardTunnel(goBackend)

    @Provides
    @Singleton
    fun providesVPNSwitchFactory(wgTun: WireGuardTunnel) = VpnSwitchFactory(wgTun)

}