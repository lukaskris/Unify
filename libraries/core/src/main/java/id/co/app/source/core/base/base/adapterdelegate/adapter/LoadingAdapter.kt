package id.co.app.source.core.base.base.adapterdelegate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.app.source.core.R
import id.co.app.source.core.base.base.adapterdelegate.DelegateAdapter
import id.co.app.source.core.base.base.adapterdelegate.DelegateAdapterItem
import id.co.app.source.core.base.base.adapterdelegate.DelegateAdapterItemClick
import id.co.app.source.core.base.base.adapterdelegate.adapter.model.LoadingModel
import id.co.app.source.core.databinding.ItemLoadingBinding


/**
 * Created by Lukas Kristianto on 4/29/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class LoadingAdapter(
	private val listener: DelegateAdapterItemClick,
) : DelegateAdapter<LoadingModel, LoadingAdapter.LoadingItemViewHolder>(
	LoadingModel::class.java
) {
	override fun createViewHolder(parent: ViewGroup): LoadingItemViewHolder {
		return LoadingItemViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context), R.layout.item_loading, parent, false
			)
		)
	}

	override fun bindViewHolder(
		model: LoadingModel,
		viewHolder: LoadingItemViewHolder,
		payloads: List<DelegateAdapterItem.Payloadable>,
	) {
		viewHolder.bind(model)
	}

	inner class LoadingItemViewHolder(
		binding: ItemLoadingBinding,
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(model: LoadingModel) {
		}
	}
}