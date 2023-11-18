package id.co.app.core

import id.co.app.core.extension.formatDate
import id.co.app.core.utilities.DateFormatterUtil
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Lukas Kristianto on 15/12/21 10.21.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

class DateFormatTest {
    @Test
    fun testDateFormatPattern2(){
        val date = "2023-11-07T00:00:00+08:00"
        val dateOutput = DateFormatterUtil.formatDate(date, DateFormatterUtil.FormatType.FULL_TIME_FORMAT)

        print(dateOutput)

    }

    @Test
    fun testDateFormatPattern(){
        val date = "2022-03-01T09:53:07+07:00"
//        val dateOutput = DateFormatterUtil.formatDate(date, DateFormatterUtil.FormatType.HOUR_FORMAT)

        val dateInPattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val simpleDateFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val simpleDateFormatter2 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateInFormatter = SimpleDateFormat(dateInPattern, Locale.getDefault())
        dateInFormatter.timeZone = Calendar.getInstance().timeZone
        simpleDateFormatter.timeZone = Calendar.getInstance().timeZone
        simpleDateFormatter2.timeZone = Calendar.getInstance().timeZone

        val dateIn = dateInFormatter.parse(date)
        val result = simpleDateFormatter.format(dateIn)
        val resull2 = simpleDateFormatter2.format(dateIn)

        Assert.assertEquals(result,"09:53")

    }

    @Test
    fun testDateFormatQR(){
        val date = "2021-12-13"
        val date2 = "13-06-2021"
        val regex = Regex("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$")
        val regex2 = Regex("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-\\d{4}\$")

        val dateOutput = date2.formatDate(DateFormatterUtil.FormatType.STANDARD_FORMAT)
        val matches = regex2.matches(date2)
        assert(matches)
        DateFormatterUtil.formatDate(date, DateFormatterUtil.FormatType.STANDARD_FORMAT)

    }
}