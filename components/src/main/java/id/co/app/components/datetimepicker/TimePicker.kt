package id.co.app.components.datetimepicker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import id.co.app.components.datetimepicker.picker.PickerAdapter
import id.co.app.components.datetimepicker.picker.PickerUnify
import id.co.app.components.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class TimePicker : FrameLayout {
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
        R.layout.time_picker_layout, this) }
    lateinit var selectedDate: Calendar

    val hourSet: MutableList<String> = mutableListOf()

    val minuteSet: MutableList<String> = mutableListOf()

    val hourPicker: PickerUnify
    val minutePicker: PickerUnify
    val sdf = SimpleDateFormat("EEE, dd MMM yy", locale)
    var hourInterval: Int = 1
    var minuteInterval: Int = 1

    private lateinit var defaultDate: Calendar
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private var onDateChangedListener: OnDateChangedListener? = null

    var minutePickerChangeListener: (() -> Unit)? = null
    var hourPickerChangeListener: (() -> Unit)? = null

    init {
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
        calendar.set(
            Calendar.HOUR_OF_DAY,
            ceil(defaultDate.get(Calendar.HOUR_OF_DAY).toDouble() / hourInterval).toInt() * hourInterval
        )
        calendar.set(
            Calendar.MINUTE,
            ceil(defaultDate.get(Calendar.MINUTE).toDouble() / minuteInterval).toInt() * minuteInterval
        )

        selectedDate = when {
            calendar.timeInMillis > maxDate.timeInMillis -> maxDate
            calendar.timeInMillis < minDate.timeInMillis -> minDate
            else -> calendar
        }

        this.onDateChangedListener = onDateChangedListener
        this.defaultDate = defaultDate
        this.minDate = minDate
        this.maxDate = maxDate

        initHour()
        initMinute()
    }

    private fun initHour() {
        for (i in 0..(23 / hourInterval)) {
            hourSet.add(String.format("%02d", i * hourInterval))
        }

        hourPicker.apply {
            textAlign = PickerAdapter.ALIGN_RIGHT
            stringData = hourSet
            selectableRangeItems =
                ceil(minDate.get(Calendar.HOUR_OF_DAY).toDouble() / hourInterval).toInt()..maxDate.get(Calendar.HOUR_OF_DAY) / hourInterval
            goToPosition(ceil(defaultDate.get(Calendar.HOUR_OF_DAY).toDouble() / hourInterval).toInt())
            onValueChanged = { value, index ->
                setSelectedDate()
                updateMinute()

                hourPickerChangeListener?.invoke()
            }
        }

        updateMinute()
    }

    private fun initMinute() {
        for (i in 0..(59 / minuteInterval)) {
            minuteSet.add(String.format("%02d", i * minuteInterval))
        }

        minutePicker.apply {
            textAlign = PickerAdapter.ALIGN_LEFT
            stringData = minuteSet
            goToPosition(ceil(selectedDate.get(Calendar.MINUTE).toDouble() / minuteInterval).toInt())
            onValueChanged = { value, index ->
                setSelectedDate()

                minutePickerChangeListener?.invoke()
            }
        }
    }

    private fun setSelectedDate() {
        val calendar = defaultDate
        calendar.set(Calendar.HOUR_OF_DAY, hourPicker.activeIndex * hourInterval)
        calendar.set(Calendar.MINUTE, minutePicker.activeIndex * minuteInterval)

        selectedDate = when {
            calendar.timeInMillis > maxDate.timeInMillis -> maxDate
            calendar.timeInMillis < minDate.timeInMillis -> minDate
            else -> calendar
        }

        onDateChangedListener?.onDateChanged(selectedDate.timeInMillis)
    }

    private fun updateMinute() {
        if (selectedDate.get(Calendar.HOUR_OF_DAY) == minDate.get(Calendar.HOUR_OF_DAY)) {
            minutePicker.selectableRangeItems =
                ceil(minDate.get(Calendar.MINUTE).toDouble() / minuteInterval).toInt()..59 / minuteInterval
        } else if (selectedDate.get(Calendar.HOUR_OF_DAY) == maxDate.get(Calendar.HOUR_OF_DAY)) {
            minutePicker.selectableRangeItems = 0..maxDate.get(Calendar.MINUTE) / minuteInterval
        } else {
            minutePicker.selectableRangeItems = 0..59 / minuteInterval
        }
    }

    fun getDate(): Calendar {
        return selectedDate
    }
}