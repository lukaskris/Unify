package id.co.app.notification.internal

import id.co.app.notification.entities.Payload
import id.co.app.notification.internal.utils.Action


/**
 * Created by Lukas Kristianto on 15/09/21 21.12.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

internal data class RawNotification(
    internal val meta: Payload.Meta,
    internal val alerting: Payload.Alerts,
    internal val header: Payload.Header,
    internal val content: Payload.Content,
    internal val bubblize: Payload.Bubble?,
    internal val stackable: Payload.Stackable?,
    internal val actions: ArrayList<Action>?,
    internal val progress: Payload.Progress
)