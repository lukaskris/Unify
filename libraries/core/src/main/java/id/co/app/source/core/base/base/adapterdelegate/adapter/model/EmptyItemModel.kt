package id.co.app.source.core.base.base.adapterdelegate.adapter.model

import id.co.app.source.core.base.base.adapterdelegate.DelegateAdapterItem


/**
 * Created by Lukas Kristianto on 6/1/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class EmptyItemModel : DelegateAdapterItem {
	override fun id(): Any = EMPTY_ITEM_MODEL

	override fun content(): Any = EMPTY_ITEM_MODEL

	companion object {
		private const val EMPTY_ITEM_MODEL = "EmptyItemModel"
	}
}