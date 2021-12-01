package id.co.app.core.extension

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import id.co.app.core.glide.GlideApp
import id.co.app.core.glide.common.ERROR_RES_UNIFY
import id.co.app.core.glide.common.Properties
import id.co.app.core.glide.utils.DEFAULT_ROUNDED
import id.co.app.core.glide.utils.MediaTarget
import id.co.app.core.glide.utils.drawableFromId
import id.co.app.core.glide.MediaLoaderApi.loadImage as loadImageBuilder
import id.co.app.core.glide.MediaLoaderTarget.loadImage as loadImageWithTarget

/**
 * Created by Lukas Kristianto on 18/08/21 16.57.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */


fun ImageView.loadImage(bitmap: Bitmap?) = call(bitmap, Properties())

fun ImageView.loadImage(drawable: Drawable?) = this.setImageDrawable(drawable)

fun ImageView.loadImage(resource: Int) =
    this.setImageDrawable(drawableFromId(this.context, resource))

fun ImageView.loadImage(uri: Uri) = this.setImageURI(uri)

inline fun ImageView.loadImage(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties().apply(properties))

inline fun ImageView.loadImageFitCenter(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(url, Properties().apply(properties).fitCenter())

inline fun ImageView.loadImageWithoutPlaceholder(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(
    url, Properties()
        .apply(properties)
        .setPlaceHolder(-1)
)

inline fun ImageView.loadImageCircle(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(
    url, Properties().apply(properties)
        .isCircular(true)

        /*
         * loadImageCircle() extension must be haven't placeholder,
         * the loader effect should be handled by team by
         * using own shimmering.
         * */
        .setPlaceHolder(-1)
)

fun ImageView.loadImageRounded(
    resource: Int,
    rounded: Float
) = this.setImageResource(resource)

inline fun ImageView.loadImageRounded(
    data: Bitmap,
    rounded: Float = DEFAULT_ROUNDED,
    crossinline properties: Properties.() -> Unit = {}
) {
    call(
        data, Properties()
            .apply(properties)
            .setRoundedRadius(rounded)
    )
}

inline fun ImageView.loadImageRounded(
    url: String?,
    rounded: Float = DEFAULT_ROUNDED,
    crossinline properties: Properties.() -> Unit = {}
) {
    call(
        url, Properties()
            .apply(properties)
            .setRoundedRadius(rounded)
    )
}

inline fun ImageView.loadIcon(
    url: String?,
    crossinline properties: Properties.() -> Unit = {}
) = call(
    url, Properties().apply(properties)
        .useCache(false)
        .useBlurHash(false)

        /*
         * loadIcon() extension must be haven't placeholder,
         * the loader effect should be handled by team by
         * using own shimmering.
         * */
        .setPlaceHolder(-1)
)


@PublishedApi
internal fun ImageView.call(source: Any?, properties: Properties) {
    if (context.isValid()) {
        try {
            loadImageBuilder(
                imageView = this,
                properties = properties.setSource(source)
            )
        } catch (e: Exception) {
            e.printStackTrace()

            /*
            * don't let the imageView haven't image
            * render with error drawable
            * */
            this.loadImage(ERROR_RES_UNIFY)
        }
    }
}

fun <T : View> loadImageWithTarget(
    context: Context,
    url: String,
    properties: Properties.() -> Unit,
    mediaTarget: MediaTarget<T>
) {
    loadImageWithTarget(
        context,
        Properties().apply(properties).setSource(url),
        mediaTarget
    )
}

fun ImageView?.clearImage() {
    if (this != null && context.isValid()) {
        GlideApp.with(this.context).clear(this)
    }
}

fun Context?.isValid(): Boolean {
    return when {
        this == null -> false
        this is Activity -> !(this.isDestroyed || this.isFinishing)
        else -> true
    }
}