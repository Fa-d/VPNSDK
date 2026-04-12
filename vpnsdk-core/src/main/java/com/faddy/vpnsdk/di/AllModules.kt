package com.faddy.vpnsdk.di

import android.content.Context
import com.faddy.vpnsdk.utils.SessionManagerInternal
import com.faddy.vpnsdk.vpnCores.CustomWgCore
import com.faddy.vpnsdk.vpnCores.OpenVpnCore
import com.faddy.vpnsdk.vpnCores.SingBoxCore
import com.faddy.vpnsdk.vpnCores.StrongSwanCore
import com.faddy.vpnsdk.vpnCores.VpnSwitchFactory
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

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context
    ) = SessionManagerInternal(
        context.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
    )

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
    @Singleton
    fun providesSingBoxCore(@ApplicationContext context: Context) = SingBoxCore(context)

    @Provides
    @Singleton
    fun providesStrongSwanCore(@ApplicationContext context: Context) = StrongSwanCore(context)

    @Provides
    @Singleton
    fun providesVPNSwitchFactory(
        customWgCore: CustomWgCore,
        ovpnCore: OpenVpnCore,
        singBoxCore: SingBoxCore,
        strongSwanCore: StrongSwanCore,
        internalSession: SessionManagerInternal
    ) = VpnSwitchFactory(customWgCore, ovpnCore, singBoxCore, strongSwanCore, internalSession)

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context) = context

}
