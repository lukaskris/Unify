package id.co.app.components.datetimepicker

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import id.co.app.components.R
import id.co.app.components.bottomsheet.BottomSheetUnify
import id.co.app.components.button.UnifyButton
import java.util.*
import java.util.concurrent.TimeUnit
import id.co.app.components.typography.Typography

class DateTimePickerUnify(
    context: Context,
    private val minDate: Calendar,
    private val defaultDate: Calendar,
    private val maxDate: Calendar,
    private val onDateChangedListener: OnDateChangedListener? = null,
    private val type: Int = TYPE_DATEPICKER
): BottomSheetUnify() {

    private var isShown = false
    lateinit var datePicker: DatePicker
    lateinit var dateTimePicker: DateTimePicker
    lateinit var timePicker: TimePicker
    var datePickerButton: UnifyButton
    var datePickerInfo: Typography
    var hourInterval: Int = 1
    var minuteInterval: Int = 1
    var showDay: Boolean = true
    var showMonth: Boolean = true
    var showYear: Boolean = true

    var datePickerChangeListener: (() -> Unit)? = null
    var dayPickerChangeListener: (() -> Unit)? = null
    var monthPickerChangeListener: (() -> Unit)? = null
    var yearPickerChangeListener: (() -> Unit)? = null
    var minutePickerChangeListener: (() -> Unit)? = null
    var hourPickerChangeListener: (() -> Unit)? = null

    init {
        when(type) {
            TYPE_DATEPICKER -> {
                datePicker = DatePicker(context)
                datePickerButton = datePicker.findViewById(R.id.unify_datepicker_button)
                datePickerInfo = datePicker.findViewById(R.id.unify_datepicker_info)
            }
            TYPE_DATETIMEPICKER -> {
                dateTimePicker = DateTimePicker(context)
                datePickerButton = dateTimePicker.findViewById(R.id.unify_datepicker_button)
                datePickerInfo = dateTimePicker.findViewById(R.id.unify_datepicker_info)
            }
            TYPE_TIMEPICKER -> {
                timePicker = TimePicker(context)
                datePickerButton = timePicker.findViewById(R.id.unify_datepicker_button)
                datePickerInfo = timePicker.findViewById(R.id.unify_datepicker_info)
            }

            else -> {
                datePicker = DatePicker(context)
                datePickerButton = datePicker.findViewById(R.id.unify_datepicker_button)
                datePickerInfo = datePicker.findViewById(R.id.unify_datepicker_info)
            }
        }
    }

    fun getDate(): Calendar {
        if (::datePicker.isInitialized) {
            return datePicker.getDate()
        }
        if (::dateTimePicker.isInitialized) {
            return dateTimePicker.getDate()
        }
        if (::timePicker.isInitialized) {
            return timePicker.getDate()
        }

        return datePicker.getDate()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        initDateTimePicker()
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        initDateTimePicker()
        return super.show(transaction, tag)
    }

    fun setInfo(info: CharSequence){
        datePickerInfo.text = info
    }

    fun getInfo(): CharSequence{
        return datePickerInfo.text.toString()
    }

    fun setInfoVisible(state: Boolean){
        datePickerInfo.visibility = if(state) View.VISIBLE else View.GONE
    }

    fun convertDateToIndex(cal: Calendar): Int {
        return TimeUnit.DAYS.convert(
            cal.timeInMillis - minDate.timeInMillis,
            TimeUnit.MILLISECONDS
        ).toInt() + 3
    }

    fun convertHourToIndex(h: Int): Int {
        return h / hourInterval
    }

    fun convertMinuteToIndex(m: Int): Int {
        return m / minuteInterval
    }

    private fun initDateTimePicker() {
        if (!isShown) {
            if (::datePicker.isInitialized) {
                datePicker.apply {
                    dayPickerChangeListener = this@DateTimePickerUnify.dayPickerChangeListener
                    monthPickerChangeListener = this@DateTimePickerUnify.monthPickerChangeListener
                    yearPickerChangeListener = this@DateTimePickerUnify.yearPickerChangeListener
                    init(defaultDate, minDate, maxDate, onDateChangedListener, this@DateTimePickerUnify.showDay, this@DateTimePickerUnify.showMonth, this@DateTimePickerUnify.showYear)
                }
                setChild(datePicker)
            }
            if (::dateTimePicker.isInitialized) {
                dateTimePicker.apply {
                    hourInterval = this@DateTimePickerUnify.hourInterval
                    minuteInterval = this@DateTimePickerUnify.minuteInterval
                    datePickerChangeListener = this@DateTimePickerUnify.datePickerChangeListener
                    hourPickerChangeListener = this@DateTimePickerUnify.hourPickerChangeListener
                    minutePickerChangeListener = this@DateTimePickerUnify.minutePickerChangeListener
                    init(defaultDate, minDate, maxDate, onDateChangedListener)
                }
                setChild(dateTimePicker)
            }
            if (::timePicker.isInitialized) {
                timePicker.apply {
                    hourInterval = this@DateTimePickerUnify.hourInterval
                    minuteInterval = this@DateTimePickerUnify.minuteInterval
                    hourPickerChangeListener = this@DateTimePickerUnify.hourPickerChangeListener
                    minutePickerChangeListener = this@DateTimePickerUnify.minutePickerChangeListener
                    init(defaultDate, minDate, maxDate, onDateChangedListener)
                }
                setChild(timePicker)
            }

            isShown = true
        }
    }


    companion object {
        const val TYPE_DATEPICKER = 0
        const val TYPE_DATETIMEPICKER = 1
        const val TYPE_TIMEPICKER = 2
    }
}