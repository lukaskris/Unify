package id.co.app.source.core.domain.network

import id.co.app.source.core.domain.entities.login.LoginParameter

class AppClientImpl(
    private val service: AppService
): AppClient{
    override suspend fun login(userId: String, password: String, locationId: String) =
        service.login(
            LoginParameter(
                userId,
                password,
                locationId
            )
        ).data
}