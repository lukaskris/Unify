package id.co.app.retrofiturlhandler

import android.text.TextUtils
import id.co.app.retrofiturlhandler.Utils.checkUrl
import id.co.app.retrofiturlhandler.parser.DefaultUrlParser
import id.co.app.retrofiturlhandler.parser.UrlParser
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

class RetrofitUrlHandler private constructor() {
    private var baseUrl: HttpUrl? = null

    var pathSize = 0
        private set

    var isRun = true
    private var debug = false
    private val mDomainNameHub: MutableMap<String, HttpUrl> = HashMap()
    private val mInterceptor: Interceptor
    private val mListeners: MutableList<OnUrlChangeListener?> = ArrayList()
    private lateinit var mUrlParser: UrlParser

    companion object {
        private const val TAG = "RetrofitUrlHandler"
        private const val DOMAIN_NAME = "Domain-Name"
        private const val GLOBAL_DOMAIN_NAME = "id.co.app.sfa.globalDomainName"

        const val IDENTIFICATION_IGNORE = "#url_ignore"

        const val IDENTIFICATION_PATH_SIZE = "#baseurl_path_size="


        private object RetrofitUrlManagerHolder {
            val instance: RetrofitUrlHandler = RetrofitUrlHandler()
        }

        fun getInstance(): RetrofitUrlHandler {
            return RetrofitUrlManagerHolder.instance
        }
    }

    init {
        val urlParser: UrlParser = DefaultUrlParser()
        urlParser.init(this)
        setUrlParser(urlParser)
        mInterceptor = Interceptor { chain ->
            if (!isRun) chain.proceed(chain.request()) else chain.proceed(
                processRequest(
                    chain.request()
                )!!
            )
        }
    }

    fun with(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
            .addInterceptor(mInterceptor)
    }

    fun processRequest(request: Request?): Request? {
        if (request == null) return request
        val newBuilder: Request.Builder = request.newBuilder()
        val url: String = request.url.toString()

        if (url.contains(IDENTIFICATION_IGNORE)) {
            return pruneIdentification(newBuilder, url)
        }
        val domainName = obtainDomainNameFromHeaders(request)
        val httpUrl: HttpUrl?
        val listeners = listenersToArray()


        if (!TextUtils.isEmpty(domainName)) {
            notifyListener(request, domainName, listeners)
            httpUrl = fetchDomain(domainName ?: "")
            newBuilder.removeHeader(DOMAIN_NAME)
        } else {
            notifyListener(request, GLOBAL_DOMAIN_NAME, listeners)
            httpUrl = getGlobalDomain()
        }
        if (null != httpUrl) {
            val newUrl = mUrlParser.parseUrl(httpUrl, request.url)
            if (debug) Timber.tag(TAG).d(
                "The new url is { " + newUrl.toString() + " }, old url is { " + request.url.toString() + " }"
            )
            for (i in listeners.indices) {
                (listeners[i] as OnUrlChangeListener).onUrlChanged(
                    newUrl,
                    request.url
                )
            }
            return newBuilder
                .url(newUrl)
                .build()
        }
        return newBuilder.build()
    }


    private fun pruneIdentification(newBuilder: Request.Builder, url: String): Request {
        val split = url.split(IDENTIFICATION_IGNORE).toTypedArray()
        val buffer = StringBuffer()
        for (s in split) {
            buffer.append(s)
        }
        return newBuilder
            .url(buffer.toString())
            .build()
    }

    private fun notifyListener(request: Request, domainName: String?, listeners: Array<Any>?) {
        if (listeners != null) {
            for (i in listeners.indices) {
                (listeners[i] as OnUrlChangeListener).onUrlChangeBefore(request.url, domainName)
            }
        }
    }

    fun setDebug(debug: Boolean) {
        this.debug = debug
    }


    fun startAdvancedModel(baseUrl: String) {
        startAdvancedModel(checkUrl(baseUrl))
    }

    @Synchronized
    fun startAdvancedModel(baseUrl: HttpUrl) {
        this.baseUrl = baseUrl
        pathSize = baseUrl.pathSize
        val baseUrlPathSegments: List<String> = baseUrl.pathSegments
        if ("" == baseUrlPathSegments[baseUrlPathSegments.size - 1]) {
            pathSize -= 1
        }
    }

    fun isAdvancedModel(): Boolean = baseUrl != null

    fun getBaseUrl(): HttpUrl? {
        return baseUrl
    }

    fun setUrlNotChange(url: String): String {
        return url + IDENTIFICATION_IGNORE
    }

    fun setPathSizeOfUrl(url: String, pathSize: Int): String {
        require(pathSize >= 0) { "pathSize must be >= 0" }
        return url + IDENTIFICATION_PATH_SIZE + pathSize
    }

    @Synchronized
    fun getGlobalDomain(): HttpUrl? {
        return mDomainNameHub[GLOBAL_DOMAIN_NAME]
    }

    fun setGlobalDomain(globalDomain: String){
        synchronized(mDomainNameHub) {
            mDomainNameHub.put(
                GLOBAL_DOMAIN_NAME,
                checkUrl(globalDomain)
            )
        }
    }

    fun removeGlobalDomain() {
        synchronized(mDomainNameHub) { mDomainNameHub.remove(GLOBAL_DOMAIN_NAME) }
    }

    fun putDomain(domainName: String, domainUrl: String) {
        synchronized(mDomainNameHub) { mDomainNameHub.put(domainName, checkUrl(domainUrl)) }
    }

    @Synchronized
    fun fetchDomain(domainName: String): HttpUrl? {
        return mDomainNameHub[domainName]
    }

    fun removeDomain(domainName: String) {
        synchronized(mDomainNameHub) { mDomainNameHub.remove(domainName) }
    }

    fun clearAllDomain() {
        mDomainNameHub.clear()
    }

    @Synchronized
    fun haveDomain(domainName: String): Boolean {
        return mDomainNameHub.containsKey(domainName)
    }

    @Synchronized
    fun domainSize(): Int {
        return mDomainNameHub.size
    }

    fun setUrlParser(parser: UrlParser) {
        mUrlParser = parser
    }

    fun registerUrlChangeListener(listener: OnUrlChangeListener) {
        synchronized(mListeners) { mListeners.add(listener) }
    }

    fun unregisterUrlChangeListener(listener: OnUrlChangeListener) {
        synchronized(mListeners) { mListeners.remove(listener) }
    }

    private fun listenersToArray(): Array<Any> {
        var listeners: Array<Any> = emptyArray()
        synchronized(mListeners) {
            if (mListeners.size > 0) {
                listeners = mListeners.toTypedArray() as Array<Any>
            }
        }
        return listeners
    }

    private fun obtainDomainNameFromHeaders(request: Request): String? {
        val headers: List<String> = request.headers(DOMAIN_NAME)
        if (headers.isEmpty()) return null
        require(headers.size <= 1) { "Only one Domain-Name in the headers" }
        return request.header(DOMAIN_NAME)
    }
}