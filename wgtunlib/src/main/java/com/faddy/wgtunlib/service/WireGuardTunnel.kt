package com.faddy.wgtunlib.service

import android.content.Context
import com.faddy.wgtunlib.data.TunnelConfig
import com.faddy.wgtunlib.data.VpnState
import com.faddy.wgtunlib.util.Constants
import com.wireguard.android.backend.Backend
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
import timber.log.Timber

class WireGuardTunnel : VpnService {
    val _vpnState = MutableStateFlow(VpnState())
    override val vpnServiceState: StateFlow<VpnState> = _vpnState.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var statsJob: Job
    private var config: Config? = null
    private var backend: Backend? = null

    fun initBackend(context: Context) {
        backend = GoBackend(context)
    }

    override suspend fun startTunnel(tunnelConfig: TunnelConfig): State {
        return try {
            stopTunnelOnConfigChange(tunnelConfig)
            emitTunnelName(tunnelConfig.name)
            config = TunnelConfig.configFromQuick(tunnelConfig.wgQuick)
            val state = backend?.setState(this, State.UP, config)
            emitTunnelState(state ?: State.DOWN)
            state ?: State.DOWN
        } catch (e: Exception) {
            Timber.e("Failed to start tunnel with error: ${e.message}")
            State.DOWN
        }
    }

    private fun emitTunnelState(state: State) {
        _vpnState.tryEmit(
            _vpnState.value.copy(status = state)
        )
    }

    private fun emitBackendStatistics(statistics: Statistics) {
        _vpnState.tryEmit(
            _vpnState.value.copy(statistics = statistics)
        )
        _vpnState.tryEmit(_vpnState.value.copy(curRx = config?.peers?.get(0)?.publicKey?.let {
            backend?.getStatistics(this)?.peer(
                it
            )?.rxBytes
        } ?: 0L))
        _vpnState.tryEmit(_vpnState.value.copy(curTx = config?.peers?.get(0)?.publicKey?.let {
            backend?.getStatistics(this)?.peer(
                it
            )?.txBytes
        } ?: 0L))
    }

    private suspend fun emitTunnelName(name: String) {
        _vpnState.emit(
            _vpnState.value.copy(name = name)
        )
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
        val state = backend?.setState(this, State.DOWN, null)
        emitTunnelState(state ?: State.DOWN)
        try {
            _vpnState.value.status
            if (_vpnState.value.status == State.UP) {
                val state = backend?.setState(this, State.DOWN, null)
                emitTunnelState(state ?: State.DOWN)
            }
        } catch (e: BackendException) {
            Timber.e("Failed to stop tunnel with error: ${e.message}")
        }
    }

    override fun getState(): State {
        return backend?.getState(this) ?: State.DOWN
    }

    override fun onStateChange(state: State) {
        val tunnel = this
        emitTunnelState(state)
        if (state == State.UP) {
            statsJob = scope.launch {
                while (true) {
                    backend?.getStatistics(tunnel)?.let { statistics ->
                        emitBackendStatistics(statistics)
                    }
                    delay(Constants.VPN_STATISTIC_CHECK_INTERVAL)
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
