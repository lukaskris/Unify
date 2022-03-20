package id.co.app.core

import id.co.app.core.utilities.DateFormatterUtil
import org.junit.Assert
import org.junit.Test
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
        val date = "2020-02-02T02:02:02.02+02:00"
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
}