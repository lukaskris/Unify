package id.co.app.components.radio

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import id.co.app.components.R
import id.co.app.components.utils.setBodyText

/**
 * Created by Lukas Kristianto on 7/15/2021 23.20.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class RadioButtonUnify(context: Context, attributeSet: AttributeSet): androidx.appcompat.widget.AppCompatRadioButton(context,attributeSet) {

    /**
     * Variable that used to get reference from custom attribute
     */
    val boldReference = IntArray(1) {R.attr.bold}

    private var isBold = false

    private var stateList = arrayOf(
        IntArray(1) { android.R.attr.state_enabled },
        IntArray(1) { -android.R.attr.state_enabled }
    )

    private var colorList = IntArray(2)

    val attributes = intArrayOf(
        android.R.attr.textColor
    )

    init {
        var mTypedArray = context.obtainStyledAttributes(attributeSet, attributes)
        var mTextColor = mTypedArray.getColor(0, ContextCompat.getColor(context, R.color.Unify_N700_96))
        mTypedArray.recycle()

        colorList[0] = mTextColor
        colorList[1] = ContextCompat.getColor(context, R.color.Unify_N700_32)
        isBold = context.obtainStyledAttributes(attributeSet, boldReference).getBoolean(0, false)

        var isRTL = attributeSet.getAttributeValue(
            "http://schemas.android.com/apk/res/android",
            "layoutDirection"
        )?.toInt() == View.LAYOUT_DIRECTION_RTL

        if (!isRTL) {
            setPadding(
                resources.getDimension(R.dimen.selection_control_padding).toInt(),
                0,
                0,
                0
            )
        } else {
            setPadding(
                0,
                0,
                resources.getDimension(R.dimen.selection_control_padding).toInt(),
                0
            )
        }

        configState(true)
        setBodyText(2, isBold)
        setTextColor(ColorStateList(stateList, colorList))

        // setBackgroundColor is used to invoke "re-render" on pre-lollipop to refresh the padding size
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun setChecked(checked: Boolean) {
        var isNeedRefresh = isChecked != checked
        super.setChecked(checked)
        if(isNeedRefresh) {
            configState(false)
        }
    }
    
    /**
     * Set text next to radio button to be bold
     * @param boldState true mean text will be bold
     */
    fun setTextBold(boldState: Boolean) {
        isBold = boldState
        setBodyText(2, isBold)
        setTextColor(ColorStateList(stateList, colorList))
    }

    /**
     * Get bold state on radio button text
     * @return true mean text is bold
     */
    fun getTextBold(): Boolean {
        return isBold
    }

    /**
     * skip/stop current running animation
     */
    fun skipAnimation() {
        (CompoundButtonCompat.getButtonDrawable(this) as AnimatedVectorDrawableCompat).stop()
    }

    private fun configState(firstState: Boolean) {
        val radioButtonCheck = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_radiobutton_checked)
        val radioButtonUncheck = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_radiobutton_unchecked)

        if (isEnabled) {
            val drawable = if (isChecked) radioButtonCheck else radioButtonUncheck
            setButtonDrawable(drawable)
            drawable?.start()

            if (firstState) {
                drawable?.stop()
            }
        } else {
            setButtonDrawable(R.drawable.unify_radiobutton_bg_disabled)
        }

    }
}