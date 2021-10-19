package id.co.app.retrofiturlhandler

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

object Utils {
    fun checkUrl(url: String): HttpUrl {
        val parseUrl = url.toHttpUrlOrNull()
        return parseUrl ?: throw InvalidUrlException(url)
    }

    fun <T> checkNotNull(`object`: T?, message: String?): T {
        if (`object` == null) {
            throw NullPointerException(message)
        }
        return `object`
    }
}