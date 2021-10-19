package id.co.app.core.extension

import java.util.*


/**
 * Created by Lukas Kristianto on 5/4/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
fun Int?.withDefault() = this ?: 0
fun String?.withDefault() = this ?: ""
fun String?.withNaN() = this ?: "NaN"
fun String?.withStripe() = if((this ?: "-").isEmpty()) "-" else this ?: "-"
fun String?.withNaNEmpty() = if(this?.isEmpty() == true) "NaN" else this ?: "NaN"
fun Int?.withNaN() = this ?: "NaN"
fun Date?.withDefault() = this ?: Date()
fun Double?.withDefault() = this ?: 0.0
fun Double?.withNaN() = this ?: "NaN"
fun Map<String, Any>?.getOrNull(key: String): Any?{
	return if(this == null) null
	else if(this.contains(key)){
		this[key]
	} else {
		null
	}
}