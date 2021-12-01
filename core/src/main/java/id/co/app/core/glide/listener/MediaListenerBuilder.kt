package id.co.app.core.glide.listener

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import id.co.app.core.glide.common.Properties
import id.co.app.core.glide.wrapper.MediaDataSource.Companion.mapTo as dataSource

object MediaListenerBuilder {

    fun callback(
        context: Context,
        properties: Properties,
        startTime: Long,
        listener: MediaListener?
    ) = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
        ): Boolean {
            listener?.onFailed(e)
            return false
        }

        override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            val loadTime = (System.currentTimeMillis() - startTime).toString()

            // override the load time into properties
            properties.loadTime = loadTime

            listener?.onLoaded(resource, dataSource(dataSource))
            return false
        }
    }

}
