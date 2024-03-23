package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycleTyped
import com.faddy.phoenixlib.interfaces.IVpnSpeedIPTyped
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile

class VpnSwitchFactory : IVpnLifecycleTyped, IStartStop, IVpnSpeedIPTyped {

    val openVpnCoreConcrete = OpenVpnCore()
    val wireGuardCoreConcrete = CustomWgCore()
    fun setVpnStateListeners(vpnType: VPNType): LiveData<VPNStatus> {
        return when (vpnType) {
            VPNType.NONE -> liveData {  }
            VPNType.OPENVPN -> openVpnCoreConcrete.currentVpnState
            VPNType.OPENCONNECT -> return liveData { }
            VPNType.WIREGUARD -> wireGuardCoreConcrete.currentVpnState
            VPNType.IPSECIKEV2 -> return liveData { }
            VPNType.SINGBOX -> return liveData { }
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
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.onVpnResume()
            }
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
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.startVpn(vpnProfile, passedActivity)
            }
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
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.stopVpn(vpnType, passedContext)
            }
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
                return wireGuardCoreConcrete.currentTxSpeed
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
                return wireGuardCoreConcrete.currentRxSpeed
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
