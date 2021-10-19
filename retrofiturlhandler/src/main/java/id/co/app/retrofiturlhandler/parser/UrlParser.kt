package id.co.app.retrofiturlhandler.parser

import id.co.app.retrofiturlhandler.RetrofitUrlHandler
import okhttp3.HttpUrl


/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

interface UrlParser {
    fun init(retrofitUrlHandler: RetrofitUrlHandler)

    fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl
}