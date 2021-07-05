package id.co.app.components.typography

import android.content.Context
import android.graphics.Typeface
import java.util.*

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object TypefaceAssets {
    val cache = Hashtable<String, Typeface>()
}

fun getTypeface(c: Context, assetPath: String): Typeface? {
    if (!TypefaceAssets.cache.containsKey(assetPath)) {
        try {
            val t = Typeface.createFromAsset(
                c.assets,
                assetPath
            )
            TypefaceAssets.cache[assetPath] = t
        } catch (e: Exception) {
            return null
        }
    }
    return TypefaceAssets.cache[assetPath]
}