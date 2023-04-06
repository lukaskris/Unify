package id.co.app.notification.internal.utils


import androidx.annotation.IntDef
import id.co.app.notification.NotificationDelivery

/**
 * Created by Lukas Kristianto on 15/09/21 20.59.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

annotation class Experimental

@DslMarker
annotation class NotifyScopeMarker

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    NotificationDelivery.IMPORTANCE_MIN,
    NotificationDelivery.IMPORTANCE_LOW,
    NotificationDelivery.IMPORTANCE_NORMAL,
    NotificationDelivery.IMPORTANCE_HIGH,
    NotificationDelivery.IMPORTANCE_MAX)
annotation class NotifyImportance