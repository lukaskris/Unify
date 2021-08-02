package id.co.app.components.datetimepicker.picker

import android.graphics.Color

class PickerDecorator(
    val fontSize: Pair<Float, Float>,
    val fontColor: Pair<Int, Int>,
    val fontBold: Pair<Boolean, Boolean>,
    val disabledColor: Int = Color.argb(81, 49,  53, 59)
)