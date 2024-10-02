package com.faddy.vpnsdk.ui

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faddy.vpnsdk.databinding.FragmentSplitTunnelBinding
import com.faddy.vpnsdk.session.SessionManager
import com.faddy.vpnsdk.ui.splitTunnel.AppAdapter
import com.faddy.vpnsdk.ui.splitTunnel.AppModel
import com.faddy.vpnsdk.ui.splitTunnel.DisAllowedAppAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplitTunnelFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentSplitTunnelBinding
    val disAllowedAppAdapter = DisAllowedAppAdapter()
    val allowedAppAdapter = AppAdapter()
    private var disAllowedAppsList: MutableList<AppModel> = mutableListOf()
    private var allowedAppsList: MutableList<AppModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = FragmentSplitTunnelBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            getApps()
            withContext(Dispatchers.Main) {
                initView()
            }
        }
        initClickListener()
        initObserver()
    }

    private fun initClickListener() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        disAllowedAppAdapter.setHasStableIds(true)
        with(binding.disallowedAppsRv) {
            setHasFixedSize(false)
            adapter = disAllowedAppAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        allowedAppAdapter.setHasStableIds(true)
        with(binding.allowedAppsRv) {
            setHasFixedSize(false)
            adapter = allowedAppAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        disAllowedAppAdapter.loadApps(disAllowedAppsList)
        allowedAppAdapter.loadApps(allowedAppsList)

        val tempDisAllowedAppList = sessionManager.getDisAllowedAppList().split(",")
        val tempAllowedAppList = sessionManager.getAllowedAppList()?.split(",") ?: listOf()

        for (i in allowedAppsList) {
            if (i.packageName !in tempAllowedAppList) {
                sessionManager.setAllowedAppList(sessionManager.getAllowedAppList() + ",${i.packageName}")
            }
        }

        for (i in disAllowedAppsList) {
            if (i.packageName !in tempDisAllowedAppList) {
                sessionManager.setDisAllowedAppList(sessionManager.getDisAllowedAppList() + ",${i.packageName}")
            }
        }

        disAllowedAppAdapter.addRemovedClicked = {
            if (it != null) {
                it.isAllowed = true
                allowedAppAdapter.addApp(it)
                sessionManager.setAllowedAppList(sessionManager.getAllowedAppList() + ",${it.packageName}")
                sessionManager.setDisAllowedAppList(
                    sessionManager.getDisAllowedAppList().replace(",${it.packageName}", "")
                )
            }
        }

        allowedAppAdapter.addRemovedClicked = {
            if (it != null) {
                it.isAllowed = false
                disAllowedAppAdapter.addApp(it)
                sessionManager.setAllowedAppList(
                    sessionManager.getAllowedAppList()!!.replace(",${it.packageName}", "")
                )
                sessionManager.setDisAllowedAppList(sessionManager.getDisAllowedAppList() + ",${it.packageName}")
            }
        }
        binding.disallowedAppsLabelTv.visibility = View.VISIBLE
        binding.allowedAppsLabelTv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun getApps() {
        val tempDisAllowedAppList = sessionManager?.getDisAllowedAppList()?.split(",") ?: listOf()
        val apps = requireActivity().packageManager.getInstalledPackages(0)

        for (i in apps.indices) {
            val p = apps[i]
            if (!isSystemPackage(p)) {
                val appName =
                    p.applicationInfo?.loadLabel(requireActivity().packageManager).toString()
                val icon = p.applicationInfo?.loadIcon(requireActivity().packageManager)
                val packageName = p.packageName
                if (icon != null) {
                    if (tempDisAllowedAppList.contains(packageName)) {
                        disAllowedAppsList.add(
                            AppModel(appName = appName, packageName = packageName, appIcon = icon)
                        )
                    } else {
                        allowedAppsList.add(
                            AppModel(appName = appName, packageName = packageName, appIcon = icon)
                        )
                    }
                }
            }
        }
    }

    private fun isSystemPackage(pi: PackageInfo): Boolean {
        return requireActivity().packageManager.getLaunchIntentForPackage(pi.packageName) == null
    }

    private fun initObserver() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val s = text.toString()
                val tempDisAllowedList = mutableListOf<AppModel>()
                val tempAllowedList = mutableListOf<AppModel>()
                for (i in disAllowedAppsList) {
                    if (i.appName.toLowerCase().contains(s.toLowerCase())) {
                        tempDisAllowedList.add(i)
                    }
                }

                for (i in allowedAppsList) {
                    if (i.appName.toLowerCase().contains(s.toLowerCase())) {
                        tempAllowedList.add(i)
                    }
                }

                disAllowedAppAdapter.filterList(tempDisAllowedList)
                allowedAppAdapter.filterList(tempAllowedList)

            }

            override fun afterTextChanged(text: Editable?) {}
        })
    }

}
