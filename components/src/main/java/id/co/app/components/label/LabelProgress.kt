package id.co.app.components.label

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.typography.Typography


/**
 * Created by Lukas Kristianto on 7/15/2021 00:12.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class LabelProgress : FrameLayout {

    var tvLabelProgress: Typography? = null
    var progressBar: ProgressBar? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        View.inflate(context, R.layout.label_layout_progress, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelProgress, 0, 0)
        tvLabelProgress = findViewById(R.id.txt_label_progressbar)
        progressBar = findViewById(R.id.progressbar_label)

        try {
            val labelText = typedArray.getString(R.styleable.LabelProgress_labelProgressTitle)
            val progressNumber = typedArray.getInt(R.styleable.LabelProgress_labelProgress, 0)
            val labelType = typedArray.getInteger(R.styleable.LabelProgress_labelColor,
                Label.GENERAL_DARK_GREY
            )
            val typographyType = typedArray.getInteger(R.styleable.LabelProgress_labelTypographyType, 1)
            setTypographyType(typographyType)
            setLabelType(labelType)
            setLabel(labelText, progressNumber)
        } finally {
            typedArray.recycle()
        }
    }

    fun setLabel(label: String?, progress: Int) {
        tvLabelProgress!!.text = label
        progressBar!!.progress = progress
    }

    fun setLabelType(labelType: Int) {

        when (labelType) {
            Label.GENERAL_LIGHT_RED -> {
                setStyle(
                    R.color.labelunify_light_red_background,
                    R.color.labelunify_light_red_text
                )
            }
            Label.GENERAL_LIGHT_GREEN -> {
                setStyle(
                    R.color.labelunify_light_green_background,
                    R.color.labelunify_light_green_text
                )
            }
            Label.GENERAL_LIGHT_BLUE -> {
                setStyle(
                    R.color.labelunify_light_blue_background,
                    R.color.labelunify_light_blue_text
                )
            }
            Label.GENERAL_LIGHT_GREY -> {
                setStyle(
                    R.color.labelunify_light_grey_background,
                    R.color.labelunify_light_grey_text
                )
            }
            Label.GENERAL_LIGHT_ORANGE -> {
                setStyle(
                    R.color.labelunify_light_orange_background,
                    R.color.labelunify_light_orange_text
                )
            }
        }
    }

    private fun setTypographyType(typographyType: Int){
        tvLabelProgress?.setType(typographyType)
    }

    private fun setStyle(backgroundColor: Int, textColor: Int) {
        tvLabelProgress?.setTextColor(ContextCompat.getColor(context, textColor))

        val layerDrawable = ContextCompat.getDrawable(context, R.drawable.label_bg_progressbar) as LayerDrawable
        val customDrawable = ContextCompat.getDrawable(context, R.drawable.label_bg_custom_progress) as GradientDrawable

        customDrawable.setColor(ContextCompat.getColor(context, backgroundColor))

        val scaleDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress) as ScaleDrawable
        scaleDrawable.drawable = customDrawable

        progressBar?.progressDrawable = scaleDrawable
    }

    private fun getUnitDp(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

}
