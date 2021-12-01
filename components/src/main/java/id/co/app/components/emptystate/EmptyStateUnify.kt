package id.co.app.components.emptystate

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.button.UnifyButton
import id.co.app.components.typography.Typography
import id.co.app.components.utils.setImage
import id.co.app.components.utils.toDp
import id.co.app.components.utils.toPx

open class EmptyStateUnify(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    /**
     * get empty state title
     */
    var emptyStateTitle: String = ""

    /**
     * get empty state description
     */
    var emptyStateDescription: String = ""

    /**
     * get empty state image url
     */
    var emptyStateImage: String = ""

    /**
     * get empty state image drawable
     */
    var emptyStateImageDrawable: Drawable? = null

    /**
     * get empty state orientation. 1 = VERTICAL, 2 = HORIZONTAL
     */
    var emptyStateOrientation: Int = 0

    /**
     * get empty state type. 1 = PAGE, 2 = SECTION
     */
    var emptyStateType: Int = 0

    /**
     * get empty state primary CTA text
     */
    var emptyStatePrimaryCTAText: String = ""

    /**
     * get empty state secondary CTA text
     */
    var emptyStateSecondaryCTAText: String = ""

    /**
     * get empty state secondary CTA text
     */
    var emptyStateCTAFullWidth: Boolean = false
    var primaryColor: Int = 0

    var emptyStateWrapper: LinearLayout
    var emptyStateContentContainerID: LinearLayout
    var emptyStateImageID: ImageView
    var emptyStateTextContainerID: LinearLayout
    var emptyStateTitleID: Typography
    var emptyStateDescriptionID: Typography
    var emptyStateCTAID: UnifyButton
    val emptyStateSecondaryCTAID: UnifyButton
    val emptyStateCTAContainerID: LinearLayout

    init {
        View.inflate(context, R.layout.empty_state_unify_layout,this)

        emptyStateWrapper = findViewById(R.id.empty_state_wrapper)
        emptyStateContentContainerID = findViewById(R.id.empty_state_content_container_id)
        emptyStateImageID = findViewById(R.id.empty_state_image_id)
        emptyStateTextContainerID = findViewById(R.id.empty_state_text_container_id)
        emptyStateTitleID = findViewById(R.id.empty_state_title_id)
        emptyStateDescriptionID = findViewById(R.id.empty_state_description_id)
        emptyStateCTAID = findViewById(R.id.empty_state_cta_id)
        emptyStateSecondaryCTAID = findViewById(R.id.empty_state_secondary_cta_id)
        emptyStateCTAContainerID = findViewById(R.id.empty_state_cta_container)

        initializeAttr(attributeSet)
        initializeText()
        initializeImage()
        initializeCTAVisibility()
        initializeOrientation()
        initializeLayout()
    }

    /**
     * Set the title displayed on this Empty State
     * @param title String that will be displayed
     */
    fun setTitle(title: String) {
        emptyStateTitleID.text = title
    }

    /**
     * Set the description displayed on this Empty State
     * @param description String that will be displayed
     */
    fun setDescription(description: String) {
        emptyStateDescriptionID.text = description
    }

    /**
     * Set the secondary CTA text
     * @param text String that will be displayed. Set to empty string to hide the CTA
     */
    fun setSecondaryCTAText(text: String) {
        emptyStateSecondaryCTAID.text = text

        if (text.isEmpty()) {
            emptyStateSecondaryCTAID.visibility = View.GONE
        } else {
            emptyStateSecondaryCTAID.visibility = View.VISIBLE
        }
    }

    /**
     * Set the primary CTA text
     * @param text String that will be displayed. Set to empty string to hide the CTA
     */
    fun setPrimaryCTAText(text: String) {
        emptyStateCTAID.text = text

        if (text.isEmpty()) {
            emptyStateCTAID.visibility = View.GONE
        } else {
            emptyStateCTAID.visibility = View.VISIBLE
        }
    }

    /**
     * Set listener for primary CTA
     * @param Unit
     */
    fun setPrimaryCTAClickListener(onCTAClickListener: () -> Unit) {
        emptyStateCTAID.setOnClickListener {
            onCTAClickListener()
        }
    }

    /**
     * Set listener for secondary CTA
     * @param Unit
     */
    fun setSecondaryCTAClickListener(onSecondaryCTAClickListener: () -> Unit) {
        emptyStateSecondaryCTAID.setOnClickListener {
            onSecondaryCTAClickListener()
        }
    }

    /**
     * Set image displayed for this Empty State
     * @param url Url that will be loaded for the image
     */
    fun setImageUrl(url: String) {
        emptyStateImageID.setImage(url, 0f)
    }

    /**
     * Set image displayed for this Empty State
     * @param drawable Drawable that will be loaded for the image
     */
    fun setImageDrawable(drawable: Drawable) {
        emptyStateImageID.setImageDrawable(drawable)
    }

    /**
     * Set image displayed for this Empty State
     * @param bitmap Bitmap that will be loaded for the image
     */
    fun setImageBitmap(bitmap: Bitmap) {
        emptyStateImageID.setImageBitmap(bitmap)
    }

    /**
     * Set orientation for this Empty State
     * @param orientation EmptyState.Orientation.VERITCAL or EmptyState.Orientation.HORIZONTAL
     */
    fun setOrientation(orientation: Int) {
        when(orientation) {
            Orientation.HORIZONTAL -> setOrientationHorizontal()
            Orientation.VERTICAL -> setOrientationVertical()
        }
    }

    private fun initializeAttr(attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.EmptyStateUnify)
        emptyStateTitle = attributeArray.getString(R.styleable.EmptyStateUnify_emptyStateTitle) ?: ""
        emptyStateDescription = attributeArray.getString(R.styleable.EmptyStateUnify_emptyStateDescription) ?: ""
        emptyStateImage = attributeArray.getString(R.styleable.EmptyStateUnify_emptyStateImageUrl) ?: ""
        try {
            var resourcesId = attributeArray.getResourceId(R.styleable.EmptyStateUnify_emptyStateImageDrawable, -1)
            emptyStateImageDrawable = ContextCompat.getDrawable(context, resourcesId)
        } catch(e: Exception) {
            var k = ""
        }
        emptyStateOrientation = attributeArray.getInt(R.styleable.EmptyStateUnify_emptyStateOrientation, Orientation.VERTICAL)
        emptyStateType = attributeArray.getInt(R.styleable.EmptyStateUnify_emptyStateType, Type.PAGE)
        emptyStatePrimaryCTAText = attributeArray.getString(R.styleable.EmptyStateUnify_emptyStatePrimaryCTAText) ?: ""
        emptyStateSecondaryCTAText = attributeArray.getString(R.styleable.EmptyStateUnify_emptyStateSecondaryCTAText) ?: ""
        emptyStateCTAFullWidth = attributeArray.getBoolean(R.styleable.EmptyStateUnify_emptyStateCTAFullWidth, false)
        primaryColor = attributeArray.getColor(
            R.styleable.UnifyButton_unifyButtonColorPrimary,
            ContextCompat.getColor(context, R.color.Unify_R500)
        )
        attributeArray.recycle()
    }

    private fun initializeText() {
        emptyStateTitleID.text = emptyStateTitle
        if (emptyStateOrientation == Orientation.HORIZONTAL) {
            emptyStateTitleID.setType(Typography.HEADING_4)
            val lp = emptyStateTitleID.layoutParams as LinearLayout.LayoutParams
            lp.setMargins(0, 0, 0, 0)
        }
        emptyStateDescriptionID.text = emptyStateDescription
        emptyStateDescriptionID.setTextColor(ContextCompat.getColor(context, R.color.Unify_N700_68))
        emptyStateCTAID.text = emptyStatePrimaryCTAText
        emptyStateSecondaryCTAID.text = emptyStateSecondaryCTAText
    }

    private fun initializeImage() {
        if (emptyStateImage.isNotEmpty()) {
            emptyStateImageID.setImage(emptyStateImage, 0f)
        }

        val emptyStateImageDrawableVar = emptyStateImageDrawable

        emptyStateImageDrawableVar?.let {
            setImageDrawable(emptyStateImageDrawableVar)
        }
    }

    private fun initializeCTAVisibility() {
        if (emptyStatePrimaryCTAText.isEmpty()) {
            emptyStateCTAID.visibility = View.GONE
        }

        if (emptyStateSecondaryCTAText.isEmpty()) {
            emptyStateSecondaryCTAID.visibility = View.GONE
        }
    }

    private fun initializeOrientation() {
        when(emptyStateOrientation) {
            Orientation.HORIZONTAL -> setOrientationHorizontal()
            Orientation.VERTICAL -> setOrientationVertical()
        }
    }

    private fun initializeLayout() {
        emptyStateCTAID.setPrimaryColor(primaryColor)
        if (emptyStateCTAFullWidth) {
            emptyStateCTAID.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            emptyStateSecondaryCTAID.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            emptyStateCTAID.layoutParams.width = 200.toPx()
            emptyStateSecondaryCTAID.layoutParams.width = 200.toPx()

            when(emptyStateType) {
                Type.PAGE -> {
                    emptyStateCTAID.buttonSize = UnifyButton.Size.LARGE
                    emptyStateSecondaryCTAID.buttonSize = UnifyButton.Size.LARGE
                    emptyStateCTAID.layoutParams.width = 200.toPx()
                }
                Type.SECTION -> {
                    val hasSecondaryCTA = emptyStateSecondaryCTAID.visibility == View.VISIBLE
                    val containerLp = emptyStateCTAContainerID.layoutParams as LinearLayout.LayoutParams
                    containerLp.setMargins(0, 12.toPx(), 0, 0)

                    // changing the layout of buttons when type is section
                    emptyStateCTAContainerID.orientation = LinearLayout.HORIZONTAL
                    emptyStateCTAID.buttonSize = UnifyButton.Size.SMALL
                    emptyStateSecondaryCTAID.apply {
                        buttonSize = UnifyButton.Size.SMALL
                        buttonVariant = UnifyButton.Variant.GHOST
                        buttonType = UnifyButton.Type.MAIN
                    }
                    emptyStateCTAContainerID.weightSum = 2f
                    val primaryLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)

                    // primary cta will fill the container if no secondary cta displayed
                    primaryLp.weight = if (hasSecondaryCTA) 1f else 2f
                    primaryLp.setMargins(if (hasSecondaryCTA) 4.toPx() else 0, 0, 0, 0)

                    val secondaryLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                    secondaryLp.weight = 1f
                    secondaryLp.setMargins(0, 0, 4.toPx(), 0)

                    if (hasSecondaryCTA) {
                        // re attach primary cta button to swap the position
                        emptyStateCTAContainerID.removeView(emptyStateCTAID)
                        emptyStateCTAContainerID.addView(emptyStateCTAID)
                    }

                    emptyStateCTAID.layoutParams = primaryLp
                    emptyStateSecondaryCTAID.layoutParams = secondaryLp
                }
            }
        }

        val scale = Resources.getSystem().displayMetrics.density
        val widthDp = (Resources.getSystem().displayMetrics.widthPixels / scale).toInt()

        if (widthDp > 500) {
            emptyStateWrapper.layoutParams.width = 500.toPx()
        }
    }

    private fun setOrientationHorizontal() {
        emptyStateContentContainerID.orientation = LinearLayout.HORIZONTAL
        emptyStateTitleID.gravity = Gravity.START
        emptyStateDescriptionID.gravity = Gravity.START
        val scale: Float = context.resources.displayMetrics.density
        val paddingLeft: Int = (24 * scale + 0.5f).toInt()
        emptyStateTextContainerID.setPadding(paddingLeft, 0, 0, 0)
        emptyStateImageID.layoutParams.width = 0
        emptyStateImageID.setPadding(0, 0, 0, 0)
        emptyStateTextContainerID.layoutParams.width = 0
    }

    private fun setOrientationVertical() {
        emptyStateContentContainerID.orientation = LinearLayout.VERTICAL
        emptyStateTitleID.gravity = Gravity.CENTER_HORIZONTAL
        emptyStateDescriptionID.gravity = Gravity.CENTER_HORIZONTAL
        emptyStateTextContainerID.setPadding(0, 0, 0, 0)
        val scale = Resources.getSystem().displayMetrics.density
        val widthDp = ((Resources.getSystem().displayMetrics.widthPixels / scale) / 1.6).toInt()
        val imageParams = emptyStateImageID.layoutParams
        imageParams.width = (widthDp * scale + 0.5f).toInt()
        emptyStateImageID.layoutParams = imageParams
        if (emptyStateImageID.layoutParams.width.toDp() > 260) {
            emptyStateImageID.layoutParams.width = 260.toPx()
        }
        emptyStateImageID.setPadding(0, 0, 0, 24)
        emptyStateTextContainerID.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }

    object Orientation {
        const val VERTICAL = 1
        const val HORIZONTAL = 2
    }

    object Type {
        const val PAGE = 1
        const val SECTION = 2
    }
}