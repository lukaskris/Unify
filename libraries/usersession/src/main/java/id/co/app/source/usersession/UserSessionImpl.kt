package id.co.app.source.usersession

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import id.co.app.source.core.domain.entities.login.User
import com.squareup.moshi.Moshi
import id.co.app.source.usersession.security.AESUtils
import kotlinx.coroutines.flow.map


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class UserSessionImpl(
	private val context: Context,
	private val moshi: Moshi
): UserSession {
	private val adapter by lazy { moshi.adapter(User::class.java) }
	private val Context.dataStore by preferencesDataStore("app_preferences")

	override suspend fun saveUser(user: User) {
		context.dataStore.edit { prefs -> prefs[KEY_USER] = AESUtils.encrypt(adapter.toJson(user)) }
	}

	override suspend fun deleteUser() {
		context.dataStore.edit { prefs -> prefs.remove(KEY_USER) }
	}

	override fun getUser() = context.dataStore.data.map {
		val userString = it[KEY_USER] ?: return@map null
		adapter.fromJson(AESUtils.decrypt(userString))
	}

	override fun getToken() = getUser().map { "Bearer " + it?.token }

	companion object {
		private const val KEY_USER_STRING = "key_user"
		val KEY_USER = stringPreferencesKey(AESUtils.encrypt(KEY_USER_STRING))
	}
}