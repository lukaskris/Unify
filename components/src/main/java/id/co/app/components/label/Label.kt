package id.co.app.components.label

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.utils.toPx
import kotlin.math.roundToInt

/**
 * Created by Lukas Kristianto on 7/15/2021 00:32.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class Label : AppCompatTextView {

    private var labelColorList: Array<IntArray>? = null

    var unlockFeature = false
    var fontColorByPass = "#FFFFFF"

    /** set opacity level from 0 to 1 **/
    var opacityLevel:Float = 1f
    set(value) {
        field = value

        drawable?.alpha = (field * 255).roundToInt()
    }

    /**
     * to show timer icon in the left hand side of the Label
     */
    var timerIcon: Boolean = false
        set(value) {
            field = value
            if (value) {
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.unify_timer_ic, 0, 0, 0)
                setCompoundDrawablePadding(4.toPx())
            } else {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }

    private val drawable = ContextCompat.getDrawable(context, R.drawable.label_bg)

    constructor(context: Context) : super(context) {
        init(context)
    }

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

    private fun init(context: Context, attrs: AttributeSet? = null) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Label, 0, 0)

        try {
            val labelTitle = typedArray.getString(R.styleable.Label_labelTitle)
            opacityLevel = typedArray.getFloat(R.styleable.Label_labelOpacityLevel, 1f)
            if (labelTitle != null) setLabel(labelTitle)

            val labelType = typedArray.getInteger(R.styleable.Label_labelType, GENERAL_DARK_GREY)
            setLabelType(labelType)

            val labelTimerIcon = typedArray.getBoolean(R.styleable.Label_labelTimerIcon, false)
            timerIcon = labelTimerIcon
        } finally {
            typedArray.recycle()
        }

        labelColorList = arrayOf(
            intArrayOf(R.color.Unify_N150, GENERAL_DARK_GREY),
            intArrayOf(R.color.Unify_G400, GENERAL_DARK_GREEN),
            intArrayOf(R.color.Unify_B400, GENERAL_DARK_BLUE),
            intArrayOf(R.color.Unify_Y400, GENERAL_DARK_ORANGE),
            intArrayOf(R.color.Unify_R400, GENERAL_DARK_RED),
            intArrayOf(R.color.Unify_R100, GENERAL_LIGHT_RED),
            intArrayOf(R.color.Unify_G200, GENERAL_LIGHT_GREEN),
            intArrayOf(R.color.Unify_B200, GENERAL_LIGHT_BLUE),
            intArrayOf(R.color.Unify_N50, GENERAL_LIGHT_GREY),
            intArrayOf(R.color.Unify_Y200, GENERAL_LIGHT_ORANGE),
            intArrayOf(R.color.Unify_Y500, TIME_HIGH_PRIORITY),
            intArrayOf(R.color.Unify_Y400, TIME_MEDIUM_PRIORITY),
            intArrayOf(R.color.Unify_Y300, TIME_LOW_PRIORITY)
        )

        gravity = Gravity.CENTER_VERTICAL
    }

    @Deprecated("use setLabelImage(image: Drawable?, width: Int, height: Int)")
    fun setLabelImage(imageResourceId: Int) {
        val image = context.resources.getDrawable(imageResourceId)
        scaleX = 0.5.toFloat()
        scaleY = 0.5.toFloat()
        compoundDrawablePadding = getUnitDp(8)
        setCompoundDrawablesWithIntrinsicBounds(image, null, null, null)
    }

    fun setLabelImage(image: Drawable?, width: Int, height: Int) {
        compoundDrawablePadding = 8.toPx()
        image?.setBounds(0, 0, width, height)

        setCompoundDrawables(image, null, null, null)
    }

    fun setLabel(label: String) {
        text = label
    }

    fun setLabelType(labelType: Int) {
        val textSize = resources.getDimensionPixelOffset(R.dimen.fontSize_lvl1)
        val textSizeTimePriority = resources.getDimensionPixelOffset(R.dimen.fontSize_lvl6)
        val leftTimeLabel = 12
        val leftBasicLabel = 4

        when (labelType) {
            GENERAL_DARK_GREY -> {
                setStyle(R.color.labelunify_dark_grey_background, R.color.labelunify_dark_time_general_text, textSize, leftBasicLabel)
            }
            GENERAL_DARK_GREEN -> {
                setStyle(R.color.labelunify_dark_green_background, R.color.labelunify_dark_time_general_text, textSize, leftBasicLabel)
            }
            GENERAL_DARK_BLUE -> {
                setStyle(R.color.labelunify_dark_blue_background, R.color.labelunify_dark_time_general_text, textSize, leftBasicLabel)
            }
            GENERAL_DARK_ORANGE -> {
                setStyle(R.color.labelunify_dark_orange_background, R.color.labelunify_dark_time_general_text, textSize, leftBasicLabel)
            }
            GENERAL_DARK_RED -> {
                setStyle(R.color.labelunify_dark_red_background, R.color.labelunify_dark_time_general_text, textSize, leftBasicLabel)
            }
            GENERAL_LIGHT_RED -> {
                setStyle(R.color.labelunify_light_red_background, R.color.labelunify_light_red_text, textSize, leftBasicLabel)
            }
            GENERAL_LIGHT_GREEN -> {
                setStyle(R.color.labelunify_light_green_background, R.color.labelunify_light_green_text, textSize, leftBasicLabel)
            }
            GENERAL_LIGHT_BLUE -> {
                setStyle(R.color.labelunify_light_blue_background, R.color.labelunify_light_blue_text, textSize, leftBasicLabel)
            }
            GENERAL_LIGHT_GREY -> {
                setStyle(R.color.labelunify_light_grey_background, R.color.labelunify_light_grey_text, textSize, leftBasicLabel)
            }
            GENERAL_LIGHT_ORANGE -> {
                setStyle(R.color.labelunify_light_orange_background, R.color.labelunify_light_orange_text, textSize, leftBasicLabel)
            }
            TIME_HIGH_PRIORITY -> {
                setStyle(R.color.labelunify_time_high_background, R.color.labelunify_dark_time_general_text, textSizeTimePriority, leftTimeLabel)
            }
            TIME_MEDIUM_PRIORITY -> {
                setStyle(R.color.labelunify_time_medium_background, R.color.labelunify_dark_time_general_text, textSizeTimePriority, leftTimeLabel)
            }
            TIME_LOW_PRIORITY -> {
                setStyle(R.color.labelunify_time_low_background, R.color.labelunify_dark_time_general_text, textSizeTimePriority, leftTimeLabel)
            }
        }
    }

    /**
     * Set Label using hexa color code, only #FFFFFF hexa format can be accepted on param.
     * @param hexColor
     */
    fun setLabelType(hexColor: String) {
        val intHexColor = Color.parseColor(hexColor)
        val newColor =  Color.rgb(Color.red(intHexColor), Color.green(intHexColor), Color.blue(intHexColor))
        opacityLevel = Color.alpha(intHexColor).toFloat() / 255

        for (i in labelColorList!!.indices) {
            val intListColor = ContextCompat.getColor(context, labelColorList!![i][0])

            if (intHexColor == intListColor) {
                setLabelType(labelColorList!![i][1])
                return
            }
        }
        
        if (unlockFeature) {
            val fontColor = Color.parseColor(fontColorByPass)
            val textSize = resources.getDimensionPixelOffset(R.dimen.fontSize_lvl1)
            val leftBasicLabel = 4

            setTextColor(fontColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            setTypeface(typeface, Typeface.BOLD)
            setPadding(
                getUnitDp(leftBasicLabel),
                getUnitDp(3),
                getUnitDp(leftBasicLabel),
                getUnitDp(3)
            )
            drawable?.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP)
            drawable?.alpha = (opacityLevel * 255).roundToInt()
            setBackgroundDrawable(drawable)
        } else {
            setLabelType(GENERAL_DARK_GREEN)
        }
    }

    fun getResColor(colorReference: Int): Int {
        return ContextCompat.getColor(context, colorReference)
    }

    private fun setStyle(backgroundColor: Int, textColor: Int, textSize: Int, left: Int) {
        setTextColor(resources.getColor(textColor))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        setTypeface(typeface, Typeface.BOLD)
        setPadding(getUnitDp(left), getUnitDp(3), getUnitDp(left), getUnitDp(3))
        drawable?.setColorFilter(resources.getColor(backgroundColor), PorterDuff.Mode.SRC_ATOP)
        drawable?.alpha = (opacityLevel * 255).roundToInt()
        setBackgroundDrawable(drawable)
    }

    private fun getUnitDp(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    companion object {
        val GENERAL_DARK_GREY = 1
        val GENERAL_DARK_GREEN = 2
        val GENERAL_DARK_BLUE = 3
        val GENERAL_DARK_ORANGE = 4
        val GENERAL_DARK_RED = 5
        val GENERAL_LIGHT_RED = 6
        val GENERAL_LIGHT_GREEN = 7
        val GENERAL_LIGHT_BLUE = 8
        val GENERAL_LIGHT_GREY = 9
        val GENERAL_LIGHT_ORANGE = 10
        val TIME_HIGH_PRIORITY = 11
        val TIME_LOW_PRIORITY = 12
        val TIME_MEDIUM_PRIORITY = 13
    }
}