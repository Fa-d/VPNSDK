package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.phoenixlib.utils.humanReadableByteCount
import com.faddy.wgtunlib.data.TunnelConfig
import com.faddy.wgtunlib.service.WireGuardTunnel
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomWgCore @Inject constructor(private val wgTun: WireGuardTunnel) : IVpnSpeedIP,
    IVpnLifecycle, IStartStop {

    val currentTxSpeed = MutableLiveData("")
    val currentRxSpeed =
                MutableLiveData("")/*  companion object {  val wgTun = WireGuardTunnel() }*/

    val currentVpnState = MutableLiveData(VPNStatus.DISCONNECTED)


    fun onVpnResume() {
        wgStateListener()
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        onVpnResume()
        val tunnelConfig = TunnelConfig(id = 1, name = "wgTunnel", wgQuick = vpnProfile.vpnConfig)
        CoroutineScope(Dispatchers.IO).launch {
            wgTun.startTunnel(tunnelConfig)
        }
    }

    override fun stopVpn() {
        onVpnResume()
        CoroutineScope(Dispatchers.IO).launch {
            wgTun.stopTunnel()
        }
    }

    override fun onVPNStart() {

    }

    override fun onVpnCreate() {

    }

    override fun onVPNResume() {
        wgStateListener()
    }

    override fun onVPNDestroy() {
    }

    override fun onVPNPause() {
    }

    override fun getUploadSpeed(): LiveData<String> {
        return currentTxSpeed
    }

    override fun getDownloadSpeed(): LiveData<String> {
        return currentRxSpeed
    }

    override fun getCurrentIp(): LiveData<String> {
        return liveData { }
    }

    fun wgStateListener() {
        CoroutineScope(Dispatchers.IO).launch {
            wgTun.vpnServiceState.collectLatest { theVal ->
                if (theVal.status == Tunnel.State.UP) {
                    currentTxSpeed.postValue(humanReadableByteCount(theVal.curTx, true))
                    currentRxSpeed.postValue(humanReadableByteCount(theVal.curRx, true))
                    currentVpnState.postValue(VPNStatus.CONNECTED)
                } else if (theVal.status == Tunnel.State.DOWN) {
                    currentVpnState.postValue(VPNStatus.DISCONNECTED)
                }
            }
        }
    }
}