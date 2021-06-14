package id.co.app.source.core.domain.entities.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@JsonClass(generateAdapter = true)
class LoginParameter(
	@Json(name = "cuis_id")
	val userName: String,
	@Json(name = "password")
	val password: String,
	@Json(name = "location_id")
	val locationId: String
)