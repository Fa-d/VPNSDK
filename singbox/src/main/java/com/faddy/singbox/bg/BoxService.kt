package com.faddy.singbox.bg

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.faddy.singbox.CustomApplication
import com.faddy.singbox.R
import com.faddy.singbox.constant.Action
import com.faddy.singbox.constant.Alert
import com.faddy.singbox.constant.Status
import com.faddy.singbox.ktx.hasPermission
import go.Seq
import io.nekohasekai.libbox.BoxService
import io.nekohasekai.libbox.CommandServer
import io.nekohasekai.libbox.CommandServerHandler
import io.nekohasekai.libbox.Libbox
import io.nekohasekai.libbox.PlatformInterface
import io.nekohasekai.libbox.SystemProxyStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

class BoxService(
    private val service: Service, private val platformInterface: PlatformInterface
) : CommandServerHandler {

    companion object {

        private var initializeOnce = false
        private fun initialize() {
            if (initializeOnce) return
            val baseDir = CustomApplication.application!!.filesDir
            baseDir.mkdirs()
            val workingDir = CustomApplication.application!!.getExternalFilesDir(null) ?: return
            workingDir.mkdirs()
            val tempDir = CustomApplication.application!!.cacheDir
            tempDir.mkdirs()
            Libbox.setup(baseDir.path, workingDir.path, tempDir.path, false)
            Libbox.redirectStderr(File(workingDir, "stderr.log").path)
            initializeOnce = true
            return
        }

        fun start() {
            val intent = runBlocking {
                withContext(Dispatchers.IO) {
                    Intent(CustomApplication.application, VPNService::class.java)
                }
            }
            ContextCompat.startForegroundService(CustomApplication.application!!, intent)
        }

        fun stop() {
            CustomApplication.application!!.sendBroadcast(
                Intent(Action.SERVICE_CLOSE).setPackage(
                    CustomApplication.application!!.packageName
                )
            )
        }

        fun reload() {
            CustomApplication.application?.sendBroadcast(
                Intent(Action.SERVICE_RELOAD).setPackage(
                    CustomApplication.application!!.packageName
                )
            )
        }
    }

    var fileDescriptor: ParcelFileDescriptor? = null

    private val status = MutableLiveData(Status.Stopped)
    private val binder = ServiceBinder(status)
    private val notification = ServiceNotification(status, service)
    private var boxService: BoxService? = null
    private var commandServer: CommandServer? = null
    private var receiverRegistered = false
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Action.SERVICE_CLOSE -> {
                    stopService()
                }

                Action.SERVICE_RELOAD -> {
                    serviceReload()
                }

                PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        serviceUpdateIdleMode()
                    }
                }
            }
        }
    }

    private fun startCommandServer() {
        val commandServer = CommandServer(this, 300)
        commandServer.start()
        this.commandServer = commandServer
    }

    private var lastProfileName = ""
    private suspend fun startService(delayStart: Boolean = false, passedConf: String) {
        try {
            withContext(Dispatchers.Main) {
                notification.show(lastProfileName, R.string.status_starting)
            }

            /*  val selectedProfileId = Settings.selectedProfile
              if (selectedProfileId == -1L) {
                  stopAndAlert(Alert.EmptyConfiguration)
                  return
              }

              val profile = ProfileManager.get(selectedProfileId)
              if (profile == null) {
                  stopAndAlert(Alert.EmptyConfiguration)
                  return
              }*/

            /*    val content = File(profile.typed.path).readText()
                if (content.isBlank()) {
                    stopAndAlert(Alert.EmptyConfiguration)
                    return
                }*/
            if (passedConf.isBlank()) {
                stopAndAlert(Alert.EmptyConfiguration)
                return
            }
            try {
                Libbox.checkConfig(passedConf)
            } catch (ex: Exception) {
                Log.e("checkConfig", ex.message.toString())
            }
            lastProfileName = "Start"
            withContext(Dispatchers.Main) {
                notification.show(lastProfileName, R.string.status_starting)
                binder.broadcast {
                    it.onServiceResetLogs(listOf())
                }
            }

            DefaultNetworkMonitor.start()
            Libbox.registerLocalDNSTransport(LocalResolver)
            Libbox.setMemoryLimit(true)

            val newService = try {
                Libbox.newService(passedConf, platformInterface)
            } catch (e: Exception) {
                stopAndAlert(Alert.CreateService, e.message)
                return
            }

            if (delayStart) {
                delay(1000L)
            }

            newService.start()

            if (newService.needWIFIState()) {
                val wifiPermission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                } else {
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                }
                if (!service.hasPermission(wifiPermission)) {
                    newService.close()
                    stopAndAlert(Alert.RequestLocationPermission)
                    return
                }
            }

            boxService = newService
            commandServer?.setService(boxService)
            status.postValue(Status.Started)
            withContext(Dispatchers.Main) {
                notification.show(lastProfileName, R.string.status_started)
            }
            notification.start()
        } catch (e: Exception) {
            stopAndAlert(Alert.StartService, e.message)
            return
        }
    }

    override fun serviceReload() {
        notification.close()
        status.postValue(Status.Starting)
        val pfd = fileDescriptor
        if (pfd != null) {
            pfd.close()
            fileDescriptor = null
        }
        commandServer?.setService(null)
        boxService?.apply {
            runCatching {
                close()
            }.onFailure {
                writeLog("service: error when closing: $it")
            }
            Seq.destroyRef(refnum)
        }
        boxService = null
        runBlocking {
            startService(true, "")
        }
    }

    override fun getSystemProxyStatus(): SystemProxyStatus {
        val status = SystemProxyStatus()
        if (service is VPNService) {
            status.available = service.systemProxyAvailable
            status.enabled = service.systemProxyEnabled
        }
        return status
    }

    override fun postServiceClose() {

    }

    override fun setSystemProxyEnabled(isEnabled: Boolean) {
        serviceReload()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun serviceUpdateIdleMode() {
        if (CustomApplication.powerManager.isDeviceIdleMode) {
            boxService?.pause()
        } else {
            boxService?.wake()
        }
    }

    private fun stopService() {
        if (status.value != Status.Started) return
        status.value = Status.Stopping
        if (receiverRegistered) {
            service.unregisterReceiver(receiver)
            receiverRegistered = false
        }
        notification.close()
        GlobalScope.launch(Dispatchers.IO) {
            val pfd = fileDescriptor
            if (pfd != null) {
                pfd.close()
                fileDescriptor = null
            }
            commandServer?.setService(null)
            boxService?.apply {
                runCatching {
                    close()
                }.onFailure {
                    writeLog("service: error when closing: $it")
                }
                Seq.destroyRef(refnum)
            }
            boxService = null
            Libbox.registerLocalDNSTransport(null)
            DefaultNetworkMonitor.stop()

            commandServer?.apply {
                close()
                Seq.destroyRef(refnum)
            }
            commandServer = null
            withContext(Dispatchers.Main) {
                status.value = Status.Stopped
                service.stopSelf()
            }
        }
    }

    private suspend fun stopAndAlert(type: Alert, message: String? = null) {
        withContext(Dispatchers.Main) {
            if (receiverRegistered) {
                service.unregisterReceiver(receiver)
                receiverRegistered = false
            }
            notification.close()
            binder.broadcast { callback ->
                callback.onServiceAlert(type.ordinal, message)
            }
            status.value = Status.Stopped
        }
    }

    fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (status.value != Status.Stopped) return Service.START_NOT_STICKY
        status.value = Status.Starting

        if (!receiverRegistered) {
            ContextCompat.registerReceiver(service, receiver, IntentFilter().apply {
                addAction(Action.SERVICE_CLOSE)
                addAction(Action.SERVICE_RELOAD)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED)
                }
            }, ContextCompat.RECEIVER_NOT_EXPORTED)
            receiverRegistered = true
        }

        GlobalScope.launch(Dispatchers.IO) {
            initialize()
            try {
                startCommandServer()
            } catch (e: Exception) {
                stopAndAlert(Alert.StartCommandServer, e.message)
                return@launch
            }
            val passedConf = intent?.getStringExtra("daad") ?: ""
            startService(passedConf = passedConf)
        }
        return Service.START_NOT_STICKY
    }

    fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun onDestroy() {
        binder.close()
    }

    fun onRevoke() {
        stopService()
    }

    fun writeLog(message: String) {
        binder.broadcast {
            it.onServiceWriteLog(message)
        }
    }

}