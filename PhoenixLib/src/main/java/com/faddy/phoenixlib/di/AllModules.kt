package com.faddy.phoenixlib.di

import android.content.Context
import com.faddy.phoenixlib.vpnCores.CustomWgCore
import com.faddy.phoenixlib.vpnCores.OpenVpnCore
import com.faddy.phoenixlib.vpnCores.SingBoxCore
import com.faddy.phoenixlib.vpnCores.VpnSwitchFactory
import com.faddy.singbox.CustomApplication
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
    fun providesWGCore(wgTun: WireGuardTunnel) = CustomWgCore(wgTun)

    @Provides
    @Singleton
    fun providesOVpnCore(@ApplicationContext context: Context) = OpenVpnCore(context)


    @Provides
    fun providesSingBoxCore(@ApplicationContext context: Context) = SingBoxCore(context)

    @Provides
    @Singleton
    fun providesVPNSwitchFactory(
        customWgCore: CustomWgCore, ovpnCore: OpenVpnCore, singBoxCore: SingBoxCore
    ) = VpnSwitchFactory(customWgCore, ovpnCore, singBoxCore)

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun providesCustomApplication(@ApplicationContext context: Context) = CustomApplication(context)

}