package id.co.app.retrofiturlhandler

import okhttp3.HttpUrl


/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

interface OnUrlChangeListener {
    fun onUrlChangeBefore(oldUrl: HttpUrl, domainName: String?)

    fun onUrlChanged(newUrl: HttpUrl, oldUrl: HttpUrl)
}