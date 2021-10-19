package id.co.app.core.base.adapterdelegate.adapter.model

import id.co.app.core.base.adapterdelegate.DelegateAdapterItem


/**
 * Created by Lukas Kristianto on 6/6/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class LoadingModel (
	val type: Type = Type.PROGRESS_BAR
): DelegateAdapterItem {
	override fun id(): Any = LOADING

	override fun content(): Any = LOADING

	enum class Type{
		PROGRESS_BAR, VERTICAL_SHIMMER, HORIZONTAL_SHIMMER
	}

	companion object {
		private const val LOADING = "Loading_Model"
	}
}