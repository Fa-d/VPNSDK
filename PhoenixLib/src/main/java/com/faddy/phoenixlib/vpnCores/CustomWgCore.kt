package com.faddy.phoenixlib.vpnCores

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faddy.phoenixlib.interfaces.IStartStop
import com.faddy.phoenixlib.interfaces.IVpnLifecycle
import com.faddy.phoenixlib.interfaces.IVpnSpeedIP
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.wgtunlib.data.TunnelConfig
import com.faddy.wgtunlib.service.WireGuardTunnel
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CustomWgCore : IVpnSpeedIP, IVpnLifecycle, IStartStop {

    val currentTxSpeed = MutableLiveData(0L)
    val currentRxSpeed = MutableLiveData(0L)
    companion object {
        val wgTun = WireGuardTunnel()
    }

    val currentVpnState = MutableLiveData(VPNStatus.DISCONNECTED)


    fun onVpnResume() {
        wgStateListener()
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        onVpnResume()
        CoroutineScope(Dispatchers.IO).launch {
            val tunnelConfig =
                TunnelConfig(id = 1, name = "wgTunnel", wgQuick = vpnProfile.vpnConfig)
            wgTun?.initBackend(context = passedContext)
            wgTun?.startTunnel(tunnelConfig)
        }
    }

    override fun stopVpn(vpnProfile: VPNType, passedContext: Context) {
        onVpnResume()
        CoroutineScope(Dispatchers.IO).launch {
            wgTun?.stopTunnel()
        }
    }

    override fun onVPNStart() {

    }

    override fun onVpnCreate(passedContext: Context, lifecycleObserver: LifecycleOwner) {

    }

    override fun onVPNResume(passedContext: Context) {
        wgStateListener()
    }

    override fun onVPNDestroy() {
    }

    override fun onVPNPause() {
    }

    override fun getUploadSpeed(): LiveData<Long> {
        return currentTxSpeed
    }

    override fun getDownloadSpeed(): LiveData<Long> {
        return currentRxSpeed
    }

    fun wgStateListener() {
        CoroutineScope(Dispatchers.IO).launch {
            wgTun?.vpnServiceState?.collectLatest { theVal ->
                if (theVal.status == Tunnel.State.UP) {
                    currentTxSpeed.postValue(wgTun._vpnState.value.curTx)
                    currentRxSpeed.postValue(wgTun._vpnState.value.curRx)
                    currentVpnState.postValue(VPNStatus.CONNECTED)
                } else if (theVal.status == Tunnel.State.DOWN) {
                    currentVpnState.postValue(VPNStatus.DISCONNECTED)
                }
            }
        }
    }
}