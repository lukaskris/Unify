package id.co.app.components.chips

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.image.ImageUnify
import id.co.app.components.notification.NotificationUnify


/**
 * Created by Lukas Kristianto on 30/08/21 10.03.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class ChipUnify : LinearLayout {
    var chip_sub_container: LinearLayout

    /**
     * get chip text reference
     */
    var chip_text: TextView

    /**
     * get chip container reference
     */
    var chip_container: LinearLayout

    /**
     * get left image of the chips reference
     */
    var chip_image_icon: ImageUnify

    /**
     * get right icon of the chips reference
     */
    var chip_right_icon: ImageView

    /**
     * get "new" notification reference
     */
    var chip_new_notification: NotificationUnify

    private var onRemoveListener: (() -> Unit)? = null

    var selectedChangeListener: ((isActive: Boolean) -> Unit)? = null

    /**
     * set/get chip text
     */
    var chipText: String? = null
        set(value) {
            field = value
            displayText()
        }

    /**
     * set/get chip left image resource
     */
    var chipImageResource: Drawable? = null
        set(value) {
            field = value
            displayImage()
        }

    var chipSize: String? = null
        set(value) {
            field = value
            setChipStyle()
        }

    var chipType: String? = null
        set(value) {
            field = value
            setChipStyle()

            when(field) {
                TYPE_SELECTED ->  selectedChangeListener?.invoke(true)
                else -> selectedChangeListener?.invoke(false)
            }

            setChevronIcon()
        }

    var centerText: Boolean = false
        set(value) {
            field = value

            setTextCenter(field)
        }

    /**
     * show "new" notification to the right of the chip
     */
    var showNewNotification: Boolean = false
        set(value) {
            field = value

            if(field) {
                chip_new_notification.visibility = View.VISIBLE
            } else {
                chip_new_notification.visibility = View.GONE
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeAttr(context, attrs, defStyleAttr)
    }

    init {
        View.inflate(context.applicationContext, R.layout.unify_chips_layout, this)

        chip_text = findViewById(R.id.chip_text)
        chip_container = findViewById(R.id.chip_container)
        chip_sub_container = findViewById(R.id.chip_sub_container)
        chip_image_icon = findViewById(R.id.chip_image_icon)
        chip_right_icon = findViewById(R.id.chip_right_icon)
        chip_new_notification = findViewById(R.id.chip_new_notification)
    }

    private fun initializeAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ChipUnify, defStyleAttr, 0)

        try {
            chipText = attributes.getString(R.styleable.ChipUnify_chipText)
            chipImageResource = attributes.getDrawable(R.styleable.ChipUnify_chipImageSrc)
            chipSize = attributes.getString(R.styleable.ChipUnify_chipSize)
            chipType = attributes.getString(R.styleable.ChipUnify_chipType)
            centerText = attributes.getBoolean(R.styleable.ChipUnify_centerText, false)
            showNewNotification = attributes.getBoolean(R.styleable.ChipUnify_showNewNotification, false)

            displayRemoveIcon()
        } finally {
            attributes.recycle()
        }
    }

    private fun displayText() {
        if (chipText != null) {
            chip_text.text = chipText
            setChipStyle()
        } else {
            chip_text.text = ""
        }
        chip_text.invalidate()
        chip_text.requestLayout()
    }

    private fun setChipStyle() {
        background = ContextCompat.getDrawable(context, R.drawable.unify_chips_bg)

        if (chipSize == SIZE_MEDIUM) {
            chip_container.layoutParams.height = resources.getDimensionPixelSize(R.dimen.chip_medium_height)
        }else if (chipSize == SIZE_SMALL) {
            chip_container.layoutParams.height = resources.getDimensionPixelSize(R.dimen.chip_small_height)
        }

        if (chipType == TYPE_ALTERNATE || chipType == TYPE_SELECTED) {
            chip_text.setTextColor(ContextCompat.getColor(context, R.color.chipsunify_text_selected))
        }else if (chipType == TYPE_DISABLE) {
            chip_text.setTextColor(ContextCompat.getColor(context, R.color.chipsunify_text_disabled))
        }else if (chipType == TYPE_NORMAL) {
            chip_text.setTextColor(ContextCompat.getColor(context, R.color.chipsunify_text_enable))
        }

        refreshDrawableState()
    }

    private fun displayImage() {
        when {
            chipImageResource != null -> {
                chip_image_icon.visibility = View.VISIBLE
                chip_image_icon.setImageDrawable(chipImageResource)
            }
            else -> {
                chip_image_icon.visibility = View.GONE
                chip_image_icon.setImageDrawable(null)
            }
        }
    }

    private fun setChevronIcon() {
        val iconAsset = when (chipType) {
            TYPE_NORMAL -> R.drawable.unify_chips_ic_chevron_normal
            TYPE_SELECTED -> R.drawable.unify_chips_ic_chevron_selected
            TYPE_DISABLE -> R.drawable.unify_chips_ic_chevron_disabled
            else -> R.drawable.unify_chips_ic_chevron_normal
        }

        chip_right_icon.setImageResource(iconAsset)
    }

    fun displayRemoveIcon() {
        if (onRemoveListener != null) {
            chip_right_icon.visibility = View.VISIBLE
            chip_right_icon.setImageResource(R.drawable.unify_chips_ic_close)
            chip_right_icon.setOnClickListener {
                onRemoveListener?.invoke()
            }
        } else {
            chip_right_icon.visibility = View.GONE
            chip_right_icon.setOnClickListener { }
        }
    }

    /**
     * set click listener to close button when chip variant contain 'X' button
     * @param listener Listener callback to be invoked when x button is clicked
     */
    fun setOnRemoveListener(listener: () -> Unit) {
        onRemoveListener = listener
        displayRemoveIcon()
    }

    /**
     * set the text to be center inside the chips
     * @param state will center the text if true
     */
    fun setTextCenter(state: Boolean) {
        if (state) {
            chip_container.gravity = Gravity.CENTER
        } else {
            chip_container.gravity = Gravity.LEFT
        }

        requestLayout()
        invalidate()
    }

    /**
     * set chevron click listener, also enable chevron icon on the right end of the chips
     * @param clickListener callback to be invoked when the chevron icon is clicked
     */
    fun setChevronClickListener(clickListener: () -> Unit) {
        chip_right_icon.visibility = View.VISIBLE

        setChevronIcon()

        chip_right_icon.setOnClickListener {
            clickListener()
        }
    }

    /**
     * clear the right icon
     */
    fun clearRightIcon() {
        chip_right_icon.visibility = View.GONE
        onRemoveListener = null
    }

    /**
     * add custom view item to ChipsUnify
     * @param item receive view
     */
    fun addCustomView(item: View) {
        chip_sub_container.removeAllViews()

        chip_sub_container.addView(item)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val newDrawableState = super.onCreateDrawableState(extraSpace+1)

        when {
            chipType == TYPE_ALTERNATE && chipSize == SIZE_MEDIUM -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipAlternateMedium})
            chipType == TYPE_ALTERNATE && chipSize == SIZE_SMALL -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipAlternateSmall})
            chipType == TYPE_NORMAL && chipSize == SIZE_MEDIUM -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipNormalMedium})
            chipType == TYPE_NORMAL && chipSize == SIZE_SMALL -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipNormalSmall})
            chipType == TYPE_DISABLE && chipSize == SIZE_MEDIUM -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipDisabledMedium})
            chipType == TYPE_DISABLE && chipSize == SIZE_SMALL -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipDisabledSmall})
            chipType == TYPE_SELECTED && chipSize == SIZE_MEDIUM -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipSelectedMedium})
            chipType == TYPE_SELECTED && chipSize == SIZE_SMALL -> View.mergeDrawableStates(newDrawableState, IntArray(1){R.attr.chipSelectedSmall})
            else -> View.mergeDrawableStates(newDrawableState, IntArray(1){ R.attr.chipNormalMedium})
        }

        return newDrawableState
    }

    companion object {
        const val SIZE_SMALL = "0"
        const val SIZE_MEDIUM = "1"

        const val TYPE_NORMAL = "0"
        const val TYPE_DISABLE = "1"
        const val TYPE_SELECTED = "2"
        const val TYPE_ALTERNATE = "3"
    }

}