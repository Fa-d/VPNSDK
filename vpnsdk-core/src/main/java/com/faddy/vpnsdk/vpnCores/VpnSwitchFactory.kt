package com.faddy.vpnsdk.vpnCores

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.faddy.vpnsdk.interfaces.IStartStop
import com.faddy.vpnsdk.interfaces.IVpnLifecycle
import com.faddy.vpnsdk.interfaces.IVpnSpeedIP
import com.faddy.vpnsdk.model.VPNStatus
import com.faddy.vpnsdk.model.VPNType
import com.faddy.vpnsdk.model.VpnProfile
import com.faddy.vpnsdk.utils.SessionManagerInternal
import javax.inject.Inject

class VpnSwitchFactory @Inject constructor(
    private val wireGuardCoreConcrete: CustomWgCore,
    private val openVpnCoreConcrete: OpenVpnCore,
    private val singBoxCoreConcrete: SingBoxCore,
    private val strongSwanCoreConcrete: StrongSwanCore,
    private val internalSession: SessionManagerInternal
) : IVpnLifecycle, IStartStop, IVpnSpeedIP {

    private fun providesLastSelectedVpnType() = when (internalSession.getLastConnVpnType()) {
        "OPENVPN" -> VPNType.OPENVPN
        "OPENCONNECT" -> VPNType.OPENCONNECT
        "WIREGUARD" -> VPNType.WIREGUARD
        "IPSECIKEV2" -> VPNType.IPSECIKEV2
        "SINGBOX" -> VPNType.SINGBOX
        else -> VPNType.NONE
    }

    fun setVpnStateListeners(): LiveData<VPNStatus> {
        return when (providesLastSelectedVpnType()) {
            VPNType.NONE -> liveData { }
            VPNType.OPENVPN -> openVpnCoreConcrete.currentVpnState
            VPNType.OPENCONNECT -> liveData { }
            VPNType.WIREGUARD -> wireGuardCoreConcrete.currentVpnState
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.currentVpnState
            VPNType.SINGBOX -> singBoxCoreConcrete.currentVpnState
        }
    }

    override fun onVPNStart() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> openVpnCoreConcrete.onVPNStart()
            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> wireGuardCoreConcrete.onVPNStart()
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.onVPNStart()
            VPNType.SINGBOX -> singBoxCoreConcrete.onVPNStart()
        }
    }

    override fun onVpnCreate() {
        openVpnCoreConcrete.onVpnCreate()
        wireGuardCoreConcrete.onVpnCreate()
        singBoxCoreConcrete.onVpnCreate()
        strongSwanCoreConcrete.onVpnCreate()
    }

    override fun onVPNResume() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> openVpnCoreConcrete.onVPNResume()
            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> wireGuardCoreConcrete.onVpnResume()
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.onVPNResume()
            VPNType.SINGBOX -> singBoxCoreConcrete.onVPNResume()
        }
    }

    override fun onVPNDestroy() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> openVpnCoreConcrete.onVPNDestroy()
            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> wireGuardCoreConcrete.onVPNDestroy()
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.onVPNDestroy()
            VPNType.SINGBOX -> singBoxCoreConcrete.onVPNDestroy()
        }
    }

    override fun onVPNPause() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> openVpnCoreConcrete.onVPNPause()
            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> wireGuardCoreConcrete.onVPNPause()
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.onVPNPause()
            VPNType.SINGBOX -> singBoxCoreConcrete.onVPNPause()
        }
    }

    override fun startVpn(vpnProfile: VpnProfile, passedActivity: Activity) {
        when (vpnProfile.vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> openVpnCoreConcrete.startVpn(vpnProfile, passedActivity)
            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> wireGuardCoreConcrete.startVpn(vpnProfile, passedActivity)
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.startVpn(vpnProfile, passedActivity)
            VPNType.SINGBOX -> singBoxCoreConcrete.startVpn(vpnProfile, passedActivity)
        }
    }

    override fun stopVpn() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> openVpnCoreConcrete.stopVpn()
            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> wireGuardCoreConcrete.stopVpn()
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.stopVpn()
            VPNType.SINGBOX -> singBoxCoreConcrete.stopVpn()
        }
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return when (providesLastSelectedVpnType()) {
            VPNType.NONE -> liveData { 0L }
            VPNType.OPENVPN -> openVpnCoreConcrete.currentUploadSpeed
            VPNType.OPENCONNECT -> liveData { 0L }
            VPNType.WIREGUARD -> wireGuardCoreConcrete.currentRxSpeed
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.getUploadSpeed()
            VPNType.SINGBOX -> singBoxCoreConcrete.getUploadSpeed()
        }
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return when (providesLastSelectedVpnType()) {
            VPNType.NONE -> liveData { 0L }
            VPNType.OPENVPN -> openVpnCoreConcrete.currentDownloadSpeed
            VPNType.OPENCONNECT -> liveData { 0L }
            VPNType.WIREGUARD -> wireGuardCoreConcrete.currentTxSpeed
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.getDownloadSpeed()
            VPNType.SINGBOX -> singBoxCoreConcrete.getDownloadSpeed()
        }
    }

    override fun getCurrentIp(): LiveData<String> {
        return when (providesLastSelectedVpnType()) {
            VPNType.NONE -> liveData { "" }
            VPNType.OPENVPN -> liveData { "" }
            VPNType.OPENCONNECT -> liveData { "" }
            VPNType.WIREGUARD -> liveData { "" }
            VPNType.IPSECIKEV2 -> strongSwanCoreConcrete.getCurrentIp()
            VPNType.SINGBOX -> liveData { "" }
        }
    }
}
