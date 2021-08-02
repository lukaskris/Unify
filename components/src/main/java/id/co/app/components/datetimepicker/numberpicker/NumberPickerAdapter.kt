package id.co.app.components.datetimepicker.numberpicker

abstract class NumberPickerAdapter {

    abstract fun getValue(position: Int): String

    abstract fun getPosition(vale: String): Int

    abstract fun getTextWithMaximumLength(): String

    open fun getSize(): Int = -1

    var picker: NumberPicker? = null

    fun notifyDataSetChanged() {
        picker?.invalidate()
        picker?.requestLayout()
    }
}