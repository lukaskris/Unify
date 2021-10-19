package id.co.app.core.base

import kotlinx.coroutines.CoroutineDispatcher


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class BaseDispatchers : Dispatchers {
	override fun io(): CoroutineDispatcher {
		return kotlinx.coroutines.Dispatchers.IO
	}

	override fun ui(): CoroutineDispatcher {
		return kotlinx.coroutines.Dispatchers.Main
	}
}