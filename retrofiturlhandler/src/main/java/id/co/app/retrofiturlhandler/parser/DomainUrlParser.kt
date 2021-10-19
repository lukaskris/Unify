package id.co.app.retrofiturlhandler.parser

import android.text.TextUtils
import id.co.app.retrofiturlhandler.RetrofitUrlHandler
import id.co.app.retrofiturlhandler.cache.Cache
import id.co.app.retrofiturlhandler.cache.LruCache
import okhttp3.HttpUrl

/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

class DomainUrlParser : UrlParser {
    private val mCache: Cache<String, String> by lazy { LruCache(100) }

    override fun init(retrofitUrlHandler: RetrofitUrlHandler) {
    }

    override fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl {
        if (null == domainUrl) return url
        val builder: HttpUrl.Builder = url.newBuilder()
        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            for (i in 0 until url.pathSize) {
                builder.removePathSegment(0)
            }
            val newPathSegments: MutableList<String> = ArrayList()
            newPathSegments.addAll(domainUrl.encodedPathSegments)
            newPathSegments.addAll(url.encodedPathSegments)
            for (PathSegment in newPathSegments) {
                builder.addEncodedPathSegment(PathSegment)
            }
        } else {
            builder.encodedPath(mCache.get(getKey(domainUrl, url)) ?: "")
        }
        val httpUrl: HttpUrl = builder
            .scheme(domainUrl.scheme)
            .host(domainUrl.host)
            .port(domainUrl.port)
            .build()
        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            mCache.put(getKey(domainUrl, url), httpUrl.encodedPath)
        }
        return httpUrl
    }

    private fun getKey(domainUrl: HttpUrl, url: HttpUrl): String {
        return domainUrl.encodedPath + url.encodedPath
    }
}