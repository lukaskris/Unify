package id.co.app.notification.entities


/**
 * Created by Lukas Kristianto on 15/09/21 20.57.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

import android.app.NotificationManager

/**
 * Provider of the initial configuration of the Notify > NotifyCreator Fluent API.
 */
data class NotificationConfig(
    /**
     * A reference to the notification manager.
     */
    internal var notificationManager: NotificationManager? = null,
    /**
     * Specifies the default configuration of a notification (e.g the default notificationIcon,
     * and notification color.)
     */
    internal var defaultHeader: Payload.Header = Payload.Header(),
    /**
     * Specifies the default configuration of a progress (e.g the default progress type)
     */
    internal var defaultProgress: Payload.Progress = Payload.Progress(),
    /**
     * Specifies the default alerting configuration for notifications.
     */
    internal var defaultAlerting: Payload.Alerts = Payload.Alerts()
) {
    fun header(init: Payload.Header.() -> Unit): NotificationConfig {
        defaultHeader.init()
        return this
    }

    fun alerting(key: String, init: Payload.Alerts.() -> Unit): NotificationConfig {
        // Clone object and assign the key.
        defaultAlerting = defaultAlerting.copy(channelKey = key)
        defaultAlerting.init()
        return this
    }

    fun progress(init: Payload.Progress.() -> Unit): NotificationConfig {
        defaultProgress.init()
        return this
    }
}