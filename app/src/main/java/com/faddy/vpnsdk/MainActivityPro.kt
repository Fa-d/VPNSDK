package com.faddy.vpnsdk

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.faddy.phoenixlib.modifiedCore.PhoenixVPNFinal
import com.faddy.vpnsdk.databinding.ActivityMainBinding
import com.phoenixLib.commoncore.model.VPNStatus
import com.phoenixLib.commoncore.model.VPNType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivityPro : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var coreSdk: PhoenixVPNFinal

    override fun onPause() {
        super.onPause()
        coreSdk.onVPNPause()
    }

    override fun onResume() {
        super.onResume()
        coreSdk.onVPNResume(this@MainActivityPro)
    }

    override fun onDestroy() {
        coreSdk.onVPNDestroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        initClickListener()
        initObserver()
    }

    private fun initObserver() {
        coreSdk.funInvoker = {
            CoroutineScope(Dispatchers.Main).launch {
                coreSdk.myCurrentIp.collect { text ->
                binding.ipText.text = text
            }

                coreSdk.currentUploadSpeed.collect { text ->
                binding.uploadText.text = text.toString()
            }

                coreSdk.currentDownloadSpeed.collect { text ->
                binding.downloadText.text = text.toString()
            }

                coreSdk.connectedVpnTime.collect { text ->
                binding.timerText.text = text.toString()
            }

                coreSdk.currentPing.collect { text ->
                binding.pingTv.text = "$text ms"
            }

                coreSdk.connectedStatus.collect { status ->
                    //    Log.e("getVpnConnectedStatus", status.name)
                when (status) {
                    VPNStatus.DISCONNECTING -> {
                        binding.connectionStatus.text = "DISCONNECTING"
                    }

                    VPNStatus.CONNECTED -> {
                        binding.connectionStatus.text = "CONNECTED"
                    }

                    VPNStatus.CONNECTING -> {
                        binding.connectionStatus.text = "CONNECTING"
                    }

                    VPNStatus.DISCONNECTED -> {
                        binding.connectionStatus.text = "DISCONNECTED"
                    }
                }
            }
        }
        }
    }

    private fun initClickListener() {
        binding.buttonWireguard.setOnClickListener {
            connDisCheck(VPNType.WIREGUARD)
        }
        binding.buttonSingbox.setOnClickListener {
            connDisCheck(VPNType.SINGBOX)
        }
        binding.buttonOvpn.setOnClickListener {
            connDisCheck(VPNType.OPENVPN)
        }
        binding.buttonValidity.setOnClickListener {}
    }

    private val activityRes =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            coreSdk.startConnect(this@MainActivityPro, VpnConfigs.getVPNProfile(VPNType.OPENVPN))
        }

    fun getDecryptedData(): String {
        val data = android.util.Base64.decode(
            VpnConfigs.singBoxConfigEnc, android.util.Base64.DEFAULT
        )
        return String(data, Charsets.UTF_8)
    }

    private fun connDisCheck(vpnType: VPNType) {
        if (coreSdk.isVpnServicePrepared()) {
            if (coreSdk.isVpnConnected()) {
                coreSdk.disconnect()
            } else {
                coreSdk.startConnect(this@MainActivityPro, VpnConfigs.getVPNProfile(vpnType))
            }
        } else {
            coreSdk.prepareVPNService(this@MainActivityPro, activityRes)
        }
    }
}