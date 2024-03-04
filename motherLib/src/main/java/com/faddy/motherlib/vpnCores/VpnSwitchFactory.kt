package com.faddy.motherlib.vpnCores

import android.app.Activity
import android.content.Context
import com.faddy.motherlib.interfaces.IVpnLifecycleTyped
import com.faddy.motherlib.model.VPNType
import com.faddy.motherlib.model.VpnProfile

class VpnSwitchFactory : IVpnLifecycleTyped, IStartStop {

    lateinit var openVpnCoreConcrete: OpenVpnCore
    fun startVpn(vpnProfile: VpnProfile, context: Context, passedActivity: Activity) {
        when (vpnProfile.vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete = OpenVpnCore(context, passedActivity)
                openVpnCoreConcrete.startVpn(vpnProfile)
            }

            VPNType.OPENCONNECT -> {}
            VPNType.WIREGUARD -> {}
            VPNType.IPSECIKEV2 -> {}
            VPNType.SINGBOX -> {}
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

    override fun onVPNResume(vpnType: VPNType) {
        when (vpnType) {
            VPNType.NONE -> {}
            VPNType.OPENVPN -> {
                openVpnCoreConcrete.onVPNResume()
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

    override fun startVpn(vpnProfile: VpnProfile) {

    }

    override fun stopVpn() {
        openVpnCoreConcrete.stopVpn()
    }

}
