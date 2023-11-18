package id.co.app.components.button

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import id.co.app.components.R
import id.co.app.components.utils.toPx

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class UnifyButton : AppCompatTextView {

    var buttonSize = Size.LARGE
        set(value) {
            if (buttonSize == value) return
            field = value
            iconDrawable?.let {
                setDrawable(it)
            }
            refresh()
        }
    var buttonVariant = Variant.FILLED
        set(value) {
            if (buttonVariant == value) return
            field = value
            refresh()
        }
    var isInverse = false
        set(value) {
            field = value
            if (buttonVariant != Variant.GHOST) return
            refresh()
        }
    var buttonType = Type.MAIN
        set(value) {
            if (buttonType == value) return
            field = value
            refresh()
        }

    var isLoading: Boolean = false
        set(value) {
            if (isLoading == value) return
            field = value

            if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                post {
                    layoutParams.width = measuredWidth
                    refresh()
                }
            } else {
                refresh()
            }
        }
    var loadingText: CharSequence = ""
    var rightLoader: Boolean = true

    private lateinit var loadingDrawable: AnimatedVectorDrawableCompat
    private var placeholderText: CharSequence = ""
    private val textPaint = Paint()
    private var loaderLeftBound = 0
    private var loaderRightBound = 0
    private var loaderTopBound = 0
    private var loaderBottomBound = 0
    private var mHeight = 0
    private var mWidth = 0
    private var iconDrawable: Drawable? = null
    private var ignoreSetDrawableOnSetText = false
    private var drawablePosition = DrawablePosition.LEFT
    private var primaryColor: Int = 0
    private var secondaryColor: Int = 0
    private var rounded = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWittAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWittAttrs(context, attrs)
    }

    private fun initWittAttrs(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.UnifyButton)
        buttonSize = attributeArray.getInt(R.styleable.UnifyButton_unifyButtonSize, Size.LARGE)
        buttonVariant = attributeArray.getInt(R.styleable.UnifyButton_unifyButtonVariant, Variant.FILLED)
        buttonType = attributeArray.getInt(R.styleable.UnifyButton_unifyButtonType, Type.MAIN)
        isInverse = attributeArray.getBoolean(R.styleable.UnifyButton_unifyButtonInverse, false)
        isInverse = attributeArray.getBoolean(R.styleable.UnifyButton_unifyButtonInverse, false)
        loadingText = attributeArray.getString(R.styleable.UnifyButton_unifyButtonLoadingText) ?: ""
        rightLoader =
            attributeArray.getBoolean(R.styleable.UnifyButton_unifyButtonRightLoader, true)
        isLoading =
            attributeArray.getBoolean(R.styleable.UnifyButton_unifyButtonLoadingState, false)
        primaryColor = attributeArray.getColor(
            R.styleable.UnifyButton_unifyButtonColorPrimary,
            ContextCompat.getColor(context, R.color.Unify_G500)
        )
        secondaryColor = attributeArray.getColor(
            R.styleable.UnifyButton_unifyButtonColorPrimary,
            ContextCompat.getColor(context, R.color.Unify_Y500)
        )
        rounded = attributeArray.getDimension(R.styleable.UnifyButton_unifyButtonRadius, resources.getDimension(R.dimen.button_corner_radius))

        attributeArray.getDrawable(R.styleable.UnifyButton_unifyButtonLeftDrawable)?.let {
            setDrawable(it, DrawablePosition.LEFT)
        }

        attributeArray.getDrawable(R.styleable.UnifyButton_unifyButtonRightDrawable)?.let {
            setDrawable(it, DrawablePosition.RIGHT)
        }

        attributeArray.recycle()
    }

    override fun onFinishInflate() {
        refresh()
        super.onFinishInflate()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)

        if (!isLoading) {
            placeholderText = text ?: ""
        }

        if (!ignoreSetDrawableOnSetText) {
            iconDrawable?.let {
                setDrawable(it, drawablePosition)
            }
        }
    }

    private fun refresh() {
        if (placeholderText.isEmpty()) placeholderText = text
        this.gravity = Gravity.CENTER
        initButtonBackground()
        initButtonSize()
        initButtonPadding()
        initButtonText()
        invalidate()
        requestLayout()
    }

    fun setPrimaryColor(color: Int){
        primaryColor = color
        refresh()
    }

    private fun initButtonPadding() {
        val padding = resources.getDimensionPixelSize(
            if (buttonSize == Size.LARGE || buttonSize == Size.MEDIUM) R.dimen.button_padding_large_and_medium else R.dimen.button_padding_small_and_micro
        )
        setPadding(padding, paddingTop, padding, paddingBottom)
    }

    private fun initButtonText() {
        val font: Typeface? = Typeface.createFromAsset(context.assets, "NunitoSansExtraBold.ttf")
        font?.let {
            typeface = it
        }
        when (buttonSize) {
            Size.LARGE -> {
                setTextSize(COMPLEX_UNIT_SP, 16f)
            }
            Size.MEDIUM -> {
                setTextSize(COMPLEX_UNIT_SP, 14f)
            }
            Size.SMALL, Size.MICRO -> {
                setTextSize(COMPLEX_UNIT_SP, 12f)
            }
        }

        if (isLoading) {
            ignoreSetDrawableOnSetText = true
            text = loadingText
            ignoreSetDrawableOnSetText = false
        }

        this.ellipsize = TextUtils.TruncateAt.END
        this.maxLines = 1
    }

    private fun initButtonSize() {
        height = resources.getDimensionPixelSize(
            when (buttonSize) {
                Size.MEDIUM -> R.dimen.button_height_medium
                Size.SMALL -> R.dimen.button_height_small
                Size.MICRO -> R.dimen.button_height_micro
                else -> R.dimen.button_height_large
            }
        )
    }

    private fun initButtonBackground() {
        val whiteColor = ContextCompat.getColor(context, android.R.color.white)
        val background = when (buttonType) {
            Type.MAIN -> primaryColor
            Type.SECONDARY -> secondaryColor
            else -> primaryColor
        }
        val enableFillDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = rounded
            setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), background)
            if (buttonVariant == Variant.GHOST && buttonType == Type.SECONDARY) {
                setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), secondaryColor)
            }
        }
        val disableFillDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.Unify_NN100))
            cornerRadius = rounded
            setStroke(
                resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                ContextCompat.getColor(context, R.color.Unify_NN100)
            )
        }
        disableFillDrawable.cornerRadius =
            rounded - resources.getDimension(if (buttonSize == Size.MICRO) R.dimen.spacing_lvl1 else R.dimen.unify_space_0)
        enableFillDrawable.cornerRadius =
            rounded - resources.getDimension(if (buttonSize == Size.MICRO) R.dimen.spacing_lvl1 else R.dimen.unify_space_0)

        if (isLoading) loadingDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_loader)!!
        when (buttonVariant) {
            Variant.FILLED -> {
                enableFillDrawable.setColor(background)
                enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), background)
                disableFillDrawable.setColor(ContextCompat.getColor(context, R.color.buttonunify_filled_disabled_color))
                disableFillDrawable.setStroke(
                    resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                    ContextCompat.getColor(context, R.color.buttonunify_filled_disabled_color)
                )
                /**
                 * Set disabled state for filled text color
                 */
                if (isEnabled) {
                    setTextColor(whiteColor)
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.buttonunify_text_disabled_color))
                }

                if (isLoading) {
                    loadingDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_loader_bw)!!
                }
            }
            Variant.GHOST -> {
                enableFillDrawable.setColor(Color.TRANSPARENT)
                disableFillDrawable.setColor(Color.TRANSPARENT)
                disableFillDrawable.setStroke(
                    resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                    ContextCompat.getColor(context, R.color.buttonunify_alternate_disabled_stroke_color)
                )
                if (isInverse) {
                    if (isEnabled) {
                        enableFillDrawable.setStroke(
                            resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                            ContextCompat.getColor(context, R.color.Unify_Static_White)
                        )
                        setTextColor(ContextCompat.getColor(context, R.color.Unify_Static_White))
                    } else {
                        disableFillDrawable.setStroke(
                            resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                            ContextCompat.getColor(context, R.color.buttonunify_alternate_disabled_stroke_color)
                        )
                        setTextColor(ContextCompat.getColor(context, R.color.buttonunify_text_disabled_color))
                    }

                    if (isLoading) {
                        loadingDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_loader_bw)!!
                    }
                } else {

                    /**
                     * Update border color using new color NN300
                     */
                    if (buttonType == Type.ALTERNATE) {
                        enableFillDrawable.setStroke(
                            resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                            ContextCompat.getColor(context, R.color.buttonunify_alternate_stroke_color)
                        )
                        setTextColor(ContextCompat.getColor(context, R.color.buttonunify_alternate_text_color))
                    } else {
                        setTextColor(background)
                    }
                }
                /**
                 * Set disabled state for ghost text color
                 */
                if (!isEnabled) {
                    setTextColor(ContextCompat.getColor(context, R.color.buttonunify_text_disabled_color))
                }
            }
            Variant.TEXT_ONLY -> {
                enableFillDrawable.setColor(ContextCompat.getColor(context, android.R.color.transparent))
                enableFillDrawable.setStroke(
                    resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                    ContextCompat.getColor(context, android.R.color.transparent)
                )
                if (isEnabled) {
                    setTextColor(ContextCompat.getColor(context, R.color.buttonunify_alternate_text_color))
                } else {
                    setTextColor(ContextCompat.getColor(context, R.color.buttonunify_text_disabled_color))
                }
            }
        }
        if (buttonVariant != Variant.TEXT_ONLY && !isEnabled)
            setTextColor(ContextCompat.getColor(context, R.color.buttonunify_text_disabled_color))
        /**
         * Apply disable state and enable state to component background
         */
        val stateListDefaultDrawable = StateListDrawable().apply {
            addState(intArrayOf(-android.R.attr.state_enabled), disableFillDrawable)
            addState(intArrayOf(android.R.attr.state_enabled), enableFillDrawable)
        }
        setBackground(stateListDefaultDrawable)

        if (isLoading) {
            ignoreSetDrawableOnSetText = true
            text = ""
            ignoreSetDrawableOnSetText = false
            loadingDrawable.start()
            val mainHandler = Handler(Looper.getMainLooper())
            loadingDrawable.registerAnimationCallback(object :
                Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    mainHandler.post {
                        loadingDrawable.start()
                    }
                }
            })
            post {
                if (isLoading) {
                    mHeight = measuredHeight
                    mWidth = measuredWidth
                    val layerDrawable =
                        LayerDrawable(arrayOf(stateListDefaultDrawable, loadingDrawable))
                    measureLoadingDrawable()
                    setBackground(layerDrawable)

                    layoutParams.height = mHeight
                }
            }
        } else {
            ignoreSetDrawableOnSetText = true
            text = placeholderText
            ignoreSetDrawableOnSetText = false
            if (::loadingDrawable.isInitialized) {
                loadingDrawable.stop()
                loadingDrawable.clearAnimationCallbacks()
            }
        }
    }

    private fun measureLoadingDrawable() {
        loadingDrawable.alpha = 0
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            loadingDrawable,
            PropertyValuesHolder.ofInt("alpha", 255)
        )
        animator.apply {
            duration = 300
            start()
        }

        textPaint.textSize = textSize

        var additionalX = mHeight - 15.toPx()
        if (text.isEmpty()) additionalX = 0

        var leftOrRight = 1
        if (!rightLoader) leftOrRight = -1

        val textMeasurement = (textPaint.measureText(text.toString()).toInt() / 2 + additionalX) * leftOrRight

        var loaderHeight = 24.toPx()

        when (buttonSize) {
            Size.SMALL, Size.MICRO -> {
                loaderHeight = 16.toPx()
            }
        }

        loaderLeftBound =
            (mWidth / 2 - loaderHeight / 2) + textMeasurement
        loaderTopBound = (mHeight - loaderHeight) / 2
        loaderRightBound =
            ((mWidth / 2 - loaderHeight / 2) + loaderHeight) + textMeasurement
        loaderBottomBound = (mHeight - loaderHeight) / 2 + loaderHeight
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (isEnabled != enabled) initButtonBackground()
        initButtonBackground()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isLoading && ::loadingDrawable.isInitialized) {
            loadingDrawable.setBounds(
                loaderLeftBound,
                loaderTopBound,
                loaderRightBound,
                loaderBottomBound
            )
        }
    }

    override fun onDetachedFromWindow() {
        if (::loadingDrawable.isInitialized) {
            loadingDrawable.stop()
            loadingDrawable.clearAnimationCallbacks()
        }

        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isLoading) {
            refresh()
        }
    }

    fun setDrawable(drawable: Drawable?, position: Int = DrawablePosition.LEFT) {
        drawable?.let {
            val mText = text.trim()
            val size = when (buttonSize) {
                Size.LARGE -> 24.toPx()
                Size.MEDIUM -> 24.toPx()
                Size.SMALL -> 16.toPx()
                Size.MICRO -> 10.toPx()
                else -> 10.toPx()
            }

            val drawableColorFilter = DrawableCompat.getColorFilter(it)
            drawablePosition = position
            iconDrawable = drawable.constantState?.newDrawable()?.mutate()
            iconDrawable?.colorFilter = drawableColorFilter
            val spannableString = when {
                mText.isEmpty() -> SpannableString(" ")
                position == DrawablePosition.LEFT -> SpannableString("  $mText")
                else -> SpannableString("$mText  ")
            }

            iconDrawable?.let {
                it.setBounds(0, 0, size, size)
                val mImageSpan = VerticalImageSpan(it, buttonSize == Size.MICRO)

                if (position == DrawablePosition.LEFT) {
                    spannableString.setSpan(mImageSpan, 0, 1, 0)
                } else {
                    spannableString.setSpan(
                        mImageSpan,
                        spannableString.length - 1,
                        spannableString.length,
                        0
                    )
                }
            }

            ignoreSetDrawableOnSetText = true
            text = spannableString
            ignoreSetDrawableOnSetText = false
        }
    }

    object Size {
        const val LARGE = 1
        const val MEDIUM = 2
        const val SMALL = 3
        const val MICRO = 4
    }

    object Variant {
        const val FILLED = 1
        const val GHOST = 2
        const val TEXT_ONLY = 3
    }

    object Type {
        const val MAIN = 1
        const val SECONDARY = 2
        const val ALTERNATE = 3
    }

    object DrawablePosition {
        const val LEFT = 1
        const val RIGHT = 2
    }

    internal class VerticalImageSpan(drawable: Drawable, var isMicroVariant: Boolean = false) : ImageSpan(drawable) {

        /**
         * update the text line height
         */
        override fun getSize(
            paint: Paint, text: CharSequence, start: Int, end: Int,
            fontMetricsInt: Paint.FontMetricsInt?
        ): Int {
            val drawable = drawable
            val rect = drawable.bounds
            if (fontMetricsInt != null) {
                val fmPaint = paint.fontMetricsInt
                val fontHeight = fmPaint.descent - fmPaint.ascent
                val drHeight = rect.bottom - rect.top
                val centerY = fmPaint.ascent + fontHeight / 2

                fontMetricsInt.ascent = centerY - drHeight / 2
                fontMetricsInt.top = fontMetricsInt.ascent
                fontMetricsInt.bottom = centerY + drHeight / 2
                fontMetricsInt.descent = fontMetricsInt.bottom
            }
            return rect.right
        }

        /**
         * see detail message in android.text.TextLine
         *
         * @param canvas the canvas, can be null if not rendering
         * @param text the text to be draw
         * @param start the text start position
         * @param end the text end position
         * @param x the edge of the replacement closest to the leading margin
         * @param top the top of the line
         * @param y the baseline
         * @param bottom the bottom of the line
         * @param paint the work paint
         */
        override fun draw(
            canvas: Canvas, text: CharSequence, start: Int, end: Int,
            x: Float, top: Int, y: Int, bottom: Int, paint: Paint
        ) {

            val drawable = drawable
            canvas.save()
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.descent - fmPaint.ascent
            var microAdjustment = 0
            if (isMicroVariant) {
                microAdjustment = 1.toPx()
            }
            val centerY = y + fmPaint.descent - fontHeight / 2 - microAdjustment
            val transY = centerY - (drawable.bounds.bottom - drawable.bounds.top) / 2
            canvas.translate(x, transY.toFloat())
            drawable.draw(canvas)
            canvas.restore()
        }
    }
}