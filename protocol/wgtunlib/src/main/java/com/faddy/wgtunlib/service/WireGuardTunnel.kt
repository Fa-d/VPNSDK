package com.faddy.wgtunlib.service

import android.content.Context
import android.util.Log
import com.faddy.wgtunlib.data.TunnelConfig
import com.faddy.wgtunlib.data.VpnState
import com.wireguard.android.backend.BackendException
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Statistics
import com.wireguard.android.backend.Tunnel.State
import com.wireguard.config.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WireGuardTunnel @Inject constructor(val backend: GoBackend, val context: Context) :
    IVpnServiceTunnel {
    val _vpnState = MutableStateFlow(VpnState())
    override val vpnServiceState: StateFlow<VpnState> = _vpnState.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var statsJob: Job
    private var config: Config? = null

    override suspend fun startTunnel(tunnelConfig: TunnelConfig): State {
        return try {
            stopTunnelOnConfigChange(tunnelConfig)
            emitTunnelName(tunnelConfig.name)
            val includedList = mutableListOf<String>()
            val appList = (context.getSharedPreferences(
                "user_info_mother_lib", Context.MODE_PRIVATE
            ).getString("disAllowedAppList", "") ?: "").split(",")
            appList.forEach {
                val tempData = it
                if (!tempData.contains(",") && tempData.isNotEmpty()) {
                    includedList.add(tempData.replace(",", "").replace(" ", ""))
                }
            }
            // = appList.toMutableSet()
            config = TunnelConfig.configFromQuick(tunnelConfig.wgQuick, context)
            backend.setState(this, State.UP, config)
        } catch (e: BackendException) {
            Log.e("sadfgfh", e.reason.name.toString())
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
