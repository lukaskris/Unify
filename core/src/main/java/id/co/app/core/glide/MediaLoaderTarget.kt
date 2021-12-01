package id.co.app.core.glide

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import id.co.app.core.glide.common.BitmapFactory
import id.co.app.core.glide.common.Loader
import id.co.app.core.glide.common.Properties
import id.co.app.core.glide.utils.MediaTarget

object MediaLoaderTarget {

    private val bitmap by lazy { BitmapFactory() }
    private val handler by lazy { Handler() }

    fun <T: View> loadImage(context: Context, properties: Properties, target: MediaTarget<T>) {

        // handling empty url
        if (properties.data.toString().isEmpty()) return

        if (target is ImageView && properties.data == null) {
            // if the data source is null, the image will be render the error drawable
            target.setImageDrawable(AppCompatResources.getDrawable(context, properties.error))
            return
        }

        if (properties.data is String) {
            GlideApp.with(context).asBitmap().also {
                // url builder
                val source = Loader.urlBuilder(properties.data.toString())

                val request = bitmap.build(
                        context = context,
                        properties = properties,
                        request = it
                ).load(source)

                // delay handler
                handler.postDelayed({
                    request.into(target)
                }, properties.renderDelay)
            }
        }
    }

}