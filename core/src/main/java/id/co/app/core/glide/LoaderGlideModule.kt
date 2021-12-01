package id.co.app.core.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class LoaderGlideModule: AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDiskCache(DiskLruCacheFactory(
            Glide.getPhotoCacheDir(context)?.absolutePath,
            (LIMIT_CACHE_SIZE_IN_MB * SIZE_IN_MB * SIZE_IN_MB).toLong()
        ))
        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
    }

    override fun isManifestParsingEnabled(): Boolean {
        /*
        * improve the initial startup time of Glide and
        * avoid some potential problems with trying to parse metadata.
        * */
        return false
    }

    companion object {
        private const val LIMIT_CACHE_SIZE_IN_MB = 50
        private const val SIZE_IN_MB = 1024
    }

}
