package id.co.app.core.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@JsonClass(generateAdapter = true)
data class AppResponse<T>(
	@field:Json(name = "status")
	val status: Boolean,
	@field:Json(name = "code")
	val code: Int,
	@field:Json(name = "message")
	val message: String,
	@field:Json(name = "data")
	val data: T?
)