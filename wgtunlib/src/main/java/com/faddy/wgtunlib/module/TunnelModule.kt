package com.faddy.wgtunlib.module

import android.content.Context
import com.faddy.wgtunlib.data.repository.SettingsRepository
import com.faddy.wgtunlib.service.tunnel.VpnService
import com.faddy.wgtunlib.service.tunnel.WireGuardTunnel
import com.wireguard.android.backend.Backend
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.WgQuickBackend
import com.wireguard.android.util.RootShell
import com.wireguard.android.util.ToolsInstaller
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TunnelModule {
    @Provides
    @Singleton
    fun provideRootShell(@ApplicationContext context: Context): RootShell {
        return RootShell(context)
    }

    @Provides
    @Singleton
    @Userspace
    fun provideUserspaceBackend(@ApplicationContext context: Context): Backend {
        return GoBackend(context)
    }

    @Provides
    @Singleton
    @Kernel
    fun provideKernelBackend(@ApplicationContext context: Context, rootShell: RootShell): Backend {
        return WgQuickBackend(context, rootShell, ToolsInstaller(context, rootShell))
    }

    @Provides
    @Singleton
    fun provideVpnService(
        @Userspace userspaceBackend: Backend,
        @Kernel kernelBackend: Backend,
        settingsRepository: SettingsRepository,
        @ApplicationContext context: Context
    ): VpnService {
        return WireGuardTunnel(userspaceBackend, kernelBackend, settingsRepository, context)
    }
}
