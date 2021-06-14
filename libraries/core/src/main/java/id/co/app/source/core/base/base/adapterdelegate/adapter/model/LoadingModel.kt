package id.co.app.source.core.base.base.adapterdelegate.adapter.model

import id.co.app.source.core.base.base.adapterdelegate.DelegateAdapterItem


/**
 * Created by Lukas Kristianto on 6/6/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class LoadingModel : DelegateAdapterItem {
	override fun id(): Any = LOADING

	override fun content(): Any = LOADING

	companion object {
		private const val LOADING = "Loading_Model"
	}
}