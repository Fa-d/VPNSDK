package com.faddy.phoenixlib.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@AndroidEntryPoint
class CountdownTimerService : Service() {

    @Inject
    lateinit var sessionManager: SessionManagerInternal

    private val timer = Timer()
    private var startTime = 0L
    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun getTimestamp(time: Long): String {
        val df = DecimalFormat("00")
        val elapsedMillis = SystemClock.elapsedRealtime() - time
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = (elapsedMillis - hours * 3600000).toInt() / 60000
        val seconds = (elapsedMillis - hours * 3600000 - minutes * 60000).toInt() / 1000
        var text = ""
        text += df.format(hours) + ":"
        text += df.format(minutes) + ":"
        text += df.format(seconds)
        return text

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTime = SystemClock.elapsedRealtime()
        sessionManager.setLastConnStartTime(System.currentTimeMillis())
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val hms = getTimestamp(startTime)
                val timerInfoIntent = Intent(TIME_INFO)
                timerInfoIntent.putExtra("VALUE", hms)
                LocalBroadcastManager.getInstance(this@CountdownTimerService)
                    .sendBroadcast(timerInfoIntent)
            }
        }, 0L, 1000L)

        return START_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
        val timerInfoIntent = Intent(TIME_INFO)
        timerInfoIntent.putExtra("myIpTimerReciever", "Stopped")
        LocalBroadcastManager.getInstance(this@CountdownTimerService).sendBroadcast(timerInfoIntent)
        isServiceStarted = false
    }

    companion object {
        const val TIME_INFO = "time_info"
        var isServiceStarted = false

    }
}