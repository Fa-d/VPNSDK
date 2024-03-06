package com.faddy.motherlib.vpnCores

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.faddy.motherlib.interfaces.IStartStop
import com.faddy.motherlib.interfaces.IVpnLifecycleTyped
import com.faddy.motherlib.interfaces.IVpnSpeedIPTyped
import com.faddy.motherlib.model.VPNStatus
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile

class VpnSwitchFactory : IVpnLifecycleTyped, IStartStop, IVpnSpeedIPTyped {

    val openVpnCoreConcrete = OpenVpnCore()
    lateinit var wireGuardCoreConcrete: WireGuardCore
    override fun setVpnStateListeners(vpnType: VPNType): LiveData<VPNStatus> {
        Log.e("setVpnStateListeners", vpnType.toString())
        return when (vpnType) {
            VPNType.NONE -> liveData { "None" }
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.currentVpnState

            }

            VPNType.OPENCONNECT -> liveData { "OPENCONNECT" }
            VPNType.WIREGUARD -> liveData { "WIREGUARD" }
            VPNType.IPSECIKEV2 -> {
                liveData { "IPSECIKEV2" }
            }

            VPNType.SINGBOX -> {
                liveData { "SINGBOX" }
            }
        }
    }
    override fun onVPNStart(vpnType: VPNType) {
        when (vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNStart()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
        }
    }

    override fun onVPNResume(vpnType: VPNType, passedContext: Context) {
        when (vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNResume(passedContext)
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
        }
    }

    override fun onVPNDestroy(vpnType: VPNType) {

        when (vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNDestroy()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
        }
    }

    override fun onVPNPause(vpnType: VPNType) {
        when (vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNPause()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
        }
    }

    override fun startVpn(vpnProfile: VpnProfile, passedActivity: Activity) {
        when (vpnProfile.vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.startVpn(vpnProfile, passedActivity)
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
        }
    }

    override fun stopVpn(vpnType: VPNType, passedContext: Context) {
        when (vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.stopVpn(vpnType, passedContext)
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
        }
    }

    override fun getUploadSpeed(vpnType: VPNType): LiveData<Long> {
        when (vpnType) {
            VPNType.NONE -> {
                return liveData { 0L }
            }

            VPNType.OPENVPN -> {
                return openVpnCoreConcrete.currentUploadSpeed
            }

            VPNType.OPENCONNECT -> {
                return liveData { 0L }
            }

            VPNType.WIREGUARD -> {
                return liveData { 0L }
            }

            VPNType.IPSECIKEV2 -> {
                return liveData { 0L }
            }

            VPNType.SINGBOX -> {
                return liveData { 0L }
            }
        }
    }

    override fun getDownloadSpeed(vpnType: VPNType): LiveData<Long> {
        when (vpnType) {
            VPNType.NONE -> {
                return liveData { 0L }
            }

            VPNType.OPENVPN -> {
                return openVpnCoreConcrete.currentDownloadSpeed
            }

            VPNType.OPENCONNECT -> {
                return liveData { 0L }
            }

            VPNType.WIREGUARD -> {
                return liveData { 0L }
            }

            VPNType.IPSECIKEV2 -> {
                return liveData { 0L }
            }

            VPNType.SINGBOX -> {
                return liveData { 0L }
            }
        }
    }

    override fun getCurrentIp(vpnType: VPNType): LiveData<String> {

        when (vpnType) {

            VPNType.NONE -> {
                return liveData { 0L }
            }

            VPNType.OPENVPN -> {
                return liveData { "" }
            }

            VPNType.OPENCONNECT -> {
                return liveData { 0L }
            }

            VPNType.WIREGUARD -> {
                return liveData { 0L }
            }

            VPNType.IPSECIKEV2 -> {
                return liveData { 0L }
            }

            VPNType.SINGBOX -> {
                return liveData { 0L }
            }
        }
    }
}
