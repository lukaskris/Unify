package id.co.app.components.notify.internal.utils

import android.text.Html
import java.util.Random

/**
 * Created by Lukas Kristianto on 15/09/21 21.00.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

internal object Utils {
    fun getRandomInt(): Int {
        return Random(System.currentTimeMillis()).nextInt()
    }

    fun getAsSecondaryFormattedText(str: String?): CharSequence? {
        str ?: return null

        return Html.fromHtml("<font color='#3D3D3D'>$str</font>")
    }
}