package id.co.app.core.extension

import android.content.res.Resources
import java.text.NumberFormat
import java.util.*

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun String?.toIntOrZero(): Int = (this?.replace(".","")?.toIntOrNull() ?: 0)
fun String?.toLongOrZero(): Long = (this?.replace(".","")?.toLongOrNull() ?: 0L)
fun Long.toThousandFormat(): String = NumberFormat.getNumberInstance(Locale.US)
    .format(this)
    .replace(",", ".")
fun Int.toThousandFormat(): String = NumberFormat.getNumberInstance(Locale.US)
    .format(this)
    .replace(",", ".")