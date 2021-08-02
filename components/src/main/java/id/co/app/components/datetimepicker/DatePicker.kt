package id.co.app.components.datetimepicker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.datetimepicker.picker.PickerAdapter
import id.co.app.components.datetimepicker.picker.PickerDecorator
import id.co.app.components.datetimepicker.picker.PickerUnify
import id.co.app.components.utils.toPx
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DatePicker : FrameLayout {
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
        R.layout.date_picker_layout, this) }
    lateinit var selectedDate: Calendar
    private val sdf = SimpleDateFormat("dd MM yyyy", locale)
    val dayPicker: PickerUnify
    val monthPicker: PickerUnify
    val yearPicker: PickerUnify
    val highlightView: View

    val dateSet: MutableList<String> = mutableListOf()

    val monthSet: MutableList<String> = mutableListOf()

    val yearSet: MutableList<String> = mutableListOf()

    private lateinit var defaultDate: Calendar
    private lateinit var minDate: Calendar
    private lateinit var maxDate: Calendar
    private var onDateChangedListener: OnDateChangedListener? = null
    private var onFirstYearChange: Boolean = true
    private var onFirstMonthChange: Boolean = true
    private var onFirstDayChange: Boolean = true

    var dayPickerChangeListener: (() -> Unit)? = null
    var monthPickerChangeListener: (() -> Unit)? = null
    var yearPickerChangeListener: (() -> Unit)? = null
    var showDay: Boolean = true
    var showMonth: Boolean = true
    var showYear: Boolean = true

    init {
        dayPicker = view.findViewById(R.id.unify_date_picker)
        monthPicker = view.findViewById(R.id.unify_month_picker)
        yearPicker = view.findViewById(R.id.unify_year_picker)
        highlightView = view.findViewById(R.id.unify_calendar_highlight)
    }

    fun init(
        defaultDate: Calendar,
        minDate: Calendar,
        maxDate: Calendar,
        onDateChangedListener: OnDateChangedListener?,
        showDay: Boolean = true,
        showMonth: Boolean = true,
        showYear: Boolean = true
    ) {
        val calendar = defaultDate

        selectedDate = when {
            calendar.timeInMillis > maxDate.timeInMillis -> maxDate
            calendar.timeInMillis < minDate.timeInMillis -> minDate
            else -> calendar
        }

        this.onDateChangedListener = onDateChangedListener
        this.defaultDate = defaultDate
        this.minDate = minDate
        this.maxDate = maxDate
        this.showDay = showDay
        this.showMonth = showMonth
        this.showYear = showYear

        initYear()
        initMonth()
        initDate()

        showItemDatePicker()
    }

    fun getDate(): Calendar {
        return selectedDate
    }

    private fun showItemDatePicker() {
        val dayParams = dayPicker.layoutParams as LinearLayout.LayoutParams
        val monthParams = monthPicker.layoutParams as LinearLayout.LayoutParams
        val yearParams = yearPicker.layoutParams as LinearLayout.LayoutParams

        if(!showDay) {
            dayPicker.visibility = View.GONE

            monthParams.width = 150.toPx()
            monthParams.weight = 1.0f
            monthPicker.layoutParams = monthParams

            yearParams.width = 50.toPx()
            yearParams.weight = 1.0f
            yearPicker.layoutParams = yearParams

        }

        if(!showMonth) {
            monthPicker.visibility = View.GONE

            dayParams.width = 50.toPx()
            dayParams.weight = 1.0f
            dayPicker.layoutParams = dayParams

            yearParams.width = 50.toPx()
            yearParams.weight = 1.0f
            yearPicker.layoutParams = yearParams
        }

        if(!showYear) {
            yearPicker.visibility = View.GONE

            dayParams.width = 50.toPx()
            dayParams.weight = 1.0f
            dayPicker.layoutParams = dayParams

            monthParams.width = 150.toPx()
            monthParams.weight = 1.0f
            monthPicker.layoutParams = monthParams
        }
    }


    private fun initYear() {
        yearPicker.infiniteMode = false
        val minYear = minDate.get(Calendar.YEAR)
        val maxYear = maxDate.get(Calendar.YEAR)
        for (i in minYear..maxYear) {
            yearSet.add(i.toString())
        }

        if (maxYear - minYear != 0) {
            for (i in 1..2) {
                yearSet.add(0, (minDate.get(Calendar.YEAR) - i).toString())
                yearSet.add((maxDate.get(Calendar.YEAR) + i).toString())
            }
            yearPicker.isUsingPlaceholder = true
        } else {
            val defaultColor = ContextCompat.getColor(context, R.color.Unify_N700_20)
            yearPicker.pickerDecorator = PickerDecorator(Pair(14f, 14f), Pair(defaultColor, defaultColor), Pair(true, false))
        }

        if(!showMonth) {
            yearPicker.textAlign = PickerAdapter.ALIGN_CENTER
        } else {
            yearPicker.textAlign = PickerAdapter.ALIGN_LEFT
        }

        yearPicker.stringData = yearSet
        yearPicker.goToPosition(selectedDate.get(Calendar.YEAR) - minYear)

        yearPicker.onValueChanged = { value, index ->
            setSelectedDate()
            val newDateSet: MutableList<String> = mutableListOf()
            for (i in 1..getMaxDate()) {
                newDateSet.add(i.toString())
            }

            dayPicker.newStringData = newDateSet

            updateMonths()
            updateDates()

            onFirstYearChange = false

            yearPickerChangeListener?.invoke()
        }
    }

    private fun initMonth() {
        monthSet.addAll(DateFormatSymbols.getInstance(locale).months)
        monthPicker.stringData = monthSet
        updateMonths()
        monthPicker.goToPosition(selectedDate.get(Calendar.MONTH))

        monthPicker.onValueChanged = { value, index ->
            setSelectedDate()
            val newDateSet: MutableList<String> = mutableListOf()
            for (i in 1..getMaxDate()) {
                newDateSet.add(i.toString())
            }

            dayPicker.newStringData = newDateSet

            updateDates()

            onFirstMonthChange = false

            monthPickerChangeListener?.invoke()
        }
    }

    private fun initDate() {
        for (i in 1..getMaxDate()) {
            dateSet.add(i.toString())
        }
        if(!showMonth) {
            dayPicker.textAlign = PickerAdapter.ALIGN_CENTER
        } else {
            dayPicker.textAlign = PickerAdapter.ALIGN_RIGHT
        }
        dayPicker.stringData = dateSet
        updateDates()
        dayPicker.goToPosition(selectedDate.get(Calendar.DATE) - 1)

        dayPicker.onValueChanged = { value, index ->
            setSelectedDate()
            onFirstDayChange = false

            dayPickerChangeListener?.invoke()
        }
    }

    private fun setSelectedDate() {
        try {
            val calendar = Calendar.getInstance()

            if(!showYear) {
                calendar.time =
                    sdf.parse("${dayPicker.activeValue} ${monthPicker.activeIndex + 1} ${defaultDate.get(Calendar.YEAR)}")
            } else {
                calendar.time =
                    sdf.parse("${dayPicker.activeValue} ${monthPicker.activeIndex + 1} ${yearPicker.activeValue}")
            }

            selectedDate = when {
                calendar.timeInMillis > maxDate.timeInMillis -> maxDate
                calendar.timeInMillis < minDate.timeInMillis -> minDate
                else -> calendar
            }
            
            onDateChangedListener?.onDateChanged(selectedDate.timeInMillis)
        } catch (e: Exception) {}
    }

    private fun updateMonths() {
        var minMonth = 0
        var maxMonth = 11
        if (selectedDate.get(Calendar.YEAR) == minDate.get(Calendar.YEAR)) {
            minMonth = minDate.get(Calendar.MONTH)
        }

        if (selectedDate.get(Calendar.YEAR) == maxDate.get(Calendar.YEAR)) {
            maxMonth = maxDate.get(Calendar.MONTH)
        }

        monthPicker.selectableRangeItems = minMonth..maxMonth
    }

    private fun updateDates() {
        var minAvailableDate = 0
        var maxAvailableDate = getMaxDate()

        if (selectedDate.get(Calendar.YEAR) == minDate.get(Calendar.YEAR) && selectedDate.get(
                Calendar.MONTH
            ) == minDate.get(Calendar.MONTH)
        ) {
            minAvailableDate = minDate.get(Calendar.DAY_OF_MONTH) - 1
        }

        if (selectedDate.get(Calendar.YEAR) == maxDate.get(Calendar.YEAR) && selectedDate.get(
                Calendar.MONTH
            ) == maxDate.get(Calendar.MONTH)
        ) {
            maxAvailableDate = maxDate.get(Calendar.DAY_OF_MONTH) - 1
        }

        dayPicker.selectableRangeItems = minAvailableDate..maxAvailableDate
    }

    private fun getMaxDate(): Int {
        val calendar = Calendar.getInstance()
        return try {
            calendar.time =
                sdf.parse("1 ${monthPicker.activeIndex + 1} ${yearPicker.activeValue}")

            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        } catch (e: Exception) {
            selectedDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
    }
}