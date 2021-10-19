package id.co.app.querybuilder

import java.io.Serializable

/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

open class BaseFilter (
    var search : String? = null,
    var offset: Int = 0,
    var limit: Int = 20
) : Serializable