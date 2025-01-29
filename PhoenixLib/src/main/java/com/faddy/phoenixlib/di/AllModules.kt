package com.faddy.phoenixlib.di

import android.content.Context
import com.faddy.phoenixlib.SdkInternal
import com.faddy.phoenixlib.utils.SessionManagerInternal
import com.faddy.phoenixlib.vpnCores.CustomWgCore
import com.faddy.phoenixlib.vpnCores.OpenVpnCore
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

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context
    ) = SessionManagerInternal(
        context.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
    )
    /*

        @Provides
        @Singleton
        fun providesSingBoxCore(@ApplicationContext context: Context) = SingBoxCore(context)
    */

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context) = context
    /*

            @Provides
            @Singleton
            fun providesCustomApplication(@ApplicationContext context: Context) = CustomApplication(context)
    */

    @Provides
    @Singleton
    fun providesVPNSwitchFactory(
        customWgCore: CustomWgCore,
        ovpnCore: OpenVpnCore,
        /*              singBoxCore: SingBoxCore,*/
        internalSession: SessionManagerInternal,
    ) = VpnSwitchFactory(customWgCore, ovpnCore, /*singBoxCore,*/ internalSession)

    @Provides
    @Singleton
    fun providesInternalValidator() = SdkInternal().systemSetup()

    @Provides
    @Singleton
    fun providesWGCore(wgTun: WireGuardTunnel) = CustomWgCore(wgTun)

    /*
        @Provides
        @Singleton
        fun providesOVpnCore(@ApplicationContext context: Context) = OpenVpnCore(context)
    */

    @Provides
    @Singleton
    fun providesGoBackend(@ApplicationContext context: Context) = GoBackend(context)

    @Provides
    @Singleton
    fun providesWireGuardTunnel(goBackend: GoBackend, @ApplicationContext context: Context) = WireGuardTunnel(goBackend, context)

}