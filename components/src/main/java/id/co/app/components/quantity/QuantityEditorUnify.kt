package id.co.app.components.quantity

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.icon.IconUnify
import id.co.app.components.utils.setBodyText
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

    private lateinit var textChangeListener: (newValue: Int, oldValue: Int, isOver: Int?) -> Unit

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

    private var newValue = 0
    private var oldValue = 0

    /**
     * Step value for each increment or decrement
     */
    var stepValue = 1

    /**
     * Reference attribute variable
     */
    private var minValueReference = IntArray(1) { R.attr.qty_min_value }
    private var maxValueReference = IntArray(1) { R.attr.qty_max_value }
    private var defaultValueReference = IntArray(1) { R.attr.qty_default_value }
    private var stepValueReference = IntArray(1) { R.attr.unify_qty_step_value }

    init {
        View.inflate(context, R.layout.quantity_editor_layout, this)

        var defaultValue =
            context.obtainStyledAttributes(attributeSet, defaultValueReference).getInt(0, 0)
        minValue = context.obtainStyledAttributes(attributeSet, minValueReference).getInt(0, 0)
        maxValue = context.obtainStyledAttributes(attributeSet, maxValueReference).getInt(0, 100)
        stepValue = context.obtainStyledAttributes(attributeSet, stepValueReference).getInt(0, 1)

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
            var tempValue = getEditTextVal()

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

            if (::textChangeListener.isInitialized) textChangeListener(newValue, oldValue, null)

            addListener()
        }

        subtractButton.setOnClickListener {
            editText.clearFocus()
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

            if (::textChangeListener.isInitialized) textChangeListener(newValue, oldValue, null)

            subtractListener()
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            val isOver: Int?
            val tempValue = getEditTextVal()

            if (!hasFocus) {
                isOver = checkOver(tempValue)

                if (autoHideKeyboard) {
                    val input =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    input.hideSoftInputFromWindow(editText.windowToken, 0)
                }

                if (::textChangeListener.isInitialized && tempValue != newValue) textChangeListener(
                    newValue,
                    oldValue,
                    isOver
                )
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                editText.removeTextChangedListener(this)

                editText.setText(applyFormat(editText.text))
                editText.setSelection(editText.text.length)

                editText.addTextChangedListener(this)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
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
        val isOver = checkOver(value)

        if (::textChangeListener.isInitialized) textChangeListener(newValue, oldValue, isOver)
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
            Log.e("Unify", "QuantityEditorUnify Int size over")
            formatter.format(maxValue)
        }

        return formattedSource.replace(",", ".")
    }

    private fun getEditTextVal(): Int {
        val tempStorageText = editText.text.toString().replace(".", "")

        return if(tempStorageText.isNullOrEmpty()){
            newValue
        }else{
            tempStorageText.toInt()
        }
    }

    companion object {
        const val OVER_MAX = 0
        const val LOWER_MIN = 1
    }
}