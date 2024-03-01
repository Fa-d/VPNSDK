package com.faddy.singboxsdk.bg

import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import io.nekohasekai.libbox.InterfaceUpdateListener
import io.nekohasekai.libbox.NetworkInterfaceIterator
import io.nekohasekai.libbox.PlatformInterface
import io.nekohasekai.libbox.StringIterator
import io.nekohasekai.libbox.TunOptions
import io.nekohasekai.libbox.WIFIState
import java.net.Inet6Address
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.util.Enumeration
import io.nekohasekai.libbox.NetworkInterface as LibboxNetworkInterface

interface PlatformInterfaceWrapper : PlatformInterface {

    override fun usePlatformAutoDetectInterfaceControl(): Boolean {
        return true
    }

    override fun autoDetectInterfaceControl(fd: Int) {
    }

    override fun openTun(options: TunOptions): Int {
        error("invalid argument")
    }

    override fun useProcFS(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun findConnectionOwner(
        ipProtocol: Int,
        sourceAddress: String,
        sourcePort: Int,
        destinationAddress: String,
        destinationPort: Int
    ): Int {
        val uid = 0
        if (uid == Process.INVALID_UID) error("android: connection owner not found")
        return uid
    }

    override fun packageNameByUid(uid: Int): String {

        return ""
    }

    @Suppress("DEPRECATION")
    override fun uidByPackageName(packageName: String): Int {
        return 0
    }

    override fun usePlatformDefaultInterfaceMonitor(): Boolean {
        return true
    }

    override fun startDefaultInterfaceMonitor(listener: InterfaceUpdateListener) {
        DefaultNetworkMonitor.setListener(listener)
    }

    override fun closeDefaultInterfaceMonitor(listener: InterfaceUpdateListener) {
        DefaultNetworkMonitor.setListener(null)
    }

    override fun usePlatformInterfaceGetter(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    override fun getInterfaces(): NetworkInterfaceIterator {
        return InterfaceArray(NetworkInterface.getNetworkInterfaces())
    }

    override fun underNetworkExtension(): Boolean {
        return false
    }

    override fun clearDNSCache() {
    }

    override fun readWIFIState(): WIFIState? {
        return null
    }

    private class InterfaceArray(private val iterator: Enumeration<NetworkInterface>) :
        NetworkInterfaceIterator {

        override fun hasNext(): Boolean {
            return iterator.hasMoreElements()
        }

        override fun next(): LibboxNetworkInterface {
            val element = iterator.nextElement()
            return LibboxNetworkInterface().apply {
                name = element.name
                index = element.index
                runCatching {
                    mtu = element.mtu
                }
                addresses =
                    StringArray(element.interfaceAddresses.mapTo(mutableListOf()) { it.toPrefix() }
                        .iterator())
            }
        }

        private fun InterfaceAddress.toPrefix(): String {
            return if (address is Inet6Address) {
                "${Inet6Address.getByAddress(address.address).hostAddress}/${networkPrefixLength}"
            } else {
                "${address.hostAddress}/${networkPrefixLength}"
            }
        }
    }

    private class StringArray(private val iterator: Iterator<String>) : StringIterator {

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): String {
            return iterator.next()
        }
    }

}