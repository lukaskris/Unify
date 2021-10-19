package id.co.app.retrofiturlhandler.parser

import id.co.app.retrofiturlhandler.RetrofitUrlHandler
import okhttp3.HttpUrl


/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

class DefaultUrlParser : UrlParser {
    private val mDomainUrlParser: UrlParser by lazy { DomainUrlParser() }

    @Volatile
    private var mAdvancedUrlParser: UrlParser? = null

    @Volatile
    private var mSuperUrlParser: UrlParser? = null

    private var mRetrofitUrlHandler: RetrofitUrlHandler? = null


    override fun init(retrofitUrlHandler: RetrofitUrlHandler) {
        mRetrofitUrlHandler = retrofitUrlHandler
        mDomainUrlParser.init(retrofitUrlHandler)
    }

    override fun parseUrl(domainUrl: HttpUrl?, url: HttpUrl): HttpUrl {
        if (null == domainUrl) return url
        if (url.toString().contains(RetrofitUrlHandler.IDENTIFICATION_PATH_SIZE)) {
            if (mSuperUrlParser == null) {
                synchronized(this) {
                    if (mSuperUrlParser == null && mRetrofitUrlHandler != null) {
                        mSuperUrlParser = SuperUrlParser()
                        mSuperUrlParser!!.init(mRetrofitUrlHandler!!)
                    }
                }
            }
            return mSuperUrlParser!!.parseUrl(domainUrl, url)
        }

        if (mRetrofitUrlHandler?.isAdvancedModel() == true) {
            if (mAdvancedUrlParser == null) {
                synchronized(this) {
                    if (mAdvancedUrlParser == null && mRetrofitUrlHandler != null) {
                        mAdvancedUrlParser = AdvancedUrlParser()
                        mAdvancedUrlParser!!.init(mRetrofitUrlHandler!!)
                    }
                }
            }
            return mAdvancedUrlParser!!.parseUrl(domainUrl, url)
        }
        return mDomainUrlParser.parseUrl(domainUrl, url)
    }
}