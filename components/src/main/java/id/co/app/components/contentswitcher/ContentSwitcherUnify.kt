package id.co.app.components.contentswitcher

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.utils.dpToPx
import id.co.app.components.utils.spToPx

// suppress warning for ontouch to call performClick(), we need disabled the click to avoid switch toggle called by it.
@SuppressLint("ClickableViewAccessibility")
class ContentSwitcherUnify(context: Context, attrs: AttributeSet) : SwitchCompat(context, attrs) {
    private var parentWidth = 0

    init {
        background = null
        isClickable = false

        this.setOnTouchListener { _, event ->
            val xPos = event?.x?.toInt() ?: 0

            this@ContentSwitcherUnify.isChecked = (xPos >= (this@ContentSwitcherUnify.width / 2))

            false
        }
    }

    /**
     * Get the width of the ContentSwitcherUnify container
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = w

        this.post {
            switchMinWidth = measuredWidth
            trackDrawable = TrackDrawable()
            thumbDrawable = ThumbDrawable()
        }
    }

    /**
     * Set left and right margin of ContentSwitcherUnify
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val margins = MarginLayoutParams::class.java.cast(layoutParams)
        val margin = 16F.dpToPx()
        margins.leftMargin = margin.toInt()
        margins.rightMargin = margin.toInt()
        layoutParams = margins
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        // turn off animation (requested by UI)
        jumpDrawablesToCurrentState()
    }

    /**
     * Draw Track: ContentSwitcherUnify background
     */
    inner class TrackDrawable : GradientDrawable() {
        private val textOffBounds = Rect()
        private val textOnBounds = Rect()
        private var bgColor = ContextCompat.getColor(context, R.color.contentswitcherunify_track_background)

        init {
            setColor(bgColor)
            setStroke(TRACK_STROKE_WIDTH, bgColor)
            setSize(parentWidth, MIN_HEIGHT)
        }

        override fun onBoundsChange(r: Rect) {
            super.onBoundsChange(r)

            cornerRadius = CONTAINER_CORNER_RADIUS

            textOffBounds.set(r)
            textOffBounds.right /= 2

            textOnBounds.set(textOffBounds)
            textOnBounds.offset(textOffBounds.right, 0)
        }

        override fun draw(canvas: Canvas) {
            super.draw(canvas)

            drawLabel(canvas, textOffBounds, trackLabelPaint, textOff)
            drawLabel(canvas, textOnBounds, trackLabelPaint, textOn)
        }
    }

    /**
     * Draw Thumb: ContentSwitcherUnify active state
     */
    inner class ThumbDrawable : GradientDrawable() {

        private val thumbLabelBounds = Rect()


        init {
            setColor(ContextCompat.getColor(context, R.color.Unify_GN500))
            setStroke(TRACK_STROKE_WIDTH, Color.TRANSPARENT)
            setSize(parentWidth / 2, MIN_HEIGHT)
        }

        override fun onBoundsChange(r: Rect) {
            super.onBoundsChange(r)

            cornerRadius = CONTENT_CORNER_RADIUS
            thumbLabelBounds.set(r)
        }

        override fun draw(canvas: Canvas) {
            super.draw(canvas)

            drawLabel(canvas, thumbLabelBounds, thumbLabelPaint, thumbLabel)
        }
    }

    private val trackLabelPaint = Paint().apply {
        isAntiAlias = true
        textSize = TRACK_LABEL_SIZE
        typeface = Typeface.createFromAsset(context.assets, "NunitoSansExtraBold.ttf")
        color = ContextCompat.getColor(context, R.color.contentswitcherunify_thumb_background)
    }

    private val thumbLabelPaint = Paint().apply {
        isAntiAlias = true
        textSize = THUMB_LABEL_SIZE
        typeface = Typeface.createFromAsset(context.assets, "NunitoSansExtraBold.ttf")
        color = ContextCompat.getColor(context, R.color.Unify_NN0)
    }

    private val thumbLabel
        get() = if (isChecked) textOn else textOff

    companion object {
        val TRACK_STROKE_WIDTH = 12F.dpToPx().toInt()
        val TRACK_LABEL_SIZE = 14F.spToPx()
        val MIN_HEIGHT = 48F.dpToPx().toInt()
        val CONTAINER_CORNER_RADIUS = 4F.dpToPx()
        val CONTENT_CORNER_RADIUS = 6F.dpToPx()

        val THUMB_LABEL_SIZE = 14F.spToPx()

        fun drawLabel(
            canvas: Canvas,
            bounds: Rect,
            paint: Paint,
            text: CharSequence?
        ) {
            text ?: return

            val tb = RectF()
            tb.right = paint.measureText(text, 0, text.length)
            tb.bottom = paint.descent() - paint.ascent()
            tb.left += bounds.centerX() - tb.centerX()
            tb.top += bounds.centerY() - tb.centerY() - paint.ascent()

            canvas.drawText(text.toString(), tb.left, tb.top, paint)
        }
    }
}