package id.co.app.components.button

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import id.co.app.components.R


/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class UnifyImageButton : AppCompatImageButton {

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
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWittAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWittAttrs(context, attrs)
    }

    private fun initWittAttrs(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.UnifyImageButton)
        buttonVariant = attributeArray.getInt(R.styleable.UnifyImageButton_unifyImageButtonVariant, Variant.FILLED)
        buttonType = attributeArray.getInt(R.styleable.UnifyImageButton_unifyImageButtonType, Type.MAIN)
        isInverse = attributeArray.getBoolean(R.styleable.UnifyImageButton_unifyImageButtonInvers, false)
        attributeArray.recycle()
    }

    override fun onFinishInflate() {
        refresh()
        super.onFinishInflate()
    }

    private fun refresh() {
        initButtonBackground()
        initButtonPadding()
        invalidate()
        requestLayout()
    }

    private fun initButtonPadding() {
        val padding = resources.getDimensionPixelSize(R.dimen.button_padding_small_and_micro)
        setPadding(padding, paddingTop, padding, paddingBottom)
    }

    private fun initButtonBackground() {
        val whiteColor = ContextCompat.getColor(context, android.R.color.white)
        val background = ContextCompat.getColor(context, when(buttonType){
            Type.MAIN -> R.color.Green_G500
            Type.TRANSACTION -> R.color.Yellow_Y500
            else -> R.color.Neutral_N0
        })
        val enableFillDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.Neutral_N0))
            cornerRadius = resources.getDimension(R.dimen.button_corner_radius)
            setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), background)
        }
        val disableFillDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.Neutral_N75))
            cornerRadius = resources.getDimension(R.dimen.button_corner_radius)
            setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), ContextCompat.getColor(context, R.color.Neutral_N75))
        }
        disableFillDrawable.cornerRadius =
            resources.getDimension(R.dimen.button_corner_radius)
        enableFillDrawable.cornerRadius =
            resources.getDimension(R.dimen.button_corner_radius)
        when(buttonVariant){
            Variant.FILLED -> {
                enableFillDrawable.setColor(background)
                enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), background)
                /**
                 * Set disabled state for filled text color
                 */
                if (isEnabled) {
                    setColorFilter(whiteColor)
                } else {
                    setColorFilter(ContextCompat.getColor(context, R.color.Neutral_N700_44))
                }
            }
            Variant.GHOST -> {
                disableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                    ContextCompat.getColor(context, R.color.Neutral_N100))
                if (isInverse){
                    enableFillDrawable.setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width), whiteColor)
                    disableFillDrawable.setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    setColorFilter(ContextCompat.getColor(context, R.color.Neutral_N0))
                } else {
                    enableFillDrawable.setColor(whiteColor)
                    disableFillDrawable.setColor(whiteColor)
                    setColorFilter(background)
                }
                /**
                 * Set disabled state for ghost text color
                 */
                if (!isEnabled) {
                    setColorFilter(ContextCompat.getColor(context, R.color.Neutral_N700_44))
                }
            }
            Variant.IMAGE_ONLY -> {
                enableFillDrawable.setColor(ContextCompat.getColor(context, android.R.color.transparent))
                enableFillDrawable.setStroke(resources.getDimensionPixelSize(R.dimen.button_stroke_width),
                    ContextCompat.getColor(context, android.R.color.transparent))
                setColorFilter(ContextCompat.getColor(context, R.color.Neutral_N700_44))
            }
        }
        if (buttonVariant != Variant.IMAGE_ONLY && !isEnabled)
            setColorFilter(ContextCompat.getColor(context, R.color.Neutral_N700_32))
        /**
         * Apply disable state and enable state to component background
         */
        val stateListDefaultDrawable = StateListDrawable().apply {
            addState(intArrayOf(-android.R.attr.state_enabled), disableFillDrawable)
            addState(intArrayOf(android.R.attr.state_enabled),enableFillDrawable)
        }
        setBackground(stateListDefaultDrawable)
    }
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (isEnabled != enabled) initButtonBackground()
        initButtonBackground()
    }

    object Variant {
        const val FILLED = 1
        const val GHOST = 2
        const val IMAGE_ONLY = 3
    }

    object Type {
        const val MAIN = 1
        const val TRANSACTION = 2
    }
}