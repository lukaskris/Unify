package id.co.app.core.extension

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import id.co.app.core.BuildConfig
import id.co.app.core.base.Resource
import id.co.app.core.domain.AppResponse
import id.co.app.core.domain.exception.ExpiredDatabaseException
import retrofit2.HttpException
import java.net.SocketTimeoutException


/**
 * Created by Lukas Kristianto on 5/18/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

enum class ErrorCodes(val code: Int) {
	SocketTimeOut(-1)
}

fun <T> Throwable.handleException(): Resource<T> {
	this.printStackTrace()
	return when (this) {
		is HttpException -> {
			try {
				val type = Types.newParameterizedType(AppResponse::class.java, Any::class.java)
				val adapter: JsonAdapter<AppResponse<Any>> = Moshi.Builder().build().adapter(type)
				val message =
					adapter.fromJson(response()?.errorBody()?.string().orEmpty())?.message.orEmpty()
				Resource.Failure(getErrorMessage(this.code(), message))
			} catch (ex: Exception) {
				Resource.Failure("")
			}
		}
		is ExpiredDatabaseException -> Resource.Failure("Database telah kadaluarsa diharapkan untuk sync seluruh data lagi")
		is SocketTimeoutException -> Resource.Failure(getErrorMessage(ErrorCodes.SocketTimeOut.code))
		else -> Resource.Failure(getErrorMessage(Int.MAX_VALUE, message.orEmpty()))
	}
}


fun Throwable.handleExceptionString(): String {
	return when (this) {
		is HttpException -> {
			try {
				val type = Types.newParameterizedType(AppResponse::class.java, Any::class.java)
				val adapter: JsonAdapter<AppResponse<Any>> = Moshi.Builder().build().adapter(type)
				val message =
					adapter.fromJson(response()?.errorBody()?.string() ?: "")?.message ?: ""
				getErrorMessage(this.code(), message)
			} catch (ex: Exception) {
				ex.message ?: ""
			}
		}
		is ExpiredDatabaseException -> "Database telah kadaluarsa diharapkan untuk sync seluruh data lagi"
		is SocketTimeoutException -> getErrorMessage(ErrorCodes.SocketTimeOut.code)
		else -> getErrorMessage(Int.MAX_VALUE, message ?: "")
	}
}

fun Throwable.isUserUnauthorized(): Boolean {
	return this is HttpException && this.code() == 401
}

private fun getErrorMessage(code: Int, serverMessage: String = ""): String {
	return when (code) {
		ErrorCodes.SocketTimeOut.code -> "Koneksi ke server gagal"
		401 -> "Session telah kadaluarsa, silahkan login ulang"
		404 -> "Server tidak ditemukan"
		else -> if (BuildConfig.DEBUG) {
			serverMessage
		} else "Server sedang gangguan"
	}
}