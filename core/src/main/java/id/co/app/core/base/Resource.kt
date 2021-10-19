package id.co.app.core.base

sealed class Resource<out T> {
    data class Success<out T>(
        val value: T
    ) : Resource<T>()

    data class Failure(
        val message: String
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()

    object Done : Resource<Nothing>()
}