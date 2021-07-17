package id.co.app.components.checkbox

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

class CheckboxUnify(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatCheckBox(context, attributeSet) {

    var checkboxType: Int = TYPE_DEFAULT
    set(value) {
        field = value

        if(field == TYPE_DEFAULT) {
            checkboxUncheck = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_checkbox_unchecked)
            indeterminateUncheck = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_indeterminate_unchecked)
        } else {
            checkboxUncheck = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_checkbox_unchecked_white)
            indeterminateUncheck = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_indeterminate_unchecked_white)
        }

        configState(true)
    }

    /**
     * Variable that used to get reference from custom attribute
     */
    val indeterminateReference = IntArray(1) { R.attr.indeterminate }
    val boldReference = IntArray(1) { R.attr.bold }
    val typeReference = IntArray(1) { R.attr.unify_checkbox_type }

    private var indeterminateUncheck: AnimatedVectorDrawableCompat? = null
    private var indeterminateCheck: AnimatedVectorDrawableCompat? = null

    private var checkboxUncheck: AnimatedVectorDrawableCompat? = null
    private var checkboxCheck: AnimatedVectorDrawableCompat? = null

    private var isIndeterminate = false
    private var isBold = false
    private var isInitiate = false

    private var stateList = arrayOf(
        IntArray(1) { android.R.attr.state_enabled },
        IntArray(1) { -android.R.attr.state_enabled }
    )

    private var colorList = IntArray(2)

    val attributes = intArrayOf(
        android.R.attr.textColor
    )

    init {
        val mTypedArray = context.obtainStyledAttributes(attributeSet, attributes)
        val mTextColor = mTypedArray.getColor(0, ContextCompat.getColor(context, R.color.Unify_N700_96))
        mTypedArray.recycle()

        colorList[0] = mTextColor
        colorList[1] = ContextCompat.getColor(context, R.color.Unify_N700_32)
        isIndeterminate = context.obtainStyledAttributes(attributeSet, indeterminateReference)
            .getBoolean(0, false)
        isBold = context.obtainStyledAttributes(attributeSet, boldReference).getBoolean(0, false)
        checkboxType = context.obtainStyledAttributes(attributeSet, typeReference).getInt(0, TYPE_DEFAULT)

        val isRTL = attributeSet.getAttributeValue(
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

        /**
         * setBackgroundColor is used to invoke "re-render" on pre-lollipop to refresh the padding size
         */
        setBackgroundColor(Color.TRANSPARENT)

        isInitiate = true
    }

    /**
     * Change checkbox to indeterminate version, true mean indeterminate
     * @param indeterminateState true means checkbox will convert to indeterminate version
     */
    fun setIndeterminate(indeterminateState: Boolean) {
        isIndeterminate = indeterminateState
        refreshDrawableState()
        configState(false)
    }

    /**
     * Get current version from checkbox, true mean indeterminate
     * @return Boolean, true mean checkbox in indeterminate version
     */
    fun getIndeterminate(): Boolean {
        return isIndeterminate
    }

    /**
     * Set text next to checkbox to be bold
     * @param boldState true mean text will be bold
     */
    fun setTextBold(boldState: Boolean) {
        isBold = boldState
        setBodyText(2, isBold)
        setTextColor(ColorStateList(stateList, colorList))
    }

    /**
     * Get bold state on checkbox text
     * @return true mean text is bold
     */
    fun getTextBold(): Boolean {
        return isBold
    }

    /**
     * skip/stop current running animation
     */
    fun skipAnimation() {
        if(!isEnabled) return
        (CompoundButtonCompat.getButtonDrawable(this) as AnimatedVectorDrawableCompat).stop()
    }

    /**
     * Add new custom drawable state to determine which drawable will be draw from background xml
     */
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val newState = super.onCreateDrawableState(extraSpace + 1)

        if (isIndeterminate) {
            View.mergeDrawableStates(newState, indeterminateReference)
        }

        return newState
    }

    /**
     * Override to maintain animation using configState
     */
    override fun setChecked(checked: Boolean) {
        val isNeedRefresh = isChecked != checked
        super.setChecked(checked)
        if (isNeedRefresh) {
            configState(false)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        if(isInitiate){
            configState(true)
        }
    }

    /**
     * Maintain transition animation state
     */
    private fun configState(firstState: Boolean) {
        if(indeterminateCheck == null) {
            indeterminateCheck =
                AnimatedVectorDrawableCompat.create(context, R.drawable.unify_indeterminate_checked)
        }
        if(checkboxCheck == null) {
            checkboxCheck =
                AnimatedVectorDrawableCompat.create(context, R.drawable.unify_checkbox_checked)
        }

        if (isEnabled) {
            val drawable: AnimatedVectorDrawableCompat?

            drawable = if (isIndeterminate) {
                if (isChecked) indeterminateCheck else indeterminateUncheck
            } else {
                if (isChecked) checkboxCheck else checkboxUncheck
            }

            buttonDrawable = drawable
            drawable?.start()

            if (firstState) {
                drawable?.stop()
            }
        } else {
            setButtonDrawable(R.drawable.unify_checkbox_bg_disabled)
        }
    }

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_WHITE = 1
    }
}