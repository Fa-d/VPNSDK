package com.faddy.phoenixlib.di

import android.content.Context
import com.faddy.phoenixlib.utils.SessionManagerInternal
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
    @Singleton
    fun providesSingBoxCore(@ApplicationContext context: Context) = SingBoxCore(context)

    @Provides
    @Singleton
    fun providesVPNSwitchFactory(
        @ApplicationContext context: Context,
        customWgCore: CustomWgCore,
        ovpnCore: OpenVpnCore,
        singBoxCore: SingBoxCore,
        internalSession: SessionManagerInternal
    ) = VpnSwitchFactory(context, customWgCore, ovpnCore, singBoxCore, internalSession)

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun providesCustomApplication(@ApplicationContext context: Context) = CustomApplication(context)

    /* @Provides
     @Singleton
     fun providesLastSelectedVpnType(@ApplicationContext appContext: Context): VPNType {
         val vpnState = SessionManagerInternal(
             appContext.getSharedPreferences("user_info_mother_lib", Context.MODE_PRIVATE)
         ).getLastConnVpnType() ?: ""
         val currentType = when (vpnState) {
             "OPENVPN" -> VPNType.OPENVPN
             "OPENCONNECT" -> VPNType.OPENCONNECT
             "WIREGUARD" -> VPNType.WIREGUARD
             "IPSECIKEV2" -> VPNType.IPSECIKEV2
             "SINGBOX" -> VPNType.SINGBOX
             else -> VPNType.NONE
         }
         Log.e("TAG", "providesLastSelectedVpnType: $currentType")
         return currentType
     }*/

}