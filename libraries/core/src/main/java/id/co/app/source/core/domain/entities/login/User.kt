package id.co.app.source.core.domain.entities.login

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class User(
	@field:Json(name = "profile")
	val profile: Profile,
	@field:Json(name = "token")
	val token: String,
	@field:Json(name = "superior")
	val superior: Profile?,
	@field:Json(name = "supervisors")
	val supervisors: List<Profile>?,
): Parcelable