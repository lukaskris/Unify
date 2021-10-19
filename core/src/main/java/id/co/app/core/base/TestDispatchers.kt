package id.co.app.core.base

import kotlinx.coroutines.CoroutineDispatcher


/**
 * Created by Lukas Kristianto on 4/28/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class TestDispatchers : Dispatchers {
	override fun io(): CoroutineDispatcher {
		return kotlinx.coroutines.Dispatchers.Unconfined
	}

	override fun ui(): CoroutineDispatcher {
		return kotlinx.coroutines.Dispatchers.Unconfined
	}
}