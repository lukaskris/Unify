package id.co.app.source.usersession

import id.co.app.source.core.domain.entities.login.User
import kotlinx.coroutines.flow.Flow


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
interface UserSession {
	suspend fun saveUser(user: User)
	suspend fun deleteUser()
	fun getUser(): Flow<User?>
	fun getToken(): Flow<String>
}