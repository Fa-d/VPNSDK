package com.faddy.singboxsdk.bg

import android.net.Network
import android.os.Build
import com.faddy.singboxsdk.di.UsedServices
import io.nekohasekai.libbox.InterfaceUpdateListener
import java.net.NetworkInterface

object DefaultNetworkMonitor {

    var defaultNetwork: Network? = null
    private var listener: InterfaceUpdateListener? = null
    private lateinit var services: UsedServices
    fun init(passedServices: UsedServices) {
        services = passedServices
    }

    suspend fun start() {
        DefaultNetworkListener.start(this) {
            defaultNetwork = it
            checkDefaultInterfaceUpdate(it)
        }
        defaultNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            services.connectivity.activeNetwork
        } else {
            DefaultNetworkListener.get()
        }
    }

    suspend fun stop() {
        DefaultNetworkListener.stop(this)
    }

    fun setListener(listener: InterfaceUpdateListener?) {
        DefaultNetworkMonitor.listener = listener
        checkDefaultInterfaceUpdate(defaultNetwork)
    }

    private fun checkDefaultInterfaceUpdate(
        newNetwork: Network?
    ) {
        val listener = listener ?: return
        if (newNetwork != null) {
            val interfaceName =
                (services.connectivity.getLinkProperties(newNetwork) ?: return).interfaceName
            for (times in 0 until 10) {
                var interfaceIndex: Int
                try {
                    interfaceIndex = NetworkInterface.getByName(interfaceName).index
                } catch (e: Exception) {
                    Thread.sleep(100)
                    continue
                }
                listener.updateDefaultInterface(interfaceName, interfaceIndex)
            }
        } else {
            listener.updateDefaultInterface("", -1)
        }
    }


}