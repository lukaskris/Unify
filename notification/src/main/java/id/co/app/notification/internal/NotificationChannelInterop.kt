package id.co.app.notification.internal

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.os.Build
import id.co.app.notification.NotificationDelivery
import id.co.app.notification.entities.Payload

/**
 * Created by Lukas Kristianto on 15/09/21 21.02.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 *
 * Provides compatibility functionality for the Notification channels introduced in Android O.
 */
internal object NotificationChannelInterop {
    @SuppressLint("WrongConstant")
    fun with(alerting: Payload.Alerts): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false
        }

        val notificationManager = NotificationDelivery.defaultConfig.notificationManager!!

        // Ensure that the alerting is not already registered -- return true if it exists.
        notificationManager.getNotificationChannel(alerting.channelKey)?.run {
            return true
        }

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val channel = NotificationChannel(alerting.channelKey, alerting.channelName, alerting.channelImportance + 3).apply {
            description = alerting.channelDescription

            // Set the lockscreen visibility.
            lockscreenVisibility = alerting.lockScreenVisibility

            alerting.lightColor
                .takeIf { it != NotificationDelivery.NO_LIGHTS }
                ?.let {
                    enableLights(true)
                    lightColor = alerting.lightColor
                }

            alerting.vibrationPattern.takeIf { it.isNotEmpty() }?.also {
                enableVibration(true)
                vibrationPattern = it.toLongArray()
            }

            alerting.sound.also {
                setSound(it, android.media.AudioAttributes.Builder().build())
            }

            setShowBadge(alerting.showBadge)

            Unit
        }

        // Register the alerting with the system
        notificationManager.createNotificationChannel(channel)

        return true
    }
}