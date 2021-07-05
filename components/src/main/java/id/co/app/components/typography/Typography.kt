package id.co.app.components.typography

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import id.co.app.components.R

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class Typography : AppCompatTextView {
    var fontType: Int = 0
    var weightType: Int = REGULAR
    private var extraSpace: Int = 0

    constructor(context: Context) : super(context) {
        setTextColor(0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(context, attrs)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs)

    }


    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.Typography)
            val xmlTextColor = context.obtainStyledAttributes(attrs, IntArray(1){android.R.attr.textColor})

            fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0)
            weightType = attributeArray.getInteger(R.styleable.Typography_typographyWeight, REGULAR)

            configFontSize()
            configFontWeight()

            // initialize text color
            try {
                // if xml textColor using single color
                // use getInt instead of get color to differentiate between color / color state list
                setTextColor(xmlTextColor.getInt(0,0))
            }catch (e: Exception){
                when(e){
                    is NumberFormatException, is UnsupportedOperationException -> {
                        // if xml textColor using color state list resource
                        setTextColor(xmlTextColor.getColorStateList(0))
                    }
                    else -> {
                        // unknown error will set the default color
                        setTextColor(0)
                    }
                }
            }

            xmlTextColor.recycle()
            attributeArray.recycle()
        }
    }

    fun setType(type: Int) {
        this.fontType = type
        configFontSize()
        configFontWeight()
        requestLayout()
    }

    fun setWeight(weight: Int) {
        this.weightType = weight
        configFontWeight()
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (textSize <= resources.getDimension(R.dimen.heading_1)) {
            val fm = paint.fontMetricsInt
            val leading = (textSize / resources.displayMetrics.scaledDensity + extraSpace).toInt().toPx()
            val addLineSpace = leading - (fm.descent - fm.ascent)
            this.setLineSpacing(addLineSpace, 1f)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun setTextColor(color: Int) {
        val stateList = arrayOf(
            IntArray(1) { android.R.attr.state_enabled },
            IntArray(1) { -android.R.attr.state_enabled }
        )
        val colorList = IntArray(2)

        colorList[0] = if(color != 0){
            color
        } else {
            ContextCompat.getColor(context, R.color.Unify_N700_96)
        }

        colorList[1] = ContextCompat.getColor(context, R.color.Unify_N700_32)

        val colorStateList = ColorStateList(stateList, colorList)

        super.setTextColor(colorStateList)
    }

    private fun Int.toPx(): Float = this * Resources.getSystem().displayMetrics.density

    private fun configFontSize() {
        val fontSize = when (fontType) {
            HEADING_1 -> resources.getDimension(R.dimen.heading_1)
            HEADING_2 -> resources.getDimension(R.dimen.heading_2)
            HEADING_3 -> resources.getDimension(R.dimen.heading_3)
            HEADING_4 -> resources.getDimension(R.dimen.heading_4)
            HEADING_5 -> resources.getDimension(R.dimen.heading_5)
            HEADING_6 -> resources.getDimension(R.dimen.heading_6)
            BODY_1 -> resources.getDimension(R.dimen.body_1)
            BODY_2 -> resources.getDimension(R.dimen.body_2)
            BODY_3 -> resources.getDimension(R.dimen.body_3)
            SMALL -> resources.getDimension(R.dimen.small)
            else -> textSize
        }

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)

        extraSpace = when (fontType) {
            in HEADING_1..HEADING_4 -> 6
            in HEADING_5..HEADING_6 -> 4
            in BODY_1..BODY_3 -> 6
            else -> 6
        }

        if(fontType == HEADING_1) this.includeFontPadding = false
        if(minLines < 1) {
            this.minHeight = (textSize + extraSpace.toPx()).toInt()
        }

        val kerningValueNewVer = if (fontType == HEADING_1) {
            -0.013f
        } else if (fontType == HEADING_2 || fontType == HEADING_3) {
            -0.01f
        } else {
            0f
        }

        this.letterSpacing = kerningValueNewVer
    }

    private fun configFontWeight() {
        when (fontType) {
            BODY_1,
            BODY_2,
            BODY_3,
            SMALL -> {
                /**
                 * REGULAR / BOLD remove native modifier because the font asset itself already bold
                 */
                if (weightType == REGULAR) {
                    this.typeface = getTypeface(context, "RobotoRegular.ttf")
                } else {
                    this.typeface = getTypeface(context, "RobotoBold.ttf")
                }
            }
            else -> {
                this.typeface = getTypeface(context, "NunitoSansExtraBold.ttf")
            }
        }
    }

    companion object {
        const val HEADING_1 = 1
        const val HEADING_2 = 2
        const val HEADING_3 = 3
        const val HEADING_4 = 4
        const val HEADING_5 = 5
        const val HEADING_6 = 6
        const val BODY_1 = 7
        const val BODY_2 = 8
        const val BODY_3 = 9
        const val SMALL = 10

        const val REGULAR = 1
        const val BOLD = 2
    }
}