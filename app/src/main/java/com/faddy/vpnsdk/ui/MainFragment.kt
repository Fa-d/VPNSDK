package com.faddy.vpnsdk.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.faddy.phoenixlib.PhoenixVPN
import com.faddy.phoenixlib.model.VPNStatus
import com.faddy.phoenixlib.model.VPNType
import com.faddy.phoenixlib.model.VpnProfile
import com.faddy.vpnsdk.R
import com.faddy.vpnsdk.VpnConfigs
import com.faddy.vpnsdk.databinding.FragmentMainBinding
import com.faddy.vpnsdk.session.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var coreSdk: PhoenixVPN

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return FragmentMainBinding.inflate(layoutInflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initClickListener()
    }

    private fun initObserver() {
        coreSdk.funInvoker = {
            coreSdk.myCurrentIp?.observe(viewLifecycleOwner) { text ->
                binding.ipText.text = text
            }

            coreSdk.currentUploadSpeed?.observe(viewLifecycleOwner) { text ->
                binding.uploadText.text = text.toString()
            }

            coreSdk.currentDownloadSpeed?.observe(viewLifecycleOwner) { text ->
                binding.downloadText.text = text.toString()
            }

            coreSdk.connectedVpnTime.observe(viewLifecycleOwner) { text ->
                binding.timerText.text = text.toString()
            }

            coreSdk.currentPing.observe(viewLifecycleOwner) { text ->
                binding.pingTv.text = "$text ms"
            }

            coreSdk.connectedStatus?.observe(viewLifecycleOwner) { status ->
                Log.e("getVpnConnectedStatus", status.name)
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

    private fun initClickListener() {
        binding.buttonWireguard.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    coreSdk.startConnect(
                        requireActivity(), VpnProfile(
                            vpnType = VPNType.WIREGUARD,
                            userName = "ss",
                            password = "123456",
                            vpnConfig = VpnConfigs.wgTunnelConfig,
                            serverIP = VpnConfigs.openVpnIP,
                            disAllowedAppsList = sessionManager.getDisAllowedAppList()

                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(requireActivity(), activityRes)
            }
        }
        binding.buttonSingbox.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    val data = android.util.Base64.decode(
                        VpnConfigs.singBoxConfigEnc, android.util.Base64.DEFAULT
                    )
                    val configText = String(data, Charsets.UTF_8)
                    coreSdk.startConnect(
                        requireActivity(), VpnProfile(
                            vpnType = VPNType.SINGBOX,
                            userName = "ss",
                            password = "123456",
                            vpnConfig = VpnConfigs.wireGuardJson,
                            // vpnConfig = configText,
                            serverIP = VpnConfigs.openVpnIP,
                            disAllowedAppsList = sessionManager.getDisAllowedAppList()

                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(requireActivity(), activityRes)
            }
        }
        binding.splitTunnel.setOnClickListener {
            findNavController().navigate(R.id.splitTunnelFragment)
        }
        binding.buttonOvpn.setOnClickListener {
            if (coreSdk.isVpnServicePrepared()) {
                if (coreSdk.isVpnConnected()) {
                    coreSdk.disconnect()
                } else {
                    val data = android.util.Base64.decode(
                        VpnConfigs.singBoxConfigEnc, android.util.Base64.DEFAULT
                    )
                    val configText = String(data, Charsets.UTF_8)
                    coreSdk.startConnect(
                        requireActivity(), VpnProfile(
                            vpnType = VPNType.OPENVPN,
                            userName = "test1",
                            password = "1111112",
                            // vpnConfig = VpnConfigs.wireGuardJson,
                            vpnConfig = VpnConfigs.openVpnConf,
                            serverIP = VpnConfigs.openVpnIP,
                            disAllowedAppsList = sessionManager.getDisAllowedAppList()
                        )
                    )
                }
            } else {
                coreSdk.prepareVPNService(requireActivity(), activityRes)
            }
        }
    }

    private val activityRes =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // startConn()
        }
}
