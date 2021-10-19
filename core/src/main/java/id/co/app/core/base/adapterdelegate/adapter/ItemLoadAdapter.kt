package id.co.app.core.base.adapterdelegate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import id.co.app.core.R


/**
 * Created by Lukas Kristianto on 6/1/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class ItemLoadAdapter : LoadStateAdapter<ItemLoadAdapter.LoadStateViewHolder>() {

	override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

		val progress = holder.itemView.findViewById<ProgressBar>(R.id.progress_bar)

		progress.isVisible = loadState is LoadState.Loading
	}

	override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
		return LoadStateViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.item_load, parent, false)
		)
	}

	class LoadStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
}