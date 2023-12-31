package id.co.app.core.utilities

import id.co.app.core.BuildConfig
import id.co.app.core.extension.toIntOrZero
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object DateFormatterUtil {

    /**
     * LOG TAG
     */
    private const val LOG_TAG = "DateTimeUtils"

    private const val TIMED_ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"
    private const val TIMED_ZONE_2_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"

    private const val SHORT_FORMAT_PATTERN = "dd/MM"
    private const val SHORT_FORMAT_PATTERN_MONTH = "dd MMM"
    private const val MEDIUM_FORMAT_PATTERN_1 = "dd-MM-yyyy"
    private const val MEDIUM_FORMAT_PATTERN_2 = "dd MMM yyyy"
    private const val MEDIUM_FORMAT_PATTERN_3 = "yyyy-MM-dd"
    private const val MEDIUM_FORMAT_PATTERN_4 = "dd MMMM yyyy"

    private const val MONTH_ONLY_PATTERN = "MMMM"
    private const val MONTH_YEAR_PATTERN = "MMMM yyyy"

    private const val TIME_FORMAT_PATTERN = "dd-MM-yyyy HH:mm:ss"
    private const val MEDIUM_TIME_FORMAT_PATTERN = "dd-MM-yyyy HH:mm"
    private const val MEDIUM_TIME_FORMAT_PATTERN_2 = "dd MMM yyyy HH:mm"
    private const val MEDIUM_TIME_FORMAT_PATTERN_3 = "dd MMMM yyyy HH:mm"

    private const val ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val ISO_8601_PATTERN_2 = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    private const val FILE_TIME_PATTERN = "yyyyMMddHHmmss"
    private const val FORMAT_CONFIRMATION_DATE = "MMddYYYYHHmmss"
    private const val HOUR_FORMAT = "HH:mm"

    private var timeZone = "UTC+7"


    /**
     * Get Date or DateTime formatting pattern
     *
     * @param dateString Date String
     * @return Format Pattern
     */
    fun getDatePattern(dateString: String): String? {
        return when {
            isFileTime(dateString) -> FILE_TIME_PATTERN
            isTimedZone(dateString) -> if (dateString.contains(".")) TIMED_ZONE_2_PATTERN else TIMED_ZONE_PATTERN
            isISO8601DateTime(dateString) -> if (dateString.contains('.')) ISO_8601_PATTERN else ISO_8601_PATTERN_2
            isMonthYear(dateString) -> MONTH_YEAR_PATTERN
            isDateTime(dateString) -> if (dateString.count { it == ':' } == 3) TIME_FORMAT_PATTERN else MEDIUM_TIME_FORMAT_PATTERN
            dateString.contains("/") -> SHORT_FORMAT_PATTERN
            Regex("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$").matches(dateString) -> MEDIUM_FORMAT_PATTERN_3
            Regex("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-\\d{4}\$").matches(dateString) -> MEDIUM_FORMAT_PATTERN_1
            else -> MEDIUM_FORMAT_PATTERN_2
        }
    }

    fun timestamp(): String {
        val date = Date()
        val simpleDateFormatter = SimpleDateFormat(FILE_TIME_PATTERN, Locale.getDefault())
        return simpleDateFormatter.format(date)
    }

    /**
     * Convert a Java Date object to String
     *
     * @param date Date Object
     * @param locale Locale
     * @return Date Object string representation
     */
    fun formatDate(date: Date, locale: Locale = Locale.getDefault()): String? {
        if (isDebug()) {
            Timber.tag(LOG_TAG).e("formatDate >> Supplied date is null")
        }
        val iso8601Format = SimpleDateFormat(
            ISO_8601_PATTERN,
            locale
        )
        iso8601Format.timeZone = TimeZone.getTimeZone(timeZone)
        if (isDebug()) {
            Timber.tag(LOG_TAG).d(
                "formatDate >> Formatting using " + iso8601Format.timeZone.displayName
                    .toString() + " | " + iso8601Format.timeZone.id
            )
        }
        return iso8601Format.format(date)
    }

    /**
     * Convert String date to another formatted time String
     *
     * @param dateString String date time
     * @param outputFormat output format date
     * @return String date with new formatted
     */
    fun formatDate(dateString: String, outputFormat: FormatType): String {
        return try {
            if (dateString.isEmpty()) return ""
            val pattern = when (outputFormat) {
                FormatType.MONTH_ONLY -> MONTH_ONLY_PATTERN
                FormatType.MONTH_YEAR_FORMAT -> MONTH_YEAR_PATTERN
                FormatType.SHORT_FORMAT -> SHORT_FORMAT_PATTERN
                FormatType.SHORT_FORMAT_MONTH -> SHORT_FORMAT_PATTERN_MONTH
                FormatType.STANDARD_FORMAT -> MEDIUM_FORMAT_PATTERN_1
                FormatType.STANDARD_FORMAT_MONTH -> MEDIUM_FORMAT_PATTERN_2
                FormatType.ISO_8601 -> ISO_8601_PATTERN_2
                FormatType.FULL_TIME_FORMAT -> TIME_FORMAT_PATTERN
                FormatType.TIME_FORMAT -> MEDIUM_TIME_FORMAT_PATTERN
                FormatType.TIME_FORMAT_MONTH -> MEDIUM_TIME_FORMAT_PATTERN_2
                FormatType.TIMEZONE_FORMAT -> TIMED_ZONE_PATTERN
                FormatType.FILE_FORMAT -> FILE_TIME_PATTERN
                FormatType.CONFIRM_DATE_FORMAT -> FORMAT_CONFIRMATION_DATE
                FormatType.HOUR_FORMAT -> HOUR_FORMAT
                FormatType.STANDARD_FORMAT_YEAR -> MEDIUM_FORMAT_PATTERN_3
                FormatType.TIMEZONE_2_FORMAT -> TIMED_ZONE_2_PATTERN
                FormatType.STANDARD_FORMAT_MONTH_FULL -> MEDIUM_FORMAT_PATTERN_4
                FormatType.TIME_FORMAT_MONTH_FULL -> MEDIUM_TIME_FORMAT_PATTERN_3
            }
            val dateInPattern = getDatePattern(dateString)
            val simpleDateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())
            dateInFormatter.timeZone = Calendar.getInstance().timeZone
            simpleDateFormatter.timeZone = Calendar.getInstance().timeZone

            val dateIn = dateInFormatter.parse(dateString)
            val result = simpleDateFormatter.format(dateIn)
            result
        } catch (ex: Exception) {
            ""
        }
    }

    /**
     * Convert String date and add with n day to another formatted time String
     *
     * @param dateString String date time
     * @param outputFormat output format date
     * @return String date with new formatted
     */
    fun formatDate(dateString: String, days: Int, outputFormat: FormatType): String {
        return try {
            if (dateString.isEmpty()) return ""
            val pattern = getPattern(outputFormat)
            val dateInPattern = getDatePattern(dateString)
            val simpleDateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())
            simpleDateFormatter.timeZone = Calendar.getInstance().timeZone
//            simpleDateFormatter.timeZone = TimeZone.getTimeZone("Asia/Bangkok")

            val dateIn = dateInFormatter.parse(dateString)
            val c = Calendar.getInstance()
            c.time = dateIn
            c.add(Calendar.DATE, days)

            simpleDateFormatter.format(c.time)
        } catch (ex: Exception) {
            ""
        }
    }

    /**
     * Convert String date and add with n day to another formatted time String
     *
     * @param dateString String date time
     * @param outputFormat output format date
     * @return String date with new formatted
     */
    fun minusOneYear(dateString: String, outputFormat: FormatType): String {
        return try {
            if (dateString.isEmpty()) return ""
            val pattern = getPattern(outputFormat)
            val dateInPattern = getDatePattern(dateString)
            val simpleDateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())
            simpleDateFormatter.timeZone = Calendar.getInstance().timeZone
//            simpleDateFormatter.timeZone = TimeZone.getTimeZone("Asia/Bangkok")

            val dateIn = dateInFormatter.parse(dateString)
            val c = Calendar.getInstance()
            c.time = dateIn
            c.add(Calendar.YEAR, -1)

            simpleDateFormatter.format(c.time)
        } catch (ex: Exception) {
            ""
        }
    }


    /**
     * Convert String date to different date with now
     *
     * @param dateString String date time
     * @param outputFormat output format date
     * @return return true if date same
     */
    fun isDifferentDate(dateString: String): Boolean {
        if (dateString.isEmpty()) return false
        val dateInPattern = getDatePattern(dateString)
        val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())
        val dateNowFormatter = SimpleDateFormat(SHORT_FORMAT_PATTERN, Locale.getDefault())
        val dateIn = dateInFormatter.parse(dateString)
        val dateNow = Date()

        val dateNowString = dateNowFormatter.format(dateNow)
        val dateInString = dateNowFormatter.format(dateIn)
        return dateNowString != dateInString
    }

    /**
     * Convert String date to another formatted time String
     *
     * @param date date time
     * @param outputFormat output format date
     * @return String date with new formatted
     */
    fun formatDate(date: Date, outputFormat: FormatType): String {
        return try {
            val pattern = getPattern(outputFormat)
            val simpleDateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            simpleDateFormatter.timeZone = Calendar.getInstance().timeZone
            simpleDateFormatter.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun formatDate(time: Long, outputFormat: FormatType): String {
        val pattern = getPattern(outputFormat)
        val simpleDateFormatter = SimpleDateFormat(pattern, Locale.getDefault())

        return simpleDateFormatter.format(time)
    }

    private fun getPattern(format: FormatType): String{
        return when (format) {
            FormatType.MONTH_ONLY -> MONTH_ONLY_PATTERN
            FormatType.MONTH_YEAR_FORMAT -> MONTH_YEAR_PATTERN
            FormatType.SHORT_FORMAT -> SHORT_FORMAT_PATTERN
            FormatType.SHORT_FORMAT_MONTH -> SHORT_FORMAT_PATTERN_MONTH
            FormatType.STANDARD_FORMAT -> MEDIUM_FORMAT_PATTERN_1
            FormatType.STANDARD_FORMAT_MONTH -> MEDIUM_FORMAT_PATTERN_2
            FormatType.ISO_8601 -> ISO_8601_PATTERN_2
            FormatType.FULL_TIME_FORMAT -> TIME_FORMAT_PATTERN
            FormatType.TIME_FORMAT -> MEDIUM_TIME_FORMAT_PATTERN
            FormatType.TIME_FORMAT_MONTH -> MEDIUM_TIME_FORMAT_PATTERN_2
            FormatType.TIMEZONE_FORMAT -> TIMED_ZONE_PATTERN
            FormatType.FILE_FORMAT -> FILE_TIME_PATTERN
            FormatType.CONFIRM_DATE_FORMAT -> FORMAT_CONFIRMATION_DATE
            FormatType.HOUR_FORMAT -> HOUR_FORMAT
            FormatType.STANDARD_FORMAT_YEAR -> MEDIUM_FORMAT_PATTERN_3
            FormatType.TIMEZONE_2_FORMAT -> TIMED_ZONE_2_PATTERN
            FormatType.STANDARD_FORMAT_MONTH_FULL -> MEDIUM_FORMAT_PATTERN_4
            FormatType.TIME_FORMAT_MONTH_FULL -> MEDIUM_TIME_FORMAT_PATTERN_3
        }
    }

    /**
     * Convert String date to another formatted time String
     *
     * @param dateString String date time
     * @param outputFormat output format date
     * @return String date with new formatted
     */
    fun toDate(dateString: String): Date {
        if (dateString.isEmpty()) return Date()
        val dateInPattern = getDatePattern(dateString)
        val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())

        val dateIn = dateInFormatter.parse(dateString)
        return dateIn ?: Date()
    }

    fun iso8601Now(): String {
        val date = Date()
        val simpleDateFormatter = SimpleDateFormat(ISO_8601_PATTERN_2, Locale.getDefault())
        simpleDateFormatter.timeZone = Calendar.getInstance().timeZone
        return simpleDateFormatter.format(date)
    }

    /**
     * Convert String date to different date with now
     *
     * @param dateString String date time
     * @param outputFormat output format date
     * @return String date with new formatted
     */
    fun differentDate(dateString: String): Long {
        if (dateString.isEmpty()) return 0L
        val dateInPattern = getDatePattern(dateString)
        val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())

        val dateIn = dateInFormatter.parse(dateString)

        val time = dateIn.time
        val current = Calendar.getInstance().time
        val diff = current.time - time
        return diff / (24 * 60 * 60 * 1000)
    }

    fun currentDay(): Int {
        val date = Date()
        val formatter = SimpleDateFormat("dd")
        return formatter.format(date).toIntOrZero()
    }

    /**
     * Tell whether or not a given string represent a date time string or a simple date
     *
     * @param dateString Date String
     * @return True if given string is a date time False otherwise
     */
    fun isDateTime(dateString: String): Boolean {
        return dateString.trim { it <= ' ' }.split(" ").toTypedArray().size > 1
    }

    /**
     * Tell whether or not a given string represent a date time timedzone string or a simple date
     *
     * @param dateString Date String
     * @return True if given string is a date time with timedzone False otherwise
     */
    fun isTimedZone(dateString: String): Boolean {
        return dateString.trim().contains("+")
    }

    /**
     * Tell whether or not a given string represent a ISO8601 date time string or a simple date
     *
     * @param dateString Date String
     * @return True if given string is a ISO8601 date time False otherwise
     */
    fun isISO8601DateTime(dateString: String): Boolean {
        return dateString.trim().contains("T")
    }

    fun isMonthYear(dateString: String): Boolean {
        val splits = dateString.split(' ')
        return splits.size == 2 && !splits.first().replace("-","").matches("[0-9]+".toRegex())
    }

    /**
     * Tell whether or not a given string represent a ISO8601 date time string or a simple date
     *
     * @param dateString Date String
     * @return True if given string is a ISO8601 date time False otherwise
     */
    fun isFileTime(dateString: String): Boolean {
        return dateString.trim().length == FILE_TIME_PATTERN.length && !dateString.contains(' ') && !dateString.contains(
            ':'
        ) && !dateString.contains('-') && !dateString.contains('/')
    }


    /**
     * Tell is debug apps or release app
     *
     * @return True if is debug apps False otherwise
     */
    fun isDebug() = BuildConfig.DEBUG

    enum class FormatType {
        SHORT_FORMAT, // dd/MM
        SHORT_FORMAT_MONTH, // dd/MM
        MONTH_ONLY, // MMM
        STANDARD_FORMAT, // dd-MM-yyyy
        STANDARD_FORMAT_YEAR, // yyyy-MM-dd
        STANDARD_FORMAT_MONTH, // dd MMM yyyy
        STANDARD_FORMAT_MONTH_FULL, // dd MMMM yyyy
        ISO_8601, // yyyy-MM-dd'T'HH:mm:ss'Z'
        FULL_TIME_FORMAT, // dd-MM-yyyy HH:mm:ss
        TIME_FORMAT, // dd-MM-yyyy HH:mm
        TIME_FORMAT_MONTH, // dd MMM yyyy HH:mm
        TIME_FORMAT_MONTH_FULL, // dd MMMM yyyy HH:mm
        TIMEZONE_FORMAT, // yyyy-MM-dd'T'HH:mm:ssXXX
        TIMEZONE_2_FORMAT, // yyyy-MM-dd'T'HH:mm:ss.SSXXX
        FILE_FORMAT, // yyyyMMddHHmmss
        MONTH_YEAR_FORMAT, // MMM yyyy
        CONFIRM_DATE_FORMAT, // MMddYYYYHHmmss
        HOUR_FORMAT // HH:ss
    }
}