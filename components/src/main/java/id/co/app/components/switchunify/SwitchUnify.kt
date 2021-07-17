package id.co.app.components.switchunify

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.utils.setBodyText


/**
 * Created by Lukas Kristianto on 7/15/2021 00:42.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class SwitchUnify(context: Context, attributeSet: AttributeSet) : SwitchCompat(context, attributeSet) {

    /**
     * Variable that used to get reference from custom attribute
     */
    private val boldReference = IntArray(1){ R.attr.bold }

    private var isBold = false

    private var stateList = arrayOf(
        IntArray(1) { android.R.attr.state_enabled },
        IntArray(1) { -android.R.attr.state_enabled }
    )

    private var colorList = IntArray(2)

    private val attributes = intArrayOf(
        android.R.attr.textColor
    )

    init {
        val mTypedArray = context.obtainStyledAttributes(attributeSet, attributes)
        val mTextColor = mTypedArray.getColor(0, ContextCompat.getColor(context, R.color.Unify_N700_96))
        mTypedArray.recycle()

        colorList[0] = mTextColor
        colorList[1] = ContextCompat.getColor(context, R.color.Unify_N700_32)
        isBold = context.obtainStyledAttributes(attributeSet, boldReference).getBoolean(0, false)

        textOff = ""
        textOn = ""
        switchMinWidth = 0

        setThumbResource(R.drawable.unify_switch_thumb_selector)
        setTrackResource(R.drawable.unify_switch_track_selector)
        setBodyText(2, isBold)
        setTextColor(ColorStateList(stateList, colorList))
    }

    /**
     * Set text next to switch to be bold
     * @param boldState true mean text will be bold
     */
    fun setTextBold(boldState: Boolean) {
        isBold = boldState
        setBodyText(2, isBold)
        setTextColor(ColorStateList(stateList, colorList))
    }

    /**
     * Get bold state on switch text
     * @return true mean text is bold
     */
    fun getTextBold(): Boolean {
        return isBold
    }
}