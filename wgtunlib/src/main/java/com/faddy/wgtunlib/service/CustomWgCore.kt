package com.faddy.wgtunlib.service

import android.app.Activity
import android.util.Log
import com.faddy.wgtunlib.data.TunnelConfig
import com.faddy.wgtunlib.data.VpnState
import com.phoenixLib.commoncore.interfaces.IStartStop
import com.phoenixLib.commoncore.interfaces.IVpnLifecycle
import com.phoenixLib.commoncore.interfaces.IVpnSpeedIP
import com.phoenixLib.commoncore.model.VPNStatus
import com.phoenixLib.commoncore.model.VpnProfile
import com.wireguard.android.backend.BackendException
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Statistics
import com.wireguard.android.backend.Tunnel.State
import com.wireguard.config.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomWgCore @Inject constructor(val backend: GoBackend) : IVpnServiceTunnel, IVpnSpeedIP,
    IVpnLifecycle, IStartStop {
    val _vpnState = MutableStateFlow(VpnState())
    override val vpnServiceState: StateFlow<VpnState> = _vpnState.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var statsJob: Job
    private var config: Config? = null
    var currentTxSpeed = MutableStateFlow(0L)
    var currentRxSpeed = MutableStateFlow(0L)
    var currentVpnState = MutableStateFlow(VPNStatus.DISCONNECTED)

    fun onVpnResume() {
        wgStateListener()
    }

    override fun startVpn(vpnProfile: VpnProfile, passedContext: Activity) {
        onVpnResume()
        val tunnelConfig = TunnelConfig(id = 1, name = "wgTunnel", wgQuick = vpnProfile.vpnConfig)
        CoroutineScope(Dispatchers.IO).launch {
            startTunnel(tunnelConfig)
        }
    }

    override fun stopVpn() {
        onVpnResume()
        CoroutineScope(Dispatchers.IO).launch {
            stopTunnel()
        }
    }

    override fun onVPNStart() {

    }

    override fun onVPNResume(activity: Activity) {
        wgStateListener()
    }

    override fun onVPNDestroy() {
    }

    override fun onVPNPause() {
    }

    override fun getUploadSpeed(): Flow<Long> {
        return currentTxSpeed
    }

    override fun getDownloadSpeed(): Flow<Long> {
        return currentRxSpeed
    }

    override fun getCurrentIp(): Flow<String> {
        return flow { }
    }

    private fun wgStateListener() {
        CoroutineScope(Dispatchers.Main).launch {
            vpnServiceState.collectLatest { theVal ->
                if (theVal.status == State.UP) {
                    currentTxSpeed.emit(_vpnState.value.curTx)
                    currentRxSpeed.emit(_vpnState.value.curRx)
                    currentVpnState.emit(VPNStatus.CONNECTED)
                } else if (theVal.status == State.DOWN) {
                    currentVpnState.emit(VPNStatus.DISCONNECTED)
                }
            }
        }
    }

    override suspend fun startTunnel(tunnelConfig: TunnelConfig): State {
        return try {
            stopTunnelOnConfigChange(tunnelConfig)
            emitTunnelName(tunnelConfig.name)
            config = TunnelConfig.configFromQuick(tunnelConfig.wgQuick)
            backend.setState(this, State.UP, config)
        } catch (e: BackendException) {
            Log.e("sadfgfh", e.reason.name)
            State.DOWN
        }
    }

    private fun emitTunnelState(state: State) {
        _vpnState.tryEmit(_vpnState.value.copy(status = state))
    }

    private fun emitBackendStatistics(statistics: Statistics) {
        _vpnState.tryEmit(_vpnState.value.copy(statistics = statistics))
        _vpnState.tryEmit(_vpnState.value.copy(curRx = config?.peers?.get(0)?.publicKey?.let {
            backend.getStatistics(this).peerRx(it)
        } ?: 0L))
        _vpnState.tryEmit(_vpnState.value.copy(curTx = config?.peers?.get(0)?.publicKey?.let {
            backend.getStatistics(this).peerRx(it)
        } ?: 0L))
    }

    private suspend fun emitTunnelName(name: String) {
        _vpnState.emit(_vpnState.value.copy(name = name))
    }

    private suspend fun stopTunnelOnConfigChange(tunnelConfig: TunnelConfig) {
        if (getState() == State.UP && _vpnState.value.name != tunnelConfig.name) {
            stopTunnel()
        }
    }

    override fun getName(): String {
        return _vpnState.value.name
    }

    override suspend fun stopTunnel() {
        val state = backend.setState(this, State.DOWN, null)
        emitTunnelState(state)
        try {
            _vpnState.value.status
            if (_vpnState.value.status == State.UP) {
                emitTunnelState(backend.setState(this, State.DOWN, null))
            }
        } catch (_: BackendException) {
        }
    }

    override fun getState(): State {
        return backend.getState(this)
    }

    override fun onStateChange(state: State) {
        val tunnel = this
        emitTunnelState(state)
        if (state == State.UP) {
            statsJob = scope.launch {
                while (true) {
                    emitBackendStatistics(backend.getStatistics(tunnel))
                    delay(1000L)
                }
            }
        }
        if (state == State.DOWN) {
            if (this::statsJob.isInitialized) {
                statsJob.cancel()
            }
        }
    }

}
