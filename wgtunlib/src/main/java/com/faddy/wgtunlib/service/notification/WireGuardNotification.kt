package com.faddy.wgtunlib.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.faddy.wgtunlib.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WireGuardNotification @Inject constructor(@ApplicationContext private val context: Context) :
    NotificationService {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val watcherBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.watcher_channel_id),
    )
    private val tunnelBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.vpn_channel_id),
    )

    override fun createNotification(
        channelId: String,
        channelName: String,
        title: String,
        action: PendingIntent?,
        actionText: String?,
        description: String,
        showTimestamp: Boolean,
        importance: Int,
        vibration: Boolean,
        onGoing: Boolean,
        lights: Boolean,
        onlyAlertOnce: Boolean,
    ): Notification {
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelId,
                channelName,
                importance,
            ).let {
                it.description = title
                it.enableLights(lights)
                it.lightColor = Color.RED
                it.enableVibration(vibration)
                it.vibrationPattern = longArrayOf(100, 200, 300)
                it
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(channel)/*val pendingIntent: PendingIntent =
            Intent(context, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE,
                )
            }*/

        val builder = when (channelId) {
            context.getString(R.string.watcher_channel_id) -> watcherBuilder
            context.getString(R.string.vpn_channel_id) -> tunnelBuilder
            else -> {
                NotificationCompat.Builder(
                    context,
                    channelId,
                )
            }
        }

        return builder.let {
            if (action != null && actionText != null) {
                it.addAction(
                    NotificationCompat.Action.Builder(0, actionText, action).build(),
                )
                it.setAutoCancel(true)
            }
            it.setContentTitle(title).setContentText(description).setOnlyAlertOnce(onlyAlertOnce)
                // .setContentIntent(pendingIntent)
                .setOngoing(onGoing).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setShowWhen(showTimestamp).setSmallIcon(androidx.core.R.drawable.notification_bg)
                .build()
        }
    }
}
