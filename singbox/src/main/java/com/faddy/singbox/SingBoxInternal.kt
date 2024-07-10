package com.faddy.singbox

import com.faddy.singbox.utils.CommandClient
import io.nekohasekai.libbox.StatusMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SingBoxInternal {
    val uploadDiff = MutableStateFlow(0L)
    val downloadDiff = MutableStateFlow(0L)
    val statusClient = CommandClient(
        CoroutineScope(Dispatchers.IO), CommandClient.ConnectionType.Status, StatusClient()
    )

    inner class StatusClient : CommandClient.Handler {

        override fun onConnected() {
            CoroutineScope(Dispatchers.Main).launch {

            }
        }

        override fun onDisconnected() {
            CoroutineScope(Dispatchers.Main).launch {

            }
        }

        override fun updateStatus(status: StatusMessage) {
            CoroutineScope(Dispatchers.Main).launch {
                status.goroutines.toString()
                val trafficAvailable = status.trafficAvailable
                if (trafficAvailable) {
                    status.connectionsIn.toString()
                    status.connectionsOut.toString()
                    uploadDiff.emit(status.uplink)
                    downloadDiff.emit(status.downlink)
                    status.uplinkTotal
                    status.downlinkTotal
                }
            }
        }
    }
}