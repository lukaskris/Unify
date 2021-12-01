package id.co.app.core.glide.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.load.engine.GlideException

// wrapper of GlideException
typealias MediaException = GlideException

// default rounded for loadImageRounded()
const val DEFAULT_ROUNDED = 5.0f

// resource ID reader and convert it into drawable
fun drawableFromId(context: Context, id: Int): Drawable? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        context.resources.getDrawable(id, context.applicationContext.theme)
    } else {
        AppCompatResources.getDrawable(context, id)
    }
}

/*
* determine object classification in every network capability segmentation
* only supported for 2 segmentation are slow for 2g / 3g, and fast for 4g and wifi.
* */
const val LOW_QUALITY = "3g"
const val HIGH_QUALITY = "4g"

// leave as high quality if users networkState have unexpected state.
const val UNDEFINED = "4g"

// the key of connection type
const val LOW_QUALITY_SETTINGS = 1 // 2g / 3g
const val HIGH_QUALITY_SETTINGS = 2 // 4g / wifi

// cache pref name
const val MEDIA_QUALITY_PREF = "media_image_quality"

const val PARAM_ECT = "ect"