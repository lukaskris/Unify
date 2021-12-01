package id.co.app.core.glide.common

import com.bumptech.glide.load.resource.gif.GifDrawable
import id.co.app.core.glide.GlideRequest

class GifFactory : MediaLoaderFactory<GifDrawable>() {

    fun build(
        properties: Properties,
        request: GlideRequest<GifDrawable>
    ) = setup(properties, request)

}