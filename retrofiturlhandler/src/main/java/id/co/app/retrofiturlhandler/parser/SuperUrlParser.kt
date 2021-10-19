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

class SuperUrlParser : UrlParser {
    private var mRetrofitUrlHandler: RetrofitUrlHandler? = null
    private val mCache: Cache<String, String> by lazy { LruCache(100) }

    override fun init(retrofitUrlHandler: RetrofitUrlHandler) {
        mRetrofitUrlHandler = retrofitUrlHandler
    }

    override fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl {
        if (null == domainUrl) return url
        val builder: HttpUrl.Builder = url.newBuilder()
        val pathSize = resolvePathSize(url, builder)
        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url, pathSize)))) {
            for (i in 0 until url.pathSize) {
                builder.removePathSegment(0)
            }
            val newPathSegments: MutableList<String> = ArrayList()
            newPathSegments.addAll(domainUrl.encodedPathSegments)
            if (url.pathSize > pathSize) {
                val encodedPathSegments: List<String> = url.encodedPathSegments
                for (i in pathSize until encodedPathSegments.size) {
                    newPathSegments.add(encodedPathSegments[i])
                }
            } else require(url.pathSize >= pathSize) {
                String.format(
                    "Your final path is %s, the pathSize = %d, but the #baseurl_path_size = %d, #baseurl_path_size must be less than or equal to pathSize of the final path",
                    url.scheme.toString() + "://" + url.host + url.encodedPath,
                    url.pathSize,
                    pathSize
                )
            }
            for (PathSegment in newPathSegments) {
                builder.addEncodedPathSegment(PathSegment)
            }
        } else {
            builder.encodedPath(mCache.get(getKey(domainUrl, url, pathSize)) ?: "")
        }
        val httpUrl: HttpUrl = builder
            .scheme(domainUrl.scheme)
            .host(domainUrl.host)
            .port(domainUrl.port)
            .build()
        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url, pathSize)))) {
            mCache.put(getKey(domainUrl, url, pathSize), httpUrl.encodedPath)
        }
        return httpUrl
    }

    private fun getKey(domainUrl: HttpUrl, url: HttpUrl, PathSize: Int): String {
        return (domainUrl.encodedPath + url.encodedPath
                + PathSize)
    }

    private fun resolvePathSize(httpUrl: HttpUrl, builder: HttpUrl.Builder): Int {
        val fragment = httpUrl.fragment ?: return 0
        var pathSize = 0
        val newFragment = StringBuffer()
        if (fragment.indexOf("#") == -1) {
            val split = fragment.split("=").toTypedArray()
            if (split.size > 1) {
                pathSize = split[1].toInt()
            }
        } else {
            if (fragment.indexOf(RetrofitUrlHandler.IDENTIFICATION_PATH_SIZE) == -1) {
                val index = fragment.indexOf("#")
                newFragment.append(fragment.substring(index + 1, fragment.length))
                val split = fragment.substring(0, index).split("=").toTypedArray()
                if (split.size > 1) {
                    pathSize = split[1].toInt()
                }
            } else {
                val split: Array<String> =
                    fragment.split(RetrofitUrlHandler.IDENTIFICATION_PATH_SIZE).toTypedArray()
                newFragment.append(split[0])
                if (split.size > 1) {
                    val index = split[1].indexOf("#")
                    if (index != -1) {
                        newFragment.append(split[1].substring(index, split[1].length))
                        val substring = split[1].substring(0, index)
                        if (!TextUtils.isEmpty(substring)) {
                            pathSize = substring.toInt()
                        }
                    } else {
                        pathSize = split[1].toInt()
                    }
                }
            }
        }
        if (TextUtils.isEmpty(newFragment.toString())) {
            builder.fragment(null)
        } else {
            builder.fragment(newFragment.toString())
        }
        return pathSize
    }
}