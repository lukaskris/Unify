package id.co.app.components.notification

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.utils.toPx

class NotificationUnify : AppCompatTextView {
    companion object{
        const val COUNTER_TYPE = 1
        const val TEXT_TYPE = 2
        const val NONE_TYPE = 3
        val COLOR_PRIMARY = R.color.notificationunify_primary_type_color
        val COLOR_SECONDARY = R.color.notificationunify_secondary_type_color
        val COLOR_TEXT_TYPE = R.color.notificationunify_text_type_color
    }

    private val RAD_SIZE_SMALL = R.drawable.notification_small_bg
    private val RAD_SIZE_NORMAL = R.drawable.notification_bg
    private val RAD_SIZE_BIG = R.drawable.notification_big_bg

    private val mFontSize10 = resources.getDimensionPixelOffset(R.dimen.fontSize_lvl1)
    private val mFontSize8 = resources.getDimensionPixelOffset(R.dimen.fontSize_lvl10)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Notification, 0, 0)

        try {
            val labelTitle = typedArray.getString(R.styleable.Notification_notificationTitle) ?: ""

            val labelType = typedArray.getInteger(R.styleable.Notification_notificationType, COUNTER_TYPE)

            val labelColor = typedArray.getInt(R.styleable.Notification_notificationColor, COLOR_PRIMARY)

            val labelColorInt = when (labelColor) {
                1 -> COLOR_PRIMARY
                2 -> COLOR_SECONDARY
                3 -> COLOR_TEXT_TYPE
                else -> COLOR_PRIMARY
            }

            setTextColor(ContextCompat.getColor(context, R.color.Unify_NN0))
            setNotification(labelTitle, labelType, labelColorInt)

        } finally {
            typedArray.recycle()
        }
    }

    /**
     * create Notification to be displayed
     * @param notif The text to show
     * @param notificationType type to be applied to the Notification. Either [COUNTER_TYPE], [TEXT_TYPE], or [NONE_TYPE]
     * @param colorType backgroundcolor to be applied to the Notification. Either [COLOR_PRIMARY], [COLOR_SECONDARY], or [COLOR_TEXT_TYPE]
     */
    fun setNotification(notif: String, notificationType: Int, colorType: Int) {
        text = notif
        var textSize = mFontSize10
        var leftBasicLabel = 4

        if (notificationType == COUNTER_TYPE) {
            setPadding(0, 0, 0, 0)
            setStyle(colorType, textSize, leftBasicLabel, RAD_SIZE_NORMAL)
            setHeight(pxToDp(R.dimen.notification_size_lv3))
            setMinWidth(pxToDp(R.dimen.notification_size_lv3))
        } else if (notificationType == NONE_TYPE) {
            setStyle(colorType, textSize, leftBasicLabel, RAD_SIZE_BIG)
            setHeight(pxToDp(R.dimen.notification_size_lv1))
            setWidth(pxToDp(R.dimen.notification_size_lv1))
        } else if (notificationType == TEXT_TYPE) {
            setPadding(0, 0, 0, 0)
            textSize = mFontSize8
            setStyle(colorType, textSize, leftBasicLabel, RAD_SIZE_SMALL)
            setHeight(pxToDp(R.dimen.notification_size_lv2))
        }
    }

    private fun setStyle(backgroundColor: Int, textSize: Int, left: Int, @DrawableRes rad:Int) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        setTypeface(typeface, Typeface.BOLD)
        setPadding(left.toPx(), paddingTop, left.toPx(), paddingBottom)
        gravity = Gravity.CENTER
        includeFontPadding = false
        val drawable = ContextCompat.getDrawable(context, rad)
        drawable?.setColorFilter(resources.getColor(backgroundColor), PorterDuff.Mode.SRC_ATOP)
        setBackgroundDrawable(drawable)
    }

    private fun pxToDp(value: Int): Int{
        return context.resources.getDimensionPixelSize(value)
    }
}