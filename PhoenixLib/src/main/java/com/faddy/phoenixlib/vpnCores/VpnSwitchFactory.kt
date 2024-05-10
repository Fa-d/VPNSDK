package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.phoenixlib.utils.SessionManagerInternal
import javax.inject.Inject

class VpnSwitchFactory @Inject constructor(
    private val wireGuardCoreConcrete: CustomWgCore,
    private val openVpnCoreConcrete: OpenVpnCore,
    private val singBoxCoreConcrete: SingBoxCore,
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
            VPNType.NONE -> liveData {  }
            VPNType.OPENVPN -> openVpnCoreConcrete.currentVpnState
            VPNType.OPENCONNECT -> return liveData { }
            VPNType.WIREGUARD -> wireGuardCoreConcrete.currentVpnState
            VPNType.IPSECIKEV2 -> return liveData { }
            VPNType.SINGBOX -> return singBoxCoreConcrete.currentVpnState
        }
    }

    override fun onVPNStart() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNStart()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.onVPNStart()
            }
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {
                singBoxCoreConcrete.onVPNStart()
            }
        }
    }

    override fun onVpnCreate() {
        openVpnCoreConcrete.onVpnCreate()
        wireGuardCoreConcrete.onVpnCreate()
        singBoxCoreConcrete.onVpnCreate()
    }

    override fun onVPNResume() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNResume()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.onVpnResume()
            }
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {
                singBoxCoreConcrete.onVPNResume()
            }
        }
    }

    override fun onVPNDestroy() {

        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNDestroy()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.onVPNDestroy()
            }
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {
                singBoxCoreConcrete.onVPNDestroy()
            }
        }
    }

    override fun onVPNPause() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNPause()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.onVPNPause()
            }
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {
                wireGuardCoreConcrete.onVPNPause()
            }
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
            VPNType.SINGBOX -> {
                singBoxCoreConcrete.startVpn(vpnProfile, passedActivity)
            }
        }
    }

    override fun stopVpn() {
        when (providesLastSelectedVpnType()) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.stopVpn()
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {
                wireGuardCoreConcrete.stopVpn()
            }
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {
                singBoxCoreConcrete.stopVpn()
            }
        }
    }

    override fun getUploadSpeed(): LiveData<Long> {
        when (providesLastSelectedVpnType()) {
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
                return wireGuardCoreConcrete.currentRxSpeed
            }

            VPNType.IPSECIKEV2 -> {
                return liveData { 0L }
            }

            VPNType.SINGBOX -> {
                return singBoxCoreConcrete.getUploadSpeed()
            }
        }
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        when (providesLastSelectedVpnType()) {
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
                return wireGuardCoreConcrete.currentTxSpeed
            }

            VPNType.IPSECIKEV2 -> {
                return liveData { 0L }
            }

            VPNType.SINGBOX -> {
                return singBoxCoreConcrete.getDownloadSpeed()
            }
        }
    }

    override fun getCurrentIp(): LiveData<String> {

        when (providesLastSelectedVpnType()) {

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
