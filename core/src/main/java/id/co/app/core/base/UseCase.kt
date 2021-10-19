package id.co.app.core.base

import kotlinx.coroutines.flow.Flow


/**
 * Created by Lukas Kristianto on 5/4/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
abstract class UseCase<T> {
	open fun execute(token: String): Flow<T> {
		TODO("Your default implementation here")
	}

	open fun execute(): Flow<T> {
		TODO("Your default implementation here")
	}
}