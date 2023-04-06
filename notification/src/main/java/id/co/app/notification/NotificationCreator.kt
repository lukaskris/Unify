package id.co.app.notification

import android.annotation.TargetApi
import android.os.Build
import androidx.core.app.NotificationCompat
import id.co.app.notification.entities.Payload
import id.co.app.notification.internal.RawNotification
import id.co.app.notification.internal.utils.Action
import id.co.app.notification.internal.utils.Errors
import id.co.app.notification.internal.utils.NotifyScopeMarker


/**
 * Created by Lukas Kristianto on 15/09/21 21.13.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 *
 * Fluent API for creating a Notification object.
 */
@NotifyScopeMarker
class NotificationCreator internal constructor(private val notificationDelivery: NotificationDelivery) {

    private var meta = Payload.Meta()
    private var alerts = NotificationDelivery.defaultConfig.defaultAlerting
    private var header = NotificationDelivery.defaultConfig.defaultHeader.copy()
    private var content: Payload.Content = Payload.Content.Default()
    private var actions: ArrayList<Action>? = null
    private var bubblize: Payload.Bubble? = null
    private var stackable: Payload.Stackable? = null
    private var progress: Payload.Progress = Payload.Progress()

    /**
     * Scoped function for modifying the Metadata of a notification, such as click intents,
     * notification category, and priority among other options.
     */
    fun meta(init: Payload.Meta.() -> Unit): NotificationCreator {
        this.meta.init()

        return this
    }

    /**
     * Scoped function for modifying the Alerting of a notification. This includes visibility,
     * sounds, lights, etc.
     *
     * If an existing key is provided the existing channel is retrieved (API >= AndroidO) and set as the alerting
     * configuration. If the key is new, the channel is created and set as the alerting configuration.
     */
    fun alerting(key: String, init: Payload.Alerts.() -> Unit): NotificationCreator {
        // Clone object and assign the key.
        this.alerts = this.alerts.copy(channelKey = key).also(init)
        return this
    }

    /**
     * Scoped function for modifying the Header of a notification. Specifically, it allows the
     * modification of the notificationIcon, color, the headerText (optional text next to the
     * appName), and finally the notifyChannel of the notification if targeting Android O.
     */
    fun header(init: Payload.Header.() -> Unit): NotificationCreator {
        this.header.init()
        return this
    }

    fun progress(init: Payload.Progress.() -> Unit): NotificationCreator {
        this.progress.init()
        return this
    }

    /**
     * Scoped function for modifying the content of a 'Default' notification.
     */
    fun content(init: Payload.Content.Default.() -> Unit): NotificationCreator {
        this.content = Payload.Content.Default().also(init)
        return this
    }

    /**
     * Scoped function for modifying the content of a 'TextList' notification.
     */
    fun asTextList(init: Payload.Content.TextList.() -> Unit): NotificationCreator {
        this.content = Payload.Content.TextList().also(init)
        return this
    }

    /**
     * Scoped function for modifying the content of a 'BigText' notification.
     */
    fun asBigText(init: Payload.Content.BigText.() -> Unit): NotificationCreator {
        this.content = Payload.Content.BigText().also(init)
        return this
    }

    /**
     * Scoped function for modifying the content of a 'BigPicture' notification.
     */
    fun asBigPicture(init: Payload.Content.BigPicture.() -> Unit): NotificationCreator {
        this.content = Payload.Content.BigPicture().also(init)
        return this
    }

    /**
     * Scoped function for modifying the content of a 'Message' notification.
     */
    fun asMessage(init: Payload.Content.Message.() -> Unit): NotificationCreator {
        this.content = Payload.Content.Message().also(init)
        return this
    }

    /**
     * Scoped function for modifying the 'Actions' of a notification. The transformation
     * relies on adding standard notification Action objects.
     */
    fun actions(init: ArrayList<Action>.() -> Unit): NotificationCreator {
        this.actions = ArrayList<Action>().also(init)
        return this
    }

    /**
     * Scoped function for modifying the behaviour of 'Bubble' notifications. The transformation
     * relies on the 'bubbleIcon' and 'targetActivity' values which are used to create the Bubble.
     *
     * Note that Bubbles have very specific restrictions in terms of when they can be shown to the
     * user. In particular, at least one of the following conditions must be met before the
     * notification is shown.
     * - The notification uses MessagingStyle, and has a Person added.
     * - The notification is from a call to Service.startForeground, has a category of
     *   CATEGORY_CALL, and has a Person added.
     * - The app is in the foreground when the notification is sent.
     *
     * In addition, the 'Bubbles' flag has to be enabled from the Android Developer Options in the
     * Settings of the Device for the notifications to be shown as Bubbles.
     *
     * Finally, the 'targetActivity' should also have the following attributes to correctly show a
     * Bubble notification.
     *
     *      android:documentLaunchMode="always"
     *      android:resizeableActivity="true"
     *      android:screenOrientation="portrait"
     *
     */

    @TargetApi(Build.VERSION_CODES.Q)
    fun bubblize(init: Payload.Bubble.() -> Unit): NotificationCreator {
        this.bubblize = Payload.Bubble().also(init)

        this.bubblize!!
            .takeUnless { it.bubbleIcon == null }
            ?: throw IllegalArgumentException(Errors.INVALID_BUBBLE_ICON_ERROR)

        this.bubblize!!
            .takeUnless { it.targetActivity == null }
            ?: throw IllegalArgumentException(Errors.INVALID_BUBBLE_TARGET_ACTIVITY_ERROR)

        return this
    }

    /**
     * Scoped function for modifying the behaviour of 'Stacked' notifications. The transformation
     * relies on the 'summaryText' of a stackable notification.
     */
    fun stackable(init: Payload.Stackable.() -> Unit): NotificationCreator {
        this.stackable = Payload.Stackable().also(init)

        this.stackable!!
            .takeUnless { it.key.isNullOrEmpty() }
            ?: throw IllegalArgumentException(Errors.INVALID_STACK_KEY_ERROR)

        return this
    }

    /**
     * Return the standard {@see NotificationCompat.Builder} after applying fluent API
     * transformations (if any) from the {@see NotifyCreator} builder object.
     */
    fun asBuilder(): NotificationCompat.Builder {
        return notificationDelivery.asBuilder(RawNotification(meta, alerts, header, content, bubblize, stackable, actions, progress))
    }


    /**
     * Delegate a @see{ Notification.Builder} object to the NotificationInterop class which builds
     * and displays the notification.
     *
     * This is a terminal operation.
     *
     * @return An integer corresponding to the ID of the system notification. Any updates should use
     * this returned integer to make updates or to cancel the notification.
     */
    fun show(): Int {
        return notificationDelivery.show(null, asBuilder())
    }

    /**
     * Delegate a {@see Notification.Builder} object to the NotificationInterop class which builds
     * and displays the notification.
     *
     * This is a terminal operation.
     *
     * @param id    An optional integer which will be used as the ID for the notification that is
     *              shown. This argument is ignored if the notification is a NotifyCreator#stackable
     *              receiver is set.
     * @return An integer corresponding to the ID of the system notification. Any updates should use
     * this returned integer to make updates or to cancel the notification.
     */
    fun show(id: Int?): Int {
        return notificationDelivery.show(id, asBuilder())
    }

}