package id.co.app.components.quantity

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.icon.IconUnify
import id.co.app.components.utils.setBodyText
import timber.log.Timber
import java.text.DecimalFormat


/**
 * Created by Lukas Kristianto on 7/15/2021 23.20.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class QuantityEditorUnify(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    /**
     * View variable
     */
    var addButton = IconUnify(context)
    var subtractButton = IconUnify(context)
    var editText = EditText(context)

    /**
     * Stored listener
     */
    private var addListener: () -> Unit = {}
    private var subtractListener: () -> Unit = {}

    private var textChangeListener: ((newValue: Int, oldValue: Int, isOver: Int?) -> Unit)? = null

    /**
     * Minimum value boundary
     */
    var minValue = 0

    /**
     * Maximum value boundary
     */
    var maxValue = 100

    /**
     * Auto hide keyboard when editText loses focus (default is false)
     */
    var autoHideKeyboard = false

    private var isPressedButton = false

    private var newValue = 0
    private var oldValue = 0

    /**
     * Step value for each increment or decrement
     */
    var stepValue = 1

    init {
        View.inflate(context, R.layout.quantity_editor_layout, this)
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.QuantityEditorUnify)
        var defaultValue = attributeArray.getInt(R.styleable.QuantityEditorUnify_editor_qty_default_value, 0)
        minValue = attributeArray.getInt(R.styleable.QuantityEditorUnify_editor_qty_min_value,0)
        maxValue = attributeArray.getInt(R.styleable.QuantityEditorUnify_editor_qty_max_value, 100)
        stepValue = attributeArray.getInt(R.styleable.QuantityEditorUnify_editor_qty_step_value, 1)

        addButton = findViewById(R.id.quantity_editor_add)
        subtractButton = findViewById(R.id.quantity_editor_substract)

        editText = findViewById(R.id.quantity_editor_qty)
        editText.setBodyText(3, false)
        editText.setTextColor(ContextCompat.getColor(context, R.color.Unify_N700_96))

        if (defaultValue >= maxValue) {
            defaultValue = maxValue
            addButton.isEnabled = false
        }
        if (defaultValue <= minValue) {
            defaultValue = minValue
            subtractButton.isEnabled = false
        }

        editText.setText(applyFormat(defaultValue.toString()))
        editText.setSelection(editText.text.length)
        newValue = defaultValue

        addButton.setOnClickListener {
            editText.clearFocus()

            isPressedButton = true

            var tempValue = getEditTextVal()
            var o1 = oldValue
            var n1 = newValue
            tempValue += stepValue
            if (tempValue > maxValue) {
                tempValue = maxValue
            }

            editText.setText(applyFormat(tempValue.toString()))

            oldValue = newValue
            newValue = tempValue

            if (tempValue >= maxValue) {
                addButton.isEnabled = false
            }

            subtractButton.isEnabled = true

            if (textChangeListener != null && newValue != oldValue) textChangeListener?.invoke(newValue, oldValue, null)

            addListener()
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //Clear focus here from edittext
                editText.clearFocus()
            }
            false
        }

        subtractButton.setOnClickListener {
            editText.clearFocus()
            isPressedButton = true
            var tempValue = getEditTextVal()

            tempValue -= stepValue
            if (tempValue < minValue) {
                tempValue = minValue
            }

            editText.setText(applyFormat(tempValue.toString()))

            oldValue = newValue
            newValue = tempValue

            if (tempValue <= minValue) {
                subtractButton.isEnabled = false
            }

            addButton.isEnabled = true
            if (textChangeListener != null && newValue != oldValue) textChangeListener?.invoke(newValue, oldValue, null)

            subtractListener()
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(!isPressedButton) {
                    val tempValue = getEditTextVal()
                    editText.removeTextChangedListener(this)

                    editText.setText(applyFormat(editText.text))
                    editText.setSelection(editText.text.length)

                    oldValue = newValue
                    newValue = tempValue

                    if (tempValue >= maxValue) {
                        addButton.isEnabled = false
                    }

                    subtractButton.isEnabled = true

                    sendListenerTextChange()

                    editText.addTextChangedListener(this)
                }
                isPressedButton = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        attributeArray.recycle()
    }

    /**
     * Get value from quantity editor
     * @return int value from quantity editor
     */
    fun getValue(): Int {
        return getEditTextVal()
    }

    /**
     * Set click callback to add (+) button
     * @param listener Unit function callback for add button when clicked
     */
    fun setAddClickListener(listener: () -> Unit) {
        addListener = listener
    }

    /**
     * Set click callback to subtract (-) button
     * @param listener Unit function callback for subtract button when clicked
     */
    fun setSubstractListener(listener: () -> Unit) {
        subtractListener = listener
    }

    /**
     * Set Unit function callback for each value changed. Callback retrieve 3 param for new value, old value and limit status
     * @param listener unit function that will be call each value changed
     */
    fun setValueChangedListener(listener: (newValue: Int, oldValue: Int, isOver: Int?) -> Unit) {
        textChangeListener = listener
    }

    /**
     * Set Quantity Editor value
     * @param value new quantity editor value
     */
    fun setValue(value: Int) {
        clear()
        checkOver(value)
    }

    /**
     * Set Id Quantity Editor and also clear text change
     * @param id new ID
     */
    fun clear(){
        newValue = 0
        oldValue = 0
        textChangeListener = null
    }

    /**
     * Internal function for check if value over or lower than limit
     */
    private fun checkOver(targetCheckValue: Int): Int? {
        oldValue = newValue

        return if (targetCheckValue >= maxValue) {
            editText.setText(applyFormat(maxValue.toString()))
            editText.setSelection(editText.text.length)
            newValue = maxValue
            addButton.isEnabled = false
            subtractButton.isEnabled = newValue > minValue

            // return
            OVER_MAX
        } else if (targetCheckValue <= minValue) {
            editText.setText(applyFormat(minValue.toString()))
            editText.setSelection(editText.text.length)
            newValue = minValue
            addButton.isEnabled = true
            subtractButton.isEnabled = newValue < minValue

            // return
            LOWER_MIN
        } else {
            addButton.isEnabled = true
            subtractButton.isEnabled = true
            editText.setText(applyFormat(targetCheckValue.toString()))
            editText.setSelection(editText.text.length)
            newValue = targetCheckValue

            // return
            null
        }
    }

    private fun sendListenerTextChange(){
        if (newValue != oldValue) {
            textChangeListener?.invoke(newValue, oldValue, null)
        }
    }

    /**
     * Apply quantity editor format to param
     * @param source target source to apply the format
     */
    fun applyFormat(source: CharSequence?): String {
        if (source.isNullOrEmpty()) return ""

        val cleanSource = source.toString().replace(".", "")

        val formatter = DecimalFormat("###,###")
        val formattedSource: String = try {
            formatter.format(cleanSource.toInt())
        } catch (e: Exception) {
            Timber.tag("Unify").e("QuantityEditorUnify Int size over")
            formatter.format(maxValue)
        }

        return formattedSource.replace(",", ".")
    }

    private fun getEditTextVal(): Int {
        val tempStorageText = editText.text.toString().replace(".", "")

        return if (tempStorageText.isEmpty()) {
            newValue
        } else {
            tempStorageText.toInt()
        }
    }

    companion object {
        const val OVER_MAX = 0
        const val LOWER_MIN = 1
    }
}