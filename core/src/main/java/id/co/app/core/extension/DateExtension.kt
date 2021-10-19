package id.co.app.core.extension

import id.co.app.core.utilities.DateFormatterUtil
import java.util.*

/**
 * Created by Lukas Kristianto on 09/07/21 19.48.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

fun String.getDifferenceDateWithCurrentDate(): Long {
    return DateFormatterUtil.differentDate(this)
}

fun String.isDifferentDate(): Boolean{
    return DateFormatterUtil.isDifferentDate(this)
}

fun String.formatDate(output: DateFormatterUtil.FormatType): String{
    return DateFormatterUtil.formatDate(this, output)
}

fun String.toDate(): Date{
    return DateFormatterUtil.toDate(this)
}

fun Date.formatDate(output: DateFormatterUtil.FormatType): String{
    return DateFormatterUtil.formatDate(this, output)
}

fun Long.formatDate(output: DateFormatterUtil.FormatType): String{
    return DateFormatterUtil.formatDate(this, output)
}