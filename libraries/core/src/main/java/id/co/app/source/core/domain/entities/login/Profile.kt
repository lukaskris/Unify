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
data class Profile(
	@field:Json(name = "cuis_id")
	val cuisId: String,
	@field:Json(name = "pernr")
	val pernr: String,
	@field:Json(name = "fullname")
	val fullName: String,
	@field:Json(name = "role")
	val role: String,
	@field:Json(name = "region_name")
	val regionName: String,
	@field:Json(name = "district_code")
	val districtCode: String,
	@field:Json(name = "superior_prnr")
	val superiorPrnr: String,
	@field:Json(name = "active")
	val active: String,
	@field:Json(name = "modified_at")
	val modifiedAt: String,
	@field:Json(name = "province")
	val province: String,
	@field:Json(name = "plantcode")
	val plantCode: String
) : Parcelable {
	fun getUserWithPernr() = "$pernr - $cuisId"
}