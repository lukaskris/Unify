package id.co.app.source.core.domain.network

import id.co.app.source.core.domain.entities.login.User

interface AppClient {
    suspend fun login(userId: String, password: String, locationId: String): User?
}