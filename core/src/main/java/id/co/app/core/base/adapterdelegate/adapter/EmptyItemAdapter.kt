package id.co.app.core.base.adapterdelegate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.app.core.R
import id.co.app.core.base.adapterdelegate.DelegateAdapter
import id.co.app.core.base.adapterdelegate.DelegateAdapterItem
import id.co.app.core.base.adapterdelegate.DelegateAdapterItemClick
import id.co.app.core.base.adapterdelegate.adapter.model.EmptyItemModel
import id.co.app.core.databinding.ItemEmptyResultBinding


/**
 * Created by Lukas Kristianto on 4/29/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class EmptyItemAdapter(
	private val listener: DelegateAdapterItemClick
) : DelegateAdapter<EmptyItemModel, EmptyItemAdapter.EmptyItemViewHolder>(
	EmptyItemModel::class.java
) {
	override fun createViewHolder(parent: ViewGroup): EmptyItemViewHolder {
		return EmptyItemViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context), R.layout.item_empty_result, parent, false
			)
		)
	}

	override fun bindViewHolder(
        model: EmptyItemModel,
        viewHolder: EmptyItemViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
	) {
		viewHolder.bind(model)
	}

	inner class EmptyItemViewHolder(
		private val binding: ItemEmptyResultBinding
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(model: EmptyItemModel) {
			binding.model = model
			binding.emptyState.setTitle(model.title)
			binding.emptyState.setDescription(model.subtitle)
		}
	}
}