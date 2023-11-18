package id.co.app.source.ui.notify

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import id.co.app.notification.NotificationDelivery
import id.co.app.source.R


/**
 * Created by Lukas Kristianto on 15/09/21 21.23.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class NotifySampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notify)

        NotificationDelivery.defaultConfig {
            header {
                color =
                    ContextCompat.getColor(this@NotifySampleActivity, R.color.color_primary_dark)
            }
            alerting(NotificationDelivery.CHANNEL_DEFAULT_KEY) {
                lightColor = Color.RED
            }
        }
    }

    fun notifyDefault(view: View) {
        NotificationDelivery.with(this)
            .content {
                title = "New dessert menu"
                text = "The Cheesecake Factory has a new dessert for you to try!"
            }
            .stackable {
                key = "test_key"
                summaryContent = "test summary content"
                summaryTitle = { count -> "Summary title" }
                summaryDescription = { count -> count.toString() + " new notifications." }
            }
            .show()
    }

    fun notifyTextList(view: View) {
        NotificationDelivery.with(this)
            .alerting("Test priority"){
                channelImportance = NotificationDelivery.IMPORTANCE_MAX
                vibrationPattern = listOf(1000, 1000)
            }
            .asTextList {
                lines = listOf(
                    "New! Fresh Strawberry Cheesecake.",
                    "New! Salted Caramel Cheesecake.",
                    "New! OREO Dream Dessert."
                )
                title = "New menu items!"
                text = lines.size.toString() + " new dessert menu items found."

            }
            .show()

    }

    fun notifyBigText(view: View) {
        NotificationDelivery.with(this)
            .asBigText {
                title = "Chocolate brownie sundae"
                text = "Try our newest dessert option!"
                expandedText = "Mouthwatering deliciousness."
                bigText =
                    "Our own Fabulous Godiva Chocolate Brownie, Vanilla Ice Cream, Hot Fudge, Whipped Cream and Toasted Almonds.\n" +
                            "\n" +
                            "Come try this delicious new dessert and get two for the price of one!"
            }
            .show()
    }

    fun notifyBigPicture(view: View) {
        NotificationDelivery.with(this)
            .asBigPicture {
                title = "Chocolate brownie sundae"
                text = "Get a look at this amazing dessert!"
                expandedText = "The delicious brownie sundae now available."
                image = BitmapFactory.decodeResource(
                    this@NotifySampleActivity.resources,
                    R.drawable.chocolate_brownie_sundae
                )
            }
            .show()
    }

    fun notifyMessage(view: View) {
        NotificationDelivery.with(this)
            .asMessage {
                userDisplayName = "Karn"
                conversationTitle = "Sundae chat"
                messages = listOf(
                    NotificationCompat.MessagingStyle.Message(
                        "Are you guys ready to try the Strawberry sundae?",
                        System.currentTimeMillis() - (6 * 60 * 1000), // 6 Mins ago
                        "Karn"
                    ),
                    NotificationCompat.MessagingStyle.Message(
                        "Yeah! I've heard great things about this place.",
                        System.currentTimeMillis() - (5 * 60 * 1000), // 5 Mins ago
                        "Nitish"
                    ),
                    NotificationCompat.MessagingStyle.Message(
                        "What time are you getting there Karn?",
                        System.currentTimeMillis() - (1 * 60 * 1000), // 1 Mins ago
                        "Moez"
                    )
                )
            }
            .show()
    }

    fun notifyBubble(view: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Toast.makeText(
                this,
                "Notification Bubbles are only supported on a device running Android Q or later.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        NotificationDelivery.with(this)
            .content {
                title = "New dessert menu"
                text = "The Cheesecake Factory has a new dessert for you to try!"
            }
            .bubblize {
                // Create bubble intent
                val target = Intent(this@NotifySampleActivity, BubbleActivity::class.java)
                val bubbleIntent =
                    PendingIntent.getActivity(this@NotifySampleActivity, 0, target,
                        PendingIntent.FLAG_IMMUTABLE /* flags */)

                bubbleIcon = IconCompat.createWithResource(
                    this@NotifySampleActivity,
                    R.drawable.cricket
                )
                targetActivity = bubbleIntent
                suppressInitialNotification = true
            }
            .show()
    }

    fun notifyIndeterminateProgress(view: View) {

        NotificationDelivery.with(this)
            .asBigText {
                title = "Uploading files"
                expandedText = "The files are being uploaded!"
                bigText = "Daft Punk - Get Lucky.flac is uploading to server /music/favorites"
            }
            .progress {
                showProgress = true
            }
            .show()
    }

    fun notifyDeterminateProgress(view: View) {

        NotificationDelivery.with(this)
            .asBigText {
                title = "Bitcoin payment processing"
                expandedText = "Your payment was sent to the Bitcoin network"
                bigText = "Your payment #0489 is being confirmed 2/4"
            }
            .progress {
                showProgress = true
                enablePercentage = true
                progressPercent = 30
            }
            .show()

    }
}