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

class AdvancedUrlParser : UrlParser {
    private lateinit var mRetrofitUrlManager: RetrofitUrlHandler
    private val mCache: Cache<String, String> by lazy { LruCache(100) }

    override fun init(retrofitUrlHandler: RetrofitUrlHandler) {
        mRetrofitUrlManager = retrofitUrlHandler

    }

    override fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl {
        if (null == domainUrl) return url
        val builder: HttpUrl.Builder = url.newBuilder()
        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            for (i in 0 until url.pathSize) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0)
            }
            val newPathSegments: MutableList<String> = ArrayList()
            newPathSegments.addAll(domainUrl.encodedPathSegments)
            if (url.pathSize > mRetrofitUrlManager.pathSize) {
                val encodedPathSegments: List<String> = url.encodedPathSegments
                for (i in mRetrofitUrlManager.pathSize until encodedPathSegments.size) {
                    newPathSegments.add(encodedPathSegments[i])
                }
            } else if (url.pathSize < mRetrofitUrlManager.pathSize) {
                throw IllegalArgumentException(
                    String.format(
                        "Your final path is %s, but the baseUrl of your RetrofitUrlManager#startAdvancedModel is %s",
                        url.scheme + "://" + url.host + url.encodedPath,
                        mRetrofitUrlManager.getBaseUrl()?.scheme + "://"
                                + mRetrofitUrlManager.getBaseUrl()?.host
                                + mRetrofitUrlManager.getBaseUrl()?.encodedPath
                    )
                )
            }
            for (pathSegment: String in newPathSegments) {
                builder.addEncodedPathSegment(pathSegment)
            }
        } else {
            mCache.get(getKey(domainUrl, url))?.let { builder.encodedPath(it) }
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
        return (domainUrl.encodedPath + url.encodedPath
                + mRetrofitUrlManager.pathSize)
    }
}