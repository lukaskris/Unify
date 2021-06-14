package id.co.app.source.core.domain.network

import id.co.app.source.core.domain.AppResponse
import id.co.app.source.core.domain.entities.login.LoginParameter
import id.co.app.source.core.domain.entities.login.User
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AppService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    suspend fun login(@Body loginParameter: LoginParameter): AppResponse<User>
}