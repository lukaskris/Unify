package id.co.app.core

import id.co.app.core.utilities.DateFormatterUtil
import org.junit.Test


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
}