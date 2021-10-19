package id.co.app.retrofiturlhandler

import android.text.TextUtils

/**
 * Created by Lukas Kristianto on 09/08/21 10.34.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

class InvalidUrlException(url: String?) :
    RuntimeException("You've configured an invalid url : " + if (TextUtils.isEmpty(url)) "EMPTY_OR_NULL_URL" else url)