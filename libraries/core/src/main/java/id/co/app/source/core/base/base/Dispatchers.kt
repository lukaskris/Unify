package id.co.app.source.core.base.base

import kotlinx.coroutines.CoroutineDispatcher


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
interface Dispatchers {
	fun io(): CoroutineDispatcher
	fun ui(): CoroutineDispatcher
}