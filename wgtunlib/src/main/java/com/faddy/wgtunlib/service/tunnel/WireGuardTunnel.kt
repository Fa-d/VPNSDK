package com.faddy.wgtunlib.service.tunnel

import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.service.quicksettings.TileService
import com.faddy.wgtunlib.data.model.TunnelConfig
import com.faddy.wgtunlib.data.repository.SettingsRepository
import com.faddy.wgtunlib.module.Kernel
import com.faddy.wgtunlib.module.Userspace
import com.faddy.wgtunlib.service.tile.TunnelControlTile
import com.faddy.wgtunlib.util.Constants
import com.wireguard.android.backend.Backend
import com.wireguard.android.backend.BackendException
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
import javax.inject.Inject

class WireGuardTunnel @Inject constructor(
    @Userspace private val userspaceBackend: Backend,
    @Kernel private val kernelBackend: Backend,
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : VpnService {
    private val _vpnState = MutableStateFlow(VpnState())
    override val vpnState: StateFlow<VpnState> = _vpnState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var statsJob: Job

    private var config: Config? = null

    private var backend: Backend = userspaceBackend

    private var backendIsUserspace = true

    init {
        scope.launch {
            settingsRepository.getSettingsFlow().collect {
                backend = userspaceBackend
                backendIsUserspace = true
//                if (it.isKernelEnabled && backendIsUserspace) {
//                    Timber.d("Setting kernel backend")
//                    backend = kernelBackend
//                    backendIsUserspace = false
//                } else if (!it.isKernelEnabled && !backendIsUserspace) {
//                    Timber.d("Setting userspace backend")
//                    backend = userspaceBackend
//                    backendIsUserspace = true
//                }
            }
        }
    }

    override suspend fun startTunnel(tunnelConfig: TunnelConfig): State {
        return try {
            stopTunnelOnConfigChange(tunnelConfig)
            emitTunnelName(tunnelConfig.name)
            config = TunnelConfig.configFromQuick(tunnelConfig.wgQuick)
            val state = backend.setState(
                this,
                State.UP,
                config,
            )
            emitTunnelState(state)
            state
        } catch (e: Exception) {
            Timber.e("Failed to start tunnel with error: ${e.message}")
            State.DOWN
        }
    }

    private fun emitTunnelState(state: State) {
        _vpnState.tryEmit(
            _vpnState.value.copy(
                status = state,
            ),
        )
    }

    private fun emitBackendStatistics(statistics: Statistics) {
        _vpnState.tryEmit(
            _vpnState.value.copy(
                statistics = statistics,
            ),
        )
    }

    private suspend fun emitTunnelName(name: String) {
        _vpnState.emit(
            _vpnState.value.copy(
                name = name,
            ),
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
        try {
            if (getState() == State.UP) {
                val state = backend.setState(this, State.DOWN, null)
                emitTunnelState(state)
            }
        } catch (e: BackendException) {
            Timber.e("Failed to stop tunnel with error: ${e.message}")
        }
    }

    override fun getState(): State {
        return backend.getState(this)
    }

    override fun onStateChange(state: State) {
        val tunnel = this
        emitTunnelState(state)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TileService.requestListeningState(
                context,
                ComponentName(context, TunnelControlTile::class.java),
            )
        }
        if (state == State.UP) {
            statsJob = scope.launch {
                while (true) {
                    val statistics = backend.getStatistics(tunnel)
                    emitBackendStatistics(statistics)
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
