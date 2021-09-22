package id.co.app.components.notify.internal.utils


import androidx.annotation.IntDef
import id.co.app.components.notify.Notify

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
    Notify.IMPORTANCE_MIN,
    Notify.IMPORTANCE_LOW,
    Notify.IMPORTANCE_NORMAL,
    Notify.IMPORTANCE_HIGH,
    Notify.IMPORTANCE_MAX)
annotation class NotifyImportance