package id.co.app.components.datetimepicker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import id.co.app.components.R
import id.co.app.components.datetimepicker.picker.PickerUnify
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class DateTimePicker : FrameLayout {
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    )
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    )
            : super(context, attrs, defStyleAttr, defStyleRes)

    private var locale: Locale = LocaleUtils.getIDLocale()
    private val view: View by lazy { View.inflate(context,
        R.layout.datetime_picker_layout, this) }
    lateinit var selectedDate: Calendar

    val dateSet: MutableList<String> = mutableListOf()

    val hourSet: MutableList<String> = mutableListOf()

    val minuteSet: MutableList<String> = mutableListOf()

    val datePicker: PickerUnify
    val hourPicker: PickerUnify
    val minutePicker: PickerUnify
    val sdf = SimpleDateFormat("EEE, dd MMM yyyy", locale)
    var hourInterval: Int = 1
    var minuteInterval: Int = 1

    var datePickerChangeListener: (() -> Unit)? = null
    var minutePickerChangeListener: (() -> Unit)? = null
    var hourPickerChangeListener: (() -> Unit)? = null

    private lateinit var defaultDate: Calendar
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private var onDateChangedListener: OnDateChangedListener? = null

    init {
        datePicker = view.findViewById(R.id.unify_date_picker)
        hourPicker = view.findViewById(R.id.unify_hour_picker)
        minutePicker = view.findViewById(R.id.unify_minute_picker)
    }

    fun init(
        defaultDate: Calendar,
        minDate: Calendar,
        maxDate: Calendar,
        onDateChangedListener: OnDateChangedListener?
    ) {
        val calendar = defaultDate

        selectedDate = when {
            calendar.timeInMillis > maxDate.timeInMillis -> maxDate
            calendar.timeInMillis < minDate.timeInMillis -> minDate
            else -> calendar
        }

        selectedDate.set(
            Calendar.HOUR_OF_DAY,
            ceil(selectedDate.get(Calendar.HOUR_OF_DAY).toDouble() / hourInterval).toInt() * hourInterval
        )
        selectedDate.set(
            Calendar.MINUTE,
            ceil(selectedDate.get(Calendar.MINUTE).toDouble() / minuteInterval).toInt() * minuteInterval
        )

        this.onDateChangedListener = onDateChangedListener
        this.defaultDate = defaultDate
        this.minDate = minDate
        this.maxDate = maxDate

        initDate()
        initHour()
        initMinute()
    }

    private fun initDate() {
        var selectedDateOnly = selectedDate.clone() as Calendar
        selectedDateOnly.apply {
            set(Calendar.HOUR, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        var minDateOnly = minDate.clone() as Calendar
        minDateOnly.apply {
            set(Calendar.HOUR, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val diffDateCurrent = TimeUnit.MILLISECONDS.toDays(
            selectedDateOnly.timeInMillis - minDateOnly.timeInMillis
        )

        val diffDate = TimeUnit.DAYS.convert(
            maxDate.timeInMillis - minDateOnly.timeInMillis,
            TimeUnit.MILLISECONDS
        )

        val calendar = Calendar.getInstance(locale)

        for (i in 0..diffDate) {
            calendar.time = minDate.time
            calendar.add(Calendar.DATE, i.toInt())

            val value = sdf.format(calendar.time)
            dateSet.add(value.toString())
        }

        for (i in 1..2) {
            val prependDate = minDate.clone() as Calendar
            prependDate.add(Calendar.DATE, -i)
            val appendDate = maxDate.clone() as Calendar
            appendDate.add(Calendar.DATE, i)
            dateSet.add(0, sdf.format(prependDate.time))
            dateSet.add(sdf.format(appendDate.time))
        }

        datePicker.apply {
            infiniteMode = false
            isUsingPlaceholder = true
            stringData = dateSet
            goToPosition(diffDateCurrent.toInt())
            onValueChanged = { value, index ->
                setSelectedDate()
                updateHour()
                updateMinute()
                datePickerChangeListener?.invoke()
            }
        }
    }

    private fun initHour() {
        for (i in 0..(23 / hourInterval)) {
            hourSet.add(String.format("%02d", i * hourInterval))
        }

        hourPicker.apply {
            infiniteMode = hourSet.size > 3
            stringData = hourSet
            goToPosition(ceil(selectedDate.get(Calendar.HOUR_OF_DAY).toDouble() / hourInterval).toInt())
            onValueChanged = { value, index ->
                setSelectedDate()
                updateMinute()
                hourPickerChangeListener?.invoke()
            }
        }
    }

    private fun initMinute() {
        for (i in 0..(59 / minuteInterval)) {
            minuteSet.add(String.format("%02d", i * minuteInterval))
        }

        minutePicker.apply {
            infiniteMode = minuteSet.size > 3
            stringData = minuteSet
            goToPosition(ceil(selectedDate.get(Calendar.MINUTE).toDouble() / minuteInterval).toInt())
            onValueChanged = { value, index ->
                setSelectedDate()
                minutePickerChangeListener?.invoke()
            }
        }
    }

    private fun setSelectedDate() {
        try {
            val calendar = Calendar.getInstance(locale)
            calendar.time = sdf.parse(datePicker.activeValue)
            calendar.set(Calendar.HOUR_OF_DAY, hourPicker.activeIndex * hourInterval)
            calendar.set(Calendar.MINUTE, minutePicker.activeIndex * minuteInterval)

            selectedDate = when {
                calendar.timeInMillis > maxDate.timeInMillis -> maxDate
                calendar.timeInMillis < minDate.timeInMillis -> minDate
                else -> calendar
            }

            onDateChangedListener?.onDateChanged(selectedDate.timeInMillis)
        } catch (e: Exception) {
        }
    }

    private fun updateHour() {
        when {
            sdf.format(selectedDate.time) == sdf.format(minDate.time) ->
                hourPicker.selectableRangeItems =
                    ceil(minDate.get(Calendar.HOUR_OF_DAY).toDouble() / hourInterval).toInt()..23 / hourInterval
            sdf.format(selectedDate.time) == sdf.format(maxDate.time) ->
                hourPicker.selectableRangeItems =
                    0..maxDate.get(Calendar.HOUR_OF_DAY) / hourInterval
            else ->
                hourPicker.selectableRangeItems = 0..23 / hourInterval
        }
    }

    private fun updateMinute() {
        when {
            sdf.format(selectedDate.time) == sdf.format(minDate.time) && selectedDate.get(Calendar.HOUR_OF_DAY) == minDate.get(
                Calendar.HOUR_OF_DAY
            ) -> minutePicker.selectableRangeItems = ceil(minDate.get(Calendar.MINUTE).toDouble() / minuteInterval).toInt()..59 / minuteInterval
            sdf.format(selectedDate.time) == sdf.format(maxDate.time) && selectedDate.get(Calendar.HOUR_OF_DAY) == maxDate.get(
                Calendar.HOUR_OF_DAY
            ) -> minutePicker.selectableRangeItems = 0..maxDate.get(Calendar.MINUTE) / minuteInterval
            else -> minutePicker.selectableRangeItems = 0..59 / minuteInterval
        }
    }

    fun getDate(): Calendar {
        return selectedDate
    }
}