package id.co.app.components.datetimepicker

import android.content.Context
import android.os.Build
import java.lang.Exception
import java.util.*

/**
 * Created by Ade Fulki on 2019-07-22.
 * ade.hadian@tokopedia.com
 */

object LocaleUtils{
    fun getCurrentLocale(context: Context): Locale {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales.get(0)
            } else {
                context.resources.configuration.locale
            }
        }catch (e: Exception){
            // default locale will be ID if locale configuration not found
            Locale("in", "ID")
        }
    }

    fun getIDLocale(): Locale = Locale("in", "ID")
}